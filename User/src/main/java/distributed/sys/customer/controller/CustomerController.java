package distributed.sys.customer.controller;

//import io.micrometer.core.instrument.Tag;

import com.fasterxml.jackson.annotation.JsonView;
import distributed.sys.customer.dao.*;
import distributed.sys.customer.entity.*;
import distributed.sys.customer.service.CustomerService.CustomerService;
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
    public CommentRepository commentRepository;

    @Autowired
    public void setCommentRepository(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

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

    @RequestMapping("/resgister")
    public String register(Customer customer) {
        if (customerRepository.findByCustomerName(customer.getCustomerName()) == null) {
            customer.setTakeCount(0);
            customer.setTakeDistance(0);
            customer.setMembershipLevel(1);
            customer.setMembershipPoint(0);
            customer.setCurX(-1);
            customer.setCurX(-1);
            customer.setIfLogin(0);
            customer.setRequestOrder(new RequestOrder());
            customer.setOrderList(new ArrayList<>());
            customerRepository.save(customer);
            return "redirect:/Index";
        } else {
            System.out.println("用户名已存在，请重新注册");
            return "redirect:/register";
        }
    }

    //    @RequestMapping("/Index")
//    public String customerIndx(HttpServletResponse response, String userName)
//    {
//        Customer customer = customerRepository.findByCustomerName(userName);
//        String retStr = customer.getCustomerName()
//    }
    @GetMapping("/Index")//初始页面 暂时返回视图
    @JsonView(Views.Public.class)
    public Customer IndexView(String username) {
        return customerRepository.findByCustomerName(username);
    }

    @RequestMapping("/updateCustomer")
    public void updateCustomer(String customerName, int curX, int curY) {
        Customer customer = customerRepository.findByCustomerName(customerName);
        //获取当前位置
        customer.setCurX(curX);
        customer.setCurY(curY);
        //更新用户积分信息

        customer.setMembershipPoint(customer.getTakeCount() * 10 + customer.getTakeDistance());
        customer.setMembershipLevel(customer.getMembershipPoint() % 50 + 1);


        customerRepository.save(customer);
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
    public String userHailing(String customerName, RequestOrder requestOrder) {
//        boolean flag = CustomerService.CustomerHailing(username,requestOrder);
        Customer customer = customerRepository.findByCustomerName(customerName);
        if (CustomerService.CustomerHailing(customerName, requestOrder)) {
            List<RequestOrder> requestOrderList = (List<RequestOrder>) requestOrderRepository.findAll();
            int x = requestOrderList.size();
            requestOrder.setCustomer(customerRepository.findByCustomerName(customerName));
            requestOrder.setCustomerName(customerName);
            requestOrder.setStartTime(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").format(LocalDateTime.now()));
            requestOrder.setCurX(customer.getCurX());
            requestOrder.setCurY(customer.getCurY());
            requestOrder.setIfCheck(0);
            requestOrder.setPriority(0);
            requestOrder.setDriverName(null);
            requestOrder.setDriver(null);
            // 目的地 服务等级 由 用户在前端通过参数发送
//            requestOrderList.add(requestOrder);


            // 找司机
            int flag = 0;
            List<Driver> driverList = (List<Driver>) driverRepository.findAll();
            for (int i = 0; i < driverList.size(); ++i) {
                Driver tempDriver = driverList.get(i);
                if (tempDriver.getIfBusy() == 0) {
                    int difx = Math.abs(tempDriver.getCurX() - customer.getCurX());
                    int dify = Math.abs(tempDriver.getCurY() - customer.getCurY());
                    if (difx + dify <= 5) {
                        tempDriver.getRequestOrderList().add(requestOrder);
                        driverRepository.save(tempDriver);
                        flag = 1;
                    }
                }
            }
            if (flag == 0)//没在合适区域找到司机
            {
                requestOrder.setPriority(1);
            }

            requestOrderRepository.save(requestOrder);
            return "正在为您寻找司机，请耐心等候";
//            return CustomerService.findDrivers(username,requestOrder);
        } else {
            return "您有正在请求的订单，请等待该订单处理完成";
        }
    }

    @GetMapping("/Cancel")
    public String userCancel(String username, RequestOrder requestOrder) {
        if (CustomerService.CustomerHailing(username, requestOrder)) {
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
    @GetMapping("/takeOrder")
    public String takeOrder(String username, RequestOrder requestOrder) {
        //后端向前端发送消息推送
        if (requestOrderRepository.findByCustomerName(username).getIfCheck() == 1) {
            return "预测还有 " + " 公里 约 " + " 分钟到达";
        }
        return "请耐心等待";

    }

    @RequestMapping("/finishOrder")
    public String finshOrder(String username, String content, int commentLevel) {
        //用户付钱 完成订单
        //更新用户信息
        Customer customer = customerRepository.findByCustomerName(username);
        Driver driver = driverRepository.findByCurCustomerName(username);
        Order order = driver.getCurOrder();
        Comment comment = new Comment(content, commentLevel);
        customer.setTakeCount(customer.getTakeCount() + 1);
        customer.setTakeDistance(customer.getTakeDistance() + order.getDistance());
        customerRepository.save(customer);

        //更新 订单 及评论信息
        order.setCurState(1);
        if (content == null) {
            comment.setContent("默认五星好评");
        }

        driver.setStars((commentLevel + driver.getStars() * driver.getFinishCount()) / driver.getFinishCount() + 1);

        orderRepository.save(order);
        commentRepository.save(comment);
        driverRepository.save(driver);

        return "祝您生活愉快";
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
