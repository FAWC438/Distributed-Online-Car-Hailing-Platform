package cn.bupt.userserver.controller;

//import io.micrometer.core.instrument.Tag;

import cn.bupt.userserver.entity.Customer;
import cn.bupt.userserver.entity.RequestOrder;
import cn.bupt.userserver.facade.HailingFeignClient;
import cn.bupt.userserver.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

//@Tag(name = "乘车用户接口")
@RestController
@RequestMapping("/user/customer")
public class CustomerController {

    final RequestOrderRepository requestOrderRepository;
    final CustomerRepository customerRepository;
    final HailingFeignClient hailingFeignClient;


    @Autowired
    public CustomerController(RequestOrderRepository requestOrderRepository, CustomerRepository customerRepository, HailingFeignClient hailingFeignClient) {
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.hailingFeignClient = hailingFeignClient;
    }


    @GetMapping("/")
    public String index() {
        return "login";
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


    //    public String register(Customer customer)
    @RequestMapping("/register")
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
//            System.out.println("\n\n\n\n\n\n\n customer" + customer + "----------------------------");
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
    @GetMapping("/index")//初始页面 暂时返回视图
//    @JsonView(Views.Public.class)
    public String indexView(String username) {
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
    public String userHailing(String customerName, int desX, int desY, int serviceLevel) {
        return hailingFeignClient.userHailing(customerName, desX, desY, serviceLevel);
    }

    @GetMapping("/Cancel")
    public String userCancel(String username) {
        return hailingFeignClient.userCancel(username);
    }


    @RequestMapping("/finishOrder")
    public String finishOrder(String customerName, String content, int commentLevel) {
        return hailingFeignClient.finishOrder(customerName, content, commentLevel);
    }

    @GetMapping("/search")
    public String searchDriver(String driverName) {
        return hailingFeignClient.searchDriver(driverName);
    }
}
