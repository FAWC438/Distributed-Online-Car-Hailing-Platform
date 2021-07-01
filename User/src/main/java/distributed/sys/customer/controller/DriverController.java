package distributed.sys.customer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import distributed.sys.customer.entity.*;
import distributed.sys.customer.repository.*;
import distributed.sys.customer.server.WebSocketServer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;


@RestController
@RequestMapping("/user/driver")
public class DriverController {
    final CommentRepository commentRepository;
    final OrderForUserRepository orderForUserRepository;
    final DriverRepository driverRepository;
    final RequestOrderRepository requestOrderRepository;
    //    final OrderRepository orderRepository;
    final CustomerRepository customerRepository;
    final AreaRepository areaRepository;
final RequestOrderForDriverRepository requestOrderForDriverRepository;

    @Autowired
    public DriverController(CommentRepository commentRepository, DriverRepository driverRepository,RequestOrderForDriverRepository requestOrderForDriverRepository
            , RequestOrderRepository requestOrderRepository, OrderForUserRepository orderForUserRepository, CustomerRepository customerRepository, AreaRepository areaRepository) {
        this.commentRepository = commentRepository;
        this.orderForUserRepository = orderForUserRepository;
        this.driverRepository = driverRepository;
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.areaRepository = areaRepository;
        this.requestOrderForDriverRepository= requestOrderForDriverRepository;
    }

    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String driverName = request.getParameter("driverName");
        String password = request.getParameter("password");

        if (null == driverName || null == password) {
            return "用户名或密码错误";
        } else {
            if (driverRepository.findByDriverName(driverName) != null) {
                if (driverRepository.findByDriverName(driverName).getPassword().equals(password)) {
                    System.out.println("司机" + driverName + "已登录");
                    Driver driver = driverRepository.findByDriverName(driverName);
                    driver.setIfLogin(1);
                    driverRepository.save(driver);
                    return driverName+ "登陆成功";
                } else {
                    return "密码错误";
                }
            } else {
                return "用户不存在";
            }
        }
    }

    @RequestMapping("/logout")
    public String logout(String driverName) {// TODO：更改代码
        Driver driver = driverRepository.findByDriverName(driverName);
        if(driver == null)
        {
            return "该用户不存在";
        }
        driver.setIfLogin(0);
        System.out.println("司机" + driverName + "已登出");
        return driverName+ "登出成功";
    }

    @RequestMapping("/register")
    public String register(String driverName, String password) {
        if (driverRepository.findByDriverName(driverName) == null) {
            Driver driver = new Driver();
            driver.setDriverName(driverName);
            driver.setPassword(password);
            driver.setFinishCount(0);
            driver.setFinishDistance(0);
//        driver.setServiceLevel(1);
            driver.setDriverLevel(1);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setIfLogin(0);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setCurX(25);
            driver.setCurY(25);
            driver.setDesX(driver.getCurX());
            driver.setDesY(driver.getCurY());

//            driver.setOrderList(new ArrayList<>());
//            driver.setCurOrder(new Order());
//            driver.setCommentList(new ArrayList<>());
//            driver.setRequestOrderList(new ArrayList<>());
            driver.setCurCustomerName("");
            driverRepository.save(driver);
            Area area = new Area();
            area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
            area.setSectorId((driver.getCurY() / 3) * 17 + driver.getCurX() / 3);
            areaRepository.save(area);
            return driverName+ "注册成功";
        } else {
            System.out.println("司机用户名已存在");
            return "司机用户名已存在请重新注册";
        }

    }

    @RequestMapping("/edit")
    public String edit(String driverName, int serviceLevel) {
        Driver driver = driverRepository.findByDriverName(driverName);
        if (driver == null) {
            return driverName+ "该司机不存在";
        }
        driver.setServiceLevel(serviceLevel);
        driverRepository.save(driver);
        return "编辑成功";
    }

    @RequestMapping("/Index")//初始页面 暂时返回视图
    @JsonView(Views.Public.class)
    public Driver Index(String driverName) {
        return driverRepository.findByDriverName(driverName);
    }


    @RequestMapping("/updateDriver")
    public String updateDriver(String driverName) {
        System.out.println("updateDriver: " + driverName);
        Driver driver = driverRepository.findByDriverName(driverName);
        if(driver == null)
        {
            return "无该司机，请检查参数";
        }
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
        Area area = areaRepository.findByDriverId(driver.getId());
        if (driver.getIfBusy() == 1) {
            //        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
            area.setSectorId((driver.getDesY() / 3) * 17 + driver.getDesX() / 3);

        } else {
            //        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
            area.setSectorId((driver.getCurY() / 3) * 17 + driver.getCurX() / 3);
        }
        areaRepository.save(area);

        driverRepository.save(driver);
        return driverName+ "司机信息更新成功";
    }


    @RequestMapping("/handleRequestOrder")
    public String handleRequestOrder(String driverName, int orderNum) {
        Driver driver = driverRepository.findByDriverName(driverName);
        if(driver == null)
        {
            return "司机不存在，请查看接口参数是否正确";
        }
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

                    return driverName+ "接受" + orderNum + "号订单";
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
    }

    @RequestMapping("/takeCustomer")
    public String takeCustomer(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);
        if(driver == null)
        {
            return "无该司机";
        }
        // TODO：需要删除MANYTOMANY关系 不然获取不到正确用户信息
//        RequestOrder requestOrder = driver.getRequestOrderList().get(0);
        List<RequestOrderForDriver> requestOrderForDriverList = driver.getRequestOrderForDriverList();
        if(requestOrderForDriverList == null)
        {
            return "无订单列表";
        }
        RequestOrderForDriver requestOrderForDriver = requestOrderForDriverList.get(0);
        if(requestOrderForDriver == null)
        {
            return "无订单，无法接单";
        }
        Customer customer = customerRepository.findByCustomerName(requestOrderForDriver.getCustomerName());
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
        order.setCurX(customer.getCurX());
        order.setCurY(customer.getCurY());
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
        requestOrderForDriverRepository.save(requestOrderForDriver);
        orderForUserRepository.save(order);
        driverRepository.save(driver);


        WebSocketServer webSocketServer = new WebSocketServer();
        String message = "司机：" + driverName + "请乘客系好安全带，前往目的地";
        try {
            webSocketServer.sendInfo(message, customer.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return customer.getCustomerName() + "请乘客系好安全带，正在前往目的地";
    }

    @RequestMapping("/finishOrder")
    public String finishOrder(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);
//        OrderForUser order  = driver.getCurOrder();
//        String orderId = driver.getCurOrderId();
        OrderForUser order = orderForUserRepository.findById(driver.getCurOrderId()).orElse(null);//TODO
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

        Area area = areaRepository.findByDriverId(driver.getId());
//        area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
        area.setSectorId((driver.getDesY() / 3) * 17 + driver.getDesX() / 3);
        areaRepository.save(area);
        driverRepository.save(driver);
//        orderRepository.save(order);TODO
        return "乘客已送达";
    }

    @GetMapping("/searchOrder")
    public String searchOrder(String driverName) {
//        Customer customer =customerRepository.findByCustomerName(customerName);
        Driver driver = driverRepository.findByDriverName(driverName);
        List<OrderForUser> orderList = orderForUserRepository.findByDriverName(driverName);
        if (orderList == null) {
            return "无订单";
        }
        StringBuilder retStr = new StringBuilder();
        for (OrderForUser orderForUser : orderList) {
            retStr.append("Order0:").append(orderForUser.toString()).append("\n");
            System.out.println("Order0:" + orderForUser.toString());
        }
        return retStr.toString();
    }
}

