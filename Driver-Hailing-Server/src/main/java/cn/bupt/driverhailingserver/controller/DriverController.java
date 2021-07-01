package cn.bupt.driverhailingserver.controller;

import cn.bupt.driverhailingserver.entity.*;
import cn.bupt.driverhailingserver.entity.RequestOrderForDriver;
import cn.bupt.driverhailingserver.repository.*;
import cn.bupt.driverhailingserver.server.WebSocketServer;
import cn.bupt.driverhailingserver.service.*;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


//@RequestMapping("/user/driver")
@RefreshScope
@RestController
public class DriverController {
    final RequestOrderForDriverRepository requestOrderForDriverRepository;
    final CommentRepository commentRepository;
    final OrderForUserRepository orderForUserRepository;
    final DriverRepository driverRepository;
    final RequestOrderRepository requestOrderRepository;
    final CustomerRepository customerRepository;
    final AreaRepository areaRepository;
    final AreaService areaService;
    final CustomerService customerService;
    final DriverService driverService;
    final OrderForUserService orderForUserService;


    @Autowired
    public DriverController(CommentRepository commentRepository, DriverRepository driverRepository, RequestOrderRepository requestOrderRepository,
                            OrderForUserRepository orderForUserRepository, CustomerRepository customerRepository, AreaRepository areaRepository,
                            AreaService areaService, CustomerService customerService, DriverService driverService, OrderForUserService orderForUserService, RequestOrderForDriverRepository requestOrderForDriverRepository) {
        this.commentRepository = commentRepository;
        this.orderForUserRepository = orderForUserRepository;
        this.driverRepository = driverRepository;
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.areaRepository = areaRepository;
        this.requestOrderForDriverRepository = requestOrderForDriverRepository;
        this.areaService = areaService;
        this.customerService = customerService;
        this.driverService = driverService;
        this.orderForUserService = orderForUserService;
    }


    @Value("${eureka.instance.hostname}")
    private String name;
    /*注入“服务提供者”的端口号*/
    @Value("${server.port}")
    private String port;

    /*提供的接口，用于返回信息*/
    @RequestMapping("/test-info")
    public String testInfo() {
        //返回数据
        return name + " port:" + port;
    }


    @RequestMapping("/updateDriver")
    @SentinelResource
    public String updateDriver(String driverName) {
        try {
            System.out.println("updateDriver: " + driverName);
            Driver driver = driverService.findByDriverName(driverName);

            //暂定市中心人最多，司机们都住在那附近，因此在空闲状态司机将会前往市中心
            //城市大小50km * 50km, 每1km有一个检测点
            int curX = driver.getCurX();
            int curY = driver.getCurY();
            double difX = Math.abs(curX - driver.getDesX());
            double difY = Math.abs(curY - driver.getDesY());
            if (difX == 0) {
                if (difY == 0) {
                    //已到达 更新各种信息 busy free 订单状况 消息推送 等
                } else// difx = 0 dify != 0 y方向走
                {
                    curY += (driver.getDesY() - curY) / difY;
                }
            } else {
                if (difY == 0)// difx!=0 dify=0 往x方向走
                {
                    curX += (driver.getDesX() - curX) / difX;
                } else // 往短的一方走
                {
                    if (difX <= difY) {
                        curX += (driver.getDesX() - curX) / difX;
                    } else {
                        curY += (driver.getDesY() - curY) / difY;
                    }
                }
            }
            driver.setCurX(curX);
            driver.setCurY(curY);

            // 更新司机积分信息
            driver.setDriverPoint(driver.getFinishCount() * 10 + driver.getFinishDistance());
            driver.setDriverLevel(driver.getDriverPoint() % 50);

            // 更新司机订单信息
            for (int i = 0; i < driver.getRequestOrderForDriverList().size(); ++i) {
                RequestOrderForDriver requestOrderForDriver = driver.getRequestOrderForDriverList().get(i);
                RequestOrder requestOrder = requestOrderRepository.findByCustomerName(requestOrderForDriver.getCustomerName());
                if (requestOrder.getIfCheck() == 1 && !requestOrder.getDriverName().equals(driverName)) {
                    driver.getRequestOrderForDriverList().remove(requestOrderForDriver);
                }
            }
            Area area = areaService.findByDriverId(driver.getId());
            if (driver.getIfBusy() == 1) {
                //        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
                area.setSectorId((driver.getDesY() / 3) * 17 + driver.getDesX() / 3);

            } else {
                //        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
                area.setSectorId((driver.getCurY() / 3) * 17 + driver.getCurX() / 3);
            }
            areaRepository.save(area);

            driverRepository.save(driver);
            return "司机信息更新成功";
        } catch (Exception e) {
            return "祝您生活愉快";
        }
    }


    @RequestMapping("/handleRequestOrder")
    @SentinelResource
    public String handleRequestOrder(String driverName, int orderNum) {
        try {
            Driver driver = driverService.findByDriverName(driverName);
            if (driver.getRequestOrderForDriverList() != null)//如果有订单请求
            {
                List<RequestOrderForDriver> driverRequestOrderForDriverList = driver.getRequestOrderForDriverList();
//            System.out.println("---------------------------");
//            System.out.println(driver.getRequestOrderList().get(orderNum).getCustomer().getCustomerName());
//            System.out.println("---------------------------");
                if (driverRequestOrderForDriverList != null && driverRequestOrderForDriverList.size() > orderNum) {
                    //接受orderNum号订单
                    RequestOrderForDriver requestOrderForDriver = driverRequestOrderForDriverList.get(orderNum);
                    RequestOrder requestOrder = requestOrderRepository.findByCustomerName(requestOrderForDriver.getCustomerName());
                    if (requestOrder.getIfCheck() == 0 || requestOrder.getDriverName().equals(driverName)) //该订单没有被其他用户接走
                    {
                        requestOrder.setIfCheck(1);
                        requestOrderRepository.save(requestOrder);
                        requestOrder.setDriverName(driverName);
                        requestOrderRepository.save(requestOrder);

                        driver.getRequestOrderForDriverList().clear();
                        List<RequestOrderForDriver> tempOrder = new ArrayList<RequestOrderForDriver>();
                        tempOrder.add(requestOrderForDriver);
                        driver.setRequestOrderForDriverList(tempOrder);
//                    driver.getRequestOrderList().add(requestOrder);
                        driver.setIfBusy(1);
                        driver.setDesX(requestOrder.getCurX());
                        driver.setDesY(requestOrder.getCurY());
                        requestOrderForDriverRepository.save(requestOrderForDriver);
                        driverRepository.save(driver);

                        //消息推送给用户 已有司机接单
                        // 司机界面变成前往 界面
                        WebSocketServer webSocketServer = new WebSocketServer();
                        String message = "司机：" + driverName + "前往上车地点";
                        try {
                            webSocketServer.sendInfo(message, requestOrder.getCustomer().getId());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        return "接受" + orderNum + "号订单";
                    } else //已被其他司机接走
                    {
                        driver.getRequestOrderForDriverList().remove(orderNum);
                        driverRepository.save(driver);
                        return "该订单已被接走，请选择其他订单";
                    }
                } else {
                    return "无该约车订单";
                }
            } else {
                return "无约车订单";
            }
        } catch (Exception e) {
            return "祝您生活愉快";
        }
    }

    @RequestMapping("/takeCustomer")
    @SentinelResource
    public String takeCustomer(String driverName) {
        try {
            Driver driver = driverService.findByDriverName(driverName);
            if(driver == null)
            {
                return "无该司机";
            }
            // TODO：需要删除MANYTOMANY关系 不然获取不到正确用户信息
//        RequestOrder requestOrder = driver.getRequestOrderList().get(0);
            RequestOrderForDriver requestOrderForDriver = driver.getRequestOrderForDriverList().get(0);
            if(requestOrderForDriver == null)
            {
                return "无订单，无法接单";
            }
            Customer customer = customerService.findByCustomerName(requestOrderForDriver.getCustomerName());
            if(customer == null)
            {
                return "该用户不存在，订单取消";
            }
//        Order order = new Order();TODO
//        OrderForDriver order= new OrderForDriver();
            OrderForUser order = new OrderForUser();

            driver.setDesX(requestOrderForDriver.getDesX());
            driver.setDesY(requestOrderForDriver.getDesY());
            order.setCustomer(customer);
            order.setDriver(driver);
//        order.setComment(new Comment("默认五星好评",5));
            order.setCustomerName(customer.getCustomerName());
            order.setDriverName(driverName);
            order.setStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            order.setEndTime("");
            order.setServiceLevel(driver.getServiceLevel());
            order.setCurX(driver.getCurX());
            order.setCurY(driver.getCurY());
            order.setDesX(requestOrderForDriver.getDesX());
            order.setDesY(requestOrderForDriver.getDesY());
            order.setDistance(Math.abs(order.getCurX() - order.getDesX()) + Math.abs(order.getCurY() - order.getDesY()));
            order.setPrice(order.getDistance() * 3 + 13);
            order.setCurState(0);
            Long id = orderForUserRepository.save(order).getId();
//        driver.getOrderForDriverList().add(order);
//        driver.setCurOrder(order);
            driver.setCurOrderId(id);
            driver.setCurCustomerName(customer.getCustomerName());
            driver.getRequestOrderForDriverList().clear();
            //TODO:乘客已上车 请求订单生命结束 从数据库中删去
//        requestOrderRepository.findById(requestOrder.getId());

            driverRepository.save(driver);


            WebSocketServer webSocketServer = new WebSocketServer();
            String message = "司机：" + driverName + "请乘客系好安全带，前往目的地";
            try {
                webSocketServer.sendInfo(message, customer.getId());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "请乘客系好安全带，正在前往目的地";
        } catch (Exception e) {
            return "祝您生活愉快";
        }
    }

    @RequestMapping("/finishOrder")
    @SentinelResource
    public String finishOrder(String driverName) {
        try {
            Driver driver = driverService.findByDriverName(driverName);
//        OrderForUser order  = driver.getCurOrder();
//        String orderId = driver.getCurOrderId();
            OrderForUser order = orderForUserService.findById(driver.getCurOrderId());//TODO
            // 更新order 信息
            if (order == null) {
                return "乘客已送达";
            }
            order.setEndTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));//TODO
//        order.setEndTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

            //更新driver 信息
//        driver.setCurOrderId((long) -1);
//        driver.setIfBusy(0);
//        driver.setFinishCount(driver.getFinishCount() + 1);
            driver.setCurX(order.getDesX());
            driver.setCurY(order.getDesY());
            driver.setFinishDistance(driver.getFinishDistance() + order.getDistance());
            System.out.println("-------------");
            System.out.println(driver.getFinishDistance());
            System.out.println(order.getDistance());
            System.out.println("-------------");
            driver.setDesX(25);
            driver.setDesY(25);

            Area area = areaService.findByDriverId(driver.getId());
//        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
            area.setSectorId((driver.getDesY() / 3) * 17 + driver.getDesX() / 3);
            areaRepository.save(area);
            driverRepository.save(driver);
//        orderRepository.save(order);TODO
            return "乘客已送达";
        } catch (Exception e) {
            return "祝您生活愉快";
        }
    }

    @GetMapping("/searchOrder")
    @SentinelResource
    public String searchOrder(String driverName) {
        try {
//        Customer customer =customerRepository.findByCustomerName(customerName);
            Driver driver = driverService.findByDriverName(driverName);
            List<OrderForUser> orderList = orderForUserService.findByDriverName(driverName);
            if (orderList == null) {
                return "无订单";
            }
            StringBuilder retStr = new StringBuilder();
            for (OrderForUser orderForUser : orderList) {
                retStr.append("Order0:").append(orderForUser.toString()).append("\n");
                System.out.println("Order0:" + orderForUser.toString());
            }
            return retStr.toString();
        } catch (Exception e) {
            return "祝您生活愉快";
        }
    }
}

