package distributed.sys.customer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import distributed.sys.customer.dao.CustomerRepository;
import distributed.sys.customer.dao.DriverRepository;
import distributed.sys.customer.dao.OrderRepository;
import distributed.sys.customer.dao.RequestOrderRepository;
import distributed.sys.customer.entity.*;
import distributed.sys.customer.server.WebSocketServer;
import distributed.sys.customer.service.CustomerService.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public OrderRepository orderRepository;

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }


    public DriverRepository driverRepository;

    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }

    public static RequestOrderRepository requestOrderRepository;

    @Autowired
    public void setRequestOrderRepository(RequestOrderRepository requestOrderRepository) {
        CustomerService.requestOrderRepository = requestOrderRepository;
    }

    public CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
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
                    return "redirect:/Index";
                } else {
                    return "密码错误";
                }
            } else {
                return "用户不存在";
            }
        }
    }

    @RequestMapping("/logout")
    public String logout(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);
        driver.setIfLogin(0);
        System.out.println("司机" + driverName + "已登出");
        return "redirect:/login";
    }

    @RequestMapping("/register")
    public String register(Driver driver) {
        if (driverRepository.findByDriverName(driver.getDriverName()) == null)
        {
            driver.setFinishCount(0);
            driver.setFinishDistance(0);
//        driver.setServiceLevel(1);
            driver.setDriverLevel(1);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setIfLogin(0);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setDesX(driver.getCurX());
            driver.setDesY(driver.getCurY());

            driver.setOrderList(new ArrayList<>());
            driver.setCurOrder(new Order());
            driver.setCommentList(new ArrayList<>());
            driver.setRequestOrderList(new ArrayList<>());
            driver.setCurCustomerName("");
            driverRepository.save(driver);

            return "redirect:/login";
        }
        else{
            System.out.println("司机用户名已存在");
            return "redirect:/register";
        }

    }

    @RequestMapping("/edit")
    public String edit(String driverName, int serviceLevel) {
        Driver driver = driverRepository.findByDriverName(driverName);
        driver.setServiceLevel(serviceLevel);
        driverRepository.save(driver);
        return "redirect:/Index";
    }

    @RequestMapping("/Index")//初始页面 暂时返回视图
    @JsonView(Views.Public.class)
    public Driver Index(String driverName) {
        return driverRepository.findByDriverName(driverName);
    }


    @RequestMapping("/updateDriver")
    public void updateDriver(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);

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
        driver.setServiceLevel(driver.getDriverPoint() % 50);
        // 更新司机订单信息
        for (int i = 0; i < driver.getRequestOrderList().size(); ++i) {
            RequestOrder requestOrder = driver.getRequestOrderList().get(i);
            if (requestOrder.getIfCheck() == 1 && !requestOrder.getDriverName().equals(driverName)) {
                driver.getRequestOrderList().remove(requestOrder);
            }
        }

        driverRepository.save(driver);
    }


    @RequestMapping("/handleRequestOrder")
    public String handleRequestOrder(String driverName, int orderNum) {
        Driver driver = driverRepository.findByDriverName(driverName);
        if (driver.getRequestOrderList() != null)//如果有订单请求
        {
            List<RequestOrder> driverRequestOrders = driver.getRequestOrderList();
            if (driverRequestOrders != null && driverRequestOrders.size() >= orderNum) {
                //接受orderNum号订单
                RequestOrder requestOrder = driverRequestOrders.get(orderNum);
                if (requestOrder.getIfCheck() == 0) //该订单没有被其他用户接走
                {
                    requestOrder.setIfCheck(1);
                    requestOrder.setDriver(driver);
                    requestOrder.setDriverName(driverName);
                    requestOrderRepository.save(requestOrder);

                    driver.getRequestOrderList().clear();
                    driver.getRequestOrderList().add(requestOrder);
                    driver.setIfBusy(1);
                    driver.setDesX(requestOrder.getCurX());
                    driver.setDesY(requestOrder.getCurY());
                    driverRepository.save(driver);

                    //消息推送给用户 已有司机接单
                    // 司机界面变成前往 界面
                    return "接受" + orderNum + "号订单";
                } else //已被其他司机接走
                {
                    driver.getRequestOrderList().remove(orderNum);
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
        RequestOrder requestOrder = driver.getRequestOrderList().get(0);
        Customer customer = customerRepository.findByCustomerName(requestOrder.getCustomerName());
        Order order = new Order();

        driver.setDesX(requestOrder.getDesX());
        driver.setDesY(requestOrder.getDesY());

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
        order.setDesX(requestOrder.getDesX());
        order.setDesY(requestOrder.getDesY());
        order.setDistance(Math.abs(order.getCurX() - order.getDesX()) + Math.abs(order.getCurY() - order.getDesY()));
        order.setPrice(order.getDistance() * 3 + 13);
        order.setCurState(0);

        driver.getOrderList().add(order);
        driver.setCurOrder(order);
        driver.setCurCustomerName(customer.getCustomerName());
        //乘客已上车 请求订单生命结束 从数据库中删去
        requestOrderRepository.delete(requestOrder);
        orderRepository.save(order);
        driverRepository.save(driver);

        WebSocketServer webSocketServer = new WebSocketServer();
        String message = "司机：" + driverName +"已前往指定地点，请耐心等候";
        try {
            webSocketServer.sendInfo(message, customer.getId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "请乘客系好安全带，正在前往目的地";
    }

    @RequestMapping("/finishOrder")
    public String finishOrder(String driverName)
    {
        Driver driver =  driverRepository.findByDriverName(driverName);
        Order order  = driver.getCurOrder();
        //更新order 信息
        order.setEndTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));

        //更新driver 信息
        driver.setCurOrder(new Order());
        driver.setIfBusy(0);
        driver.setFinishCount(driver.getFinishCount() + 1);
        driver.setFinishDistance(driver.getFinishCount() + order.getDistance());
        driver.setDesX(25);
        driver.setDesY(25);

        driverRepository.save(driver);
        orderRepository.save(order);
        return "乘客已送达";
    }

}

