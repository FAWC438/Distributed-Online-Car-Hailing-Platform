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
    final OrderForCustomerRepository orderForCustomerRepository;
    final OrderForDriverRepository orderForDriverRepository;
    final DriverRepository driverRepository;
    final RequestOrderRepository requestOrderRepository;
//    final OrderRepository orderRepository;
    final CustomerRepository customerRepository;
    final AreaRepository areaRepository;


    @Autowired
    public CustomerController(CommentRepository commentRepository, DriverRepository driverRepository, OrderForDriverRepository orderForDriverRepository, OrderForCustomerRepository orderForCustomerRepository
            , RequestOrderRepository requestOrderRepository, OrderForUserRepository orderForUserRepository,CustomerRepository customerRepository, AreaRepository areaRepository) {
        this.commentRepository = commentRepository;
        this.orderForUserRepository = orderForUserRepository;
        this.orderForCustomerRepository = orderForCustomerRepository;
        this.orderForDriverRepository = orderForDriverRepository;
        this.driverRepository = driverRepository;
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.areaRepository = areaRepository;
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
                    return "redirect:/Index";
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
        customer.setIfLogin(0);
        customerRepository.save(customer);
        System.out.println("用户" + customerName + "已登出");
        return "redirect:/login";
    }

    @RequestMapping("/register")
//    public String register(Customer customer)
    public String register(String customerName, String password, String email) {
        Customer customer = new Customer();
        if (customerRepository.findByCustomerName(customerName) == null) {
            customer.setCustomerName(customerName);
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
            return "注册成功";
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
    @GetMapping("/Index")//初始页面 暂时返回视图
//    @JsonView(Views.Public.class)
    public String IndexView(String username) {
        Customer customer = customerRepository.findByCustomerName(username);
        System.out.println(customer);
        return String.valueOf(customer);
    }

    @RequestMapping("/updateCustomer")
    public String updateCustomer(String customerName, int curX, int curY) {
        Customer customer = customerRepository.findByCustomerName(customerName);
        //获取当前位置
        customer.setCurX(curX);
        customer.setCurY(curY);
        //更新用户积分信息

        customer.setMembershipPoint(customer.getTakeCount() * 10 + customer.getTakeDistance());
        customer.setMembershipLevel(customer.getMembershipPoint() % 50 + 1);


        customerRepository.save(customer);
        return "位置更新成功";
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
    public String userHailing(String customerName,int desX, int desY,int serviceLevel) {
//        boolean flag = CustomerService.CustomerHailing(username,requestOrder);
        Customer customer = customerRepository.findByCustomerName(customerName);
//        if (CustomerService.CustomerHailing(customerName, requestOrder))
        //确定区域

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
        //找司机
        requestOrder.setCurX(customer.getCurX());
        requestOrder.setCurY(customer.getCurY());
        requestOrder.setDesX(desX);
        requestOrder.setDesY(desY);
        requestOrder.setIfCheck(0);
        requestOrder.setPriority(requestOrder.getPriority() + 1);
        int freeCnt = 0;
        for (int i = 0; i < sectorList.size(); ++i) {
            List<Area> areaList = areaRepository.findBySectorId(sectorList.get(i));
            for (int j = 0; j < areaList.size(); ++j) {
                Driver driver = driverRepository.findById((long) areaList.get(j).getDriverId()).orElse(null);
                if (driver != null) {
                    if (driver.getIfBusy() == 0) {
                        freeCnt += 1;
                    }
                    if(driver.getRequestOrderList() == null)
                    {
                        List<RequestOrder> tempOrder = new ArrayList<RequestOrder>();
                        tempOrder.add(requestOrder);
                        driver.setRequestOrderList(tempOrder);
                    }
                    else {
                        driver.getRequestOrderList().add(requestOrder);
                    }
//                    System.out.println("-----------------");
//                    System.out.println(driver);
//                    System.out.println("-----------------");
                    driverRepository.save(driver);
                    if (freeCnt >= 5) break;
                }
            }
            if (freeCnt >= 5) break;
        }

        if (requestOrder.getPriority() >= 100) {
            List<Driver> driverList = (List<Driver>) driverRepository.findAll();
            for (Driver tempDriver : driverList) {
                if (tempDriver.getIfBusy() == 0) {
                    tempDriver.getRequestOrderList().add(requestOrder);
                    driverRepository.save(tempDriver);
                }
            }
            requestOrder.setPriority(0);
        }
        requestOrderRepository.save(requestOrder);

        return "正在为您寻找司机，请耐心等候!!!!!";
    }

    @GetMapping("/Cancel")
    public String userCancel(String username, RequestOrder requestOrder) {
//        if (CustomerService.CustomerHailing(username, requestOrder))
        if (requestOrderRepository.findByCustomerName(username) == null) {
            //取消该订单
            if (requestOrderRepository.findByCustomerName(username).getIfCheck() == 1) {
                return "司机马上赶来，请等待";
            } else {
                requestOrderRepository.deleteById(requestOrder.getId());
                return "取消成功";
            }
        } else {
            return "订单已取消";
        }
    }

    // 有司机接单
//    @GetMapping("/takeOrder")
//    public String takeOrder(String username, RequestOrder requestOrder) {
//        //后端向前端发送消息推送
//        if (requestOrderRepository.findByCustomerName(username).getIfCheck() == 1) {
//            return "预测还有 " + " 公里 约 " + " 分钟到达";
//        }
//        return "请耐心等待";
//
//    }

    @RequestMapping("/finishOrder")
    public String finshOrder(String customerName, String content, int commentLevel) {
        //用户付钱 完成订单
        //更新用户信息
        Customer customer = customerRepository.findByCustomerName(customerName);
        if(customer!=null)
        {
            Driver driver = driverRepository.findByCurCustomerName(customerName);
            if(driver!=null)
            {
                OrderForUser order = orderForUserRepository.findById(driver.getCurOrderId()).orElse(null);
                if(order != null && order.getCustomerName().equals(customerName))
                {
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
                    if (content == null) {
                        comment.setContent("默认五星好评");
                    } else {
                        comment.setContent(content);
                        comment.setCommentLevel(commentLevel);
                    }
                    driver.setStars((commentLevel + driver.getStars() * driver.getFinishCount()) / driver.getFinishCount());
//        orderForCustomerRepository.save(order);//TODO
//        OrderForDriver tempOrder = orderForDriverRepository.findById(order.getOrderForDriver().getId()).orElse(null);
//        orderForDriverRepository.save(tempOrder);//TODO
                    orderForUserRepository.save(order);
                    commentRepository.save(comment);
                    driverRepository.save(driver);
                    return "祝您生活愉快";
                }
            }
        }
//        Order order = orderRepository.findById(driver.getCurOrderId()).orElse(null); TODO
//        OrderForCustomer order = orderForCustomerRepository.findById(driver.getCurOrderId()).orElse(null);

        return "误操作，请查看接口是否正确，或是否已经完成操作";
    }

    @GetMapping("/search")
    public String searchDriver(String driverName) {
        Driver driver = driverRepository.findByDriverName(driverName);
        String retStr = "Driver:" + driver.getDriverName() + "\n";
        retStr += "Service Level:" + driver.getServiceLevel() + "\n";
        retStr += "Driver Level:" + driver.getDriverLevel() + "\n";
        retStr += "Comments:\n";
        List<Comment> commentList = driver.getCommentList();
        for (int i = 0; i < commentList.size(); ++i) {
            retStr += (i + 1) + ":" + "Stars:" + commentList.get(i).getCommentLevel() + " Content:" + commentList.get(i).getContent() + "\n";
        }
        return retStr;
    }

}
