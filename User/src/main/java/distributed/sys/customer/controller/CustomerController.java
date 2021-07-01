package distributed.sys.customer.controller;

//import io.micrometer.core.instrument.Tag;

import distributed.sys.customer.entity.*;
import distributed.sys.customer.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//@Tag(name = "乘车用户接口")
@RestController
@RequestMapping("/user/customer")
public class CustomerController {
    final CommentRepository commentRepository;
    final OrderForUserRepository orderForUserRepository;
    final DriverRepository driverRepository;
    final RequestOrderRepository requestOrderRepository;
    //    final OrderRepository orderRepository;
    final CustomerRepository customerRepository;
    final AreaRepository areaRepository;
    final RequestOrderForDriverRepository requestOrderForDriverRepository;

    @Autowired
    public CustomerController(CommentRepository commentRepository, DriverRepository driverRepository, RequestOrderForDriverRepository requestOrderForDriverRepository
            , RequestOrderRepository requestOrderRepository, OrderForUserRepository orderForUserRepository, CustomerRepository customerRepository, AreaRepository areaRepository) {
        this.commentRepository = commentRepository;
        this.orderForUserRepository = orderForUserRepository;
        this.driverRepository = driverRepository;
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.areaRepository = areaRepository;
        this.requestOrderForDriverRepository = requestOrderForDriverRepository;
    }


    @GetMapping("/")
    public String index() {
        return "login";
    }


    @RequestMapping("/login")
    public String login(HttpServletRequest request) {
        String customerName = request.getParameter("customerName");
        String password = request.getParameter("password");

        if (null == customerName || null == password) {
            return "用户名或密码错误";
        } else {
            if (customerRepository.findByCustomerName(customerName) != null) {
                if (customerRepository.findByCustomerName(customerName).getPassword().equals(password)) {
                    System.out.println("用户" + customerName + "已登录");
                    Customer customer = customerRepository.findByCustomerName(customerName);
                    customer.setIfLogin(1);
                    customerRepository.save(customer);
                    return customerName + "用户已登录";
                } else {
                    return "密码错误";
                }
            } else {
                return "用户不存在";
            }
        }


//        if (null == customerName || null == password) {
//            return "redirect:/";
//        }
//        //不正确的用户名密码
//        if (!customerName.equals("test") || !password.equals("123456")) {
//            //登录失败设置标志位null 因为没有登出
//            request.getSession().setAttribute("loginName", null);
//            return "redirect:/";
//        }
    }

    @RequestMapping("/logout")
    public String logout(String customerName) {
        Customer customer = customerRepository.findByCustomerName(customerName);
        if (customer == null) {
            return "该用户不存在";
        }
        customer.setIfLogin(0);
        customerRepository.save(customer);
        System.out.println("用户" + customerName + "已登出");
        return "redirect:/login";
    }

    @RequestMapping("/register")
//    public String register(Customer customer)
    public String register(String customerName, String password, String email) {
        Customer customer = new Customer();
        if (customerName == "" || customerName == null) {
            return "注册失败，检查用户名 密码是否合规";
        }
        if (customerRepository.findByCustomerName(customerName) == null) {
            customer.setCustomerName(customerName);
            customerRepository.save(customer);
            customer.setPassword(password);
            customer.setEmail(email);
            customer.setTakeCount(0);
            customer.setTakeDistance(0);
            customer.setMembershipLevel(1);
            customer.setMembershipPoint(0);
            customer.setCurX(25);
            customer.setCurY(25);
            customer.setIfLogin(0);
//            customer.setRequestOrder(new RequestOrder());
//            customer.setOrderList(new ArrayList<>());
//            System.out.println("\n\n\n\n\n\n\n custmoer" + customer + "----------------------------");
            Long id = customerRepository.save(customer).getId();


            RequestOrder requestOrder = new RequestOrder();
            requestOrder.setCustomer(customerRepository.findByCustomerName(customerName));
            requestOrder.setCustomerName(customerName);
            requestOrder.setStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            requestOrder.setCurX(customer.getCurX());
            requestOrder.setCurY(customer.getCurY());
            requestOrder.setIfCheck(1);
            requestOrder.setPriority(0);
            requestOrder.setDriverName("");
            requestOrderRepository.save(requestOrder);
            return customerName + "注册成功";
        } else {
            System.out.println("用户名已存在，请重新注册");
            return "用户名已存在，请重新注册";
        }
    }

    //    @RequestMapping("/Index")
//    public String customerIndx(HttpServletResponse response, String userName)
//    {
//        Customer customer = customerRepository.findByCustomerName(userName);
//        String retStr = customer.getCustomerName()
//    }
    @GetMapping("/index")//初始页面 暂时返回视图
//    @JsonView(Views.Public.class)
    public String IndexView(String username) {
        Customer customer = customerRepository.findByCustomerName(username);
        if (customer == null) {
            return "无该用户";
        }
        System.out.println(customer);
        return String.valueOf(customer);
    }

    @RequestMapping("/updateCustomer")
    public String updateCustomer(String customerName, int curX, int curY) {
        Customer customer = customerRepository.findByCustomerName(customerName);
        //获取当前位置
        if (customer == null) {
            return "无该用户";
        }
        customer.setCurX(curX);
        customer.setCurY(curY);
        //更新用户积分信息

        customer.setMembershipPoint(customer.getTakeCount() * 10 + customer.getTakeDistance());
        customer.setMembershipLevel(customer.getMembershipPoint() % 50 + 1);


        customerRepository.save(customer);
        return customerName + "位置更新成功";
    }

//    @RequestMapping("/edit")
//    public String edit(String customerName, String editName)
//    {
//        if(customerRepository.findByCustomerName(editName) == null)
//        {
//            customerRepository.findByCustomerName(customerName).setCustomerName(editName);
//            return "redirect:/Index";
//        }
//        else
//        {
//            //消息通知 用户名已存在？
//        }
//    }

    @GetMapping("/Hailing")
    public String userHailing(String customerName, int desX, int desY, int serviceLevel) {
//        boolean flag = CustomerService.CustomerHailing(username,requestOrder);
        Customer customer = customerRepository.findByCustomerName(customerName);
//        RequestOrder requestOrder =requestOrderRepository.findByCustomerName(customerName);
//        if (CustomerService.CustomerHailing(customerName, requestOrder))
        //确定区域
        if (customer == null) {
            return "无该用户";
        }
        int tempX = customer.getCurX();
        int tempY = customer.getCurY();
        int sectorId = (tempY / 3) * 17 + tempX / 3;
        List<Integer> sectorList = new ArrayList<>();
        sectorList.add(sectorId);

        if (tempX / 3 == 0) {//最左一列区域
            if (tempY / 3 == 0) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId + 18);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 16);
            } else {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId - 16);
            }
        } else if (tempX / 3 == 17) {
            if (tempY / 3 == 0) {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId + 16);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 18);
            } else {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 16);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 18);
            }
        } else {
            if (tempY / 3 == 0) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId + 16);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 18);
                sectorList.add(sectorId - 16);
            } else {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 18);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId - 16);
                sectorList.add(sectorId + 16);
            }
        }

        RequestOrder requestOrder = requestOrderRepository.findByCustomerName(customerName);
        if (requestOrder == null) {
            return "该用户订单生成失败";
        }
        //找司机
        requestOrder.setCurX(customer.getCurX());
        requestOrder.setCurY(customer.getCurY());
        requestOrder.setDesX(desX);
        requestOrder.setDesY(desY);
        requestOrder.setIfCheck(0);
        requestOrder.setPriority(requestOrder.getPriority() + 1);
        requestOrderRepository.save(requestOrder);
        int freeCnt = 0;


        for (Integer integer : sectorList) {
            List<Area> areaList = areaRepository.findBySectorId(integer);
            if (areaList == null) {
                continue;
            }
            for (Area area : areaList) {
                Driver driver = driverRepository.findById(area.getDriverId()).orElse(null);
                if (driver != null) {
                    if (driver.getIfBusy() == 0) {
                        freeCnt += 1;
                    }
                    RequestOrderForDriver requestOrderForDriver = new RequestOrderForDriver();
                    List<RequestOrderForDriver> tempOrder = new ArrayList<>();
                    requestOrderForDriver.setCustomerName(customerName);
                    requestOrderForDriver.setDriverName(driver.getDriverName());
                    requestOrderForDriver.setStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                    requestOrderForDriver.setCurX(customer.getCurX());
                    requestOrderForDriver.setCurY(customer.getCurY());
                    requestOrderForDriver.setDesX(desX);
                    requestOrderForDriver.setDesY(desY);
                    requestOrderForDriver.setIfCheck(0);
                    if (driver.getRequestOrderForDriverList() == null) {
//                        List<RequestOrder> tempOrder = new ArrayList<>();
//                        tempOrder.add(requestOrder);
//                        driver.setRequestOrderForDriverList(tempOrder);
                        tempOrder.add(requestOrderForDriver);
                        requestOrderForDriver.setDriver(driver);
                        requestOrderForDriverRepository.save(requestOrderForDriver);
                        driver.setRequestOrderForDriverList(tempOrder);
                    } else {
                        List<RequestOrderForDriver> requestOrderForDriverList = driver.getRequestOrderForDriverList();
                        int flag = 0;
                        for (RequestOrderForDriver order : requestOrderForDriverList) {
                            if (order.getCustomerName().equals(customerName)) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            requestOrderForDriver.setDriver(driver);
                            requestOrderForDriverRepository.save(requestOrderForDriver);
                            driver.getRequestOrderForDriverList().add(requestOrderForDriver);
                        }
                    }
//                    System.out.println("-----------------");
//                    System.out.println(driver);
//                    System.out.println("-----------------");
                    requestOrderForDriverRepository.save(requestOrderForDriver);
                    driverRepository.save(driver);
//                    requestOrderForDriverRepository.save(requestOrderForDriver);
                    if (freeCnt >= 5) break;
                }
            }
            if (freeCnt >= 5) break;
        }

        if (requestOrder.getPriority() >= 100) {
            List<Driver> driverList = (List<Driver>) driverRepository.findAll();
            if (driverList == null) {
                return "无司机";
            }
            for (Driver driver : driverList) {
                if (driver.getIfBusy() == 0) {
////                    tempDriver.getRequestOrderList().add(requestOrder);
//                    tempDriver.getRequestOrderForDriverList().
//                    driverRepository.save(tempDriver);
                    RequestOrderForDriver requestOrderForDriver = new RequestOrderForDriver();
                    List<RequestOrderForDriver> tempOrder = new ArrayList<>();
                    requestOrderForDriver.setCustomerName(customerName);
                    requestOrderForDriver.setDriverName(driver.getDriverName());
                    requestOrderForDriver.setStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
                    requestOrderForDriver.setCurX(customer.getCurX());
                    requestOrderForDriver.setCurY(customer.getCurY());
                    requestOrderForDriver.setDesX(desX);
                    requestOrderForDriver.setDesY(desY);
                    requestOrderForDriver.setIfCheck(0);
                    if (driver.getRequestOrderForDriverList() == null) {
//                        List<RequestOrder> tempOrder = new ArrayList<>();
//                        tempOrder.add(requestOrder);
//                        driver.setRequestOrderForDriverList(tempOrder);
                        tempOrder.add(requestOrderForDriver);
                        requestOrderForDriver.setDriver(driver);
                        requestOrderForDriverRepository.save(requestOrderForDriver);
                        driver.setRequestOrderForDriverList(tempOrder);
                    } else {
                        List<RequestOrderForDriver> requestOrderForDriverList = driver.getRequestOrderForDriverList();
                        int flag = 0;
                        for (RequestOrderForDriver order : requestOrderForDriverList) {
                            if (order.getCustomerName().equals(customerName)) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            requestOrderForDriver.setDriver(driver);
                            requestOrderForDriverRepository.save(requestOrderForDriver);
                            driver.getRequestOrderForDriverList().add(requestOrderForDriver);
                        }
//                    System.out.println("-----------------");
//                    System.out.println(driver);
//                    System.out.println("-----------------");
                        driverRepository.save(driver);
                    }
                }
                requestOrder.setPriority(0);
            }

            requestOrderRepository.save(requestOrder);
        }
        return "正在为您寻找司机，请耐心等候!!!!!";
    }

    @GetMapping("/Cancel")
    public String userCancel(String customerName) {
//        if (CustomerService.CustomerHailing(username, requestOrder))
        if (requestOrderRepository.findByCustomerName(customerName) != null) {
            //取消该订单
            if (requestOrderRepository.findByCustomerName(customerName) != null && requestOrderRepository.findByCustomerName(customerName).getIfCheck() == 1) {
                return "司机马上赶来，请等待";
            } else {
                RequestOrder requestOrder = requestOrderRepository.findByCustomerName(customerName);
                if (requestOrder == null) {
                    return "订单已取消";
                }
//                requestOrderRepository.deleteById(requestOrder.getId());
                requestOrder.setIfCheck(1);
                requestOrder.setDriverName("");
                requestOrder.setPriority(0);
//                requestOrder.setDriverName();
                requestOrderRepository.save(requestOrder);
//                requestOrderRepository
                return "取消成功";
            }
        } else {
            return "订单已取消";
        }
    }


    @RequestMapping("/finishOrder")
    public String finshOrder(String customerName, String content, int commentLevel) {
        //用户付钱 完成订单
        //更新用户信息
        Customer customer = customerRepository.findByCustomerName(customerName);
        if (customer != null) {
            Driver driver = driverRepository.findByCurCustomerName(customerName);
            if (driver != null) {
                OrderForUser order = orderForUserRepository.findById(driver.getCurOrderId()).orElse(null);
                if (order != null && order.getCustomerName().equals(customerName)) {
//        Comment comment = new Comment(content, commentLevel);
                    Comment comment = new Comment();
                    customer.setTakeCount(customer.getTakeCount() + 1);
                    customer.setTakeDistance(customer.getTakeDistance() + order.getDistance()); //TODO
                    customer.setCurX(order.getDesX());
                    customer.setCurY(order.getDesY());

                    customerRepository.save(customer);
                    //更新 订单 及评论信息

                    driver.setCurOrderId((long) -1);
                    driver.setIfBusy(0);
                    driver.setFinishCount(driver.getFinishCount() + 1);
//                    driver.setFinishDistance(driver.getFinishCount() + order.getDistance());

                    order.setCurState(1); //TODO
                    orderForUserRepository.save(order);
                    if (content == null) {
                        comment.setContent("默认五星好评");
                    } else {
                        comment.setContent(content);
                        comment.setCommentLevel(commentLevel);
                    }
                    comment.setDriver(driver);
                    driver.setStars((commentLevel + driver.getStars() * driver.getFinishCount()) / driver.getFinishCount());
                    driver.setCurCustomerName("");
                    driver.getCommentList().add(comment);
//        orderForCustomerRepository.save(order);//TODO
//        OrderForDriver tempOrder = orderForDriverRepository.findById(order.getOrderForDriver().getId()).orElse(null);
//        orderForDriverRepository.save(tempOrder);//TODO

//                    commentRepository.save(comment);
                    driverRepository.save(driver);

                    RequestOrder requestOrder = requestOrderRepository.findByCustomerName(customerName);
                    requestOrder.setDriverName("");
                    requestOrderRepository.save(requestOrder);
                    return customerName + "祝您生活愉快";
                }
            }
        }
//        Order order = orderRepository.findById(driver.getCurOrderId()).orElse(null); TODO
//        OrderForCustomer order = orderForCustomerRepository.findById(driver.getCurOrderId()).orElse(null);

        return "误操作，请查看接口是否正确，或是否已经完成操作";
    }

    @GetMapping("/searchDriver")
    public String searchDriver(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);
        if (driver == null) {
            return "司机不存在";
        }
        StringBuilder retStr = new StringBuilder("Driver:" + driver.getDriverName() + "\n");
        retStr.append("Service Level:").append(driver.getServiceLevel()).append("\n");
        retStr.append("Driver Level:").append(driver.getDriverLevel()).append("\n");
        retStr.append("Comments:\n");
        List<Comment> commentList = driver.getCommentList();
        for (int i = 0; i < commentList.size(); ++i) {
            retStr.append(i + 1).append(":").append("Stars:").append(commentList.get(i).getCommentLevel()).append(" Content:").append(commentList.get(i).getContent()).append("\n");
        }
        return retStr.toString();
    }

    @GetMapping("/searchOrder")
    public String searchOrder(String customerName) {
        Customer customer = customerRepository.findByCustomerName(customerName);
        if (customer == null) {
            return "用户不存在";
        }
        List<OrderForUser> orderList = orderForUserRepository.findByCustomerName(customerName);
        if (orderList == null) {
            return "无订单";
        }
        StringBuilder retStr = new StringBuilder();
        for (OrderForUser orderForUser : orderList) {
            retStr.append("Order:").append("Driver:").append(orderForUser.getDriverName()).append(" ")
                    .append(orderForUser.getStartTime()).append(" ").append("Distance:").append(orderForUser.getDistance()).append(" Price:").append(orderForUser.getPrice()).append("\n");
//            System.out.println("Order0:" + orderForUser);
        }
        return retStr.toString();
    }
}
