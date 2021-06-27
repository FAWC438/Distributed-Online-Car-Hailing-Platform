package distributed.sys.customer.controller;

//import io.micrometer.core.instrument.Tag;

import com.fasterxml.jackson.annotation.JsonView;
import distributed.sys.customer.dao.CustomerRepository;
import distributed.sys.customer.dao.DriverRepository;
import distributed.sys.customer.dao.RequestOrderRepository;
import distributed.sys.customer.entity.*;
import distributed.sys.customer.service.CustomerService.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

//@Tag(name = "乘车用户接口")
@RestController
@RequestMapping("/user/customer")
public class CustomerController {
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
            return "redirect:/";
        }
        //不正确的用户名密码
        if (!customerName.equals("test") || !password.equals("123456")) {
            //登录失败设置标志位null 因为没有登出
            request.getSession().setAttribute("loginName", null);
            return "redirect:/";
        }

        request.getSession().setAttribute("loginName", "test");
        return "redirect:/addrList";
    }

    @RequestMapping("/resgister")
    public String register(Customer customer) {
        customerRepository.save(customer);
        return "redirect:/Index";
    }

    //    @RequestMapping("/Index")
//    public String customerIndx(HttpServletResponse response, String userName)
//    {
//        Customer customer = customerRepository.findByCustomerName(userName);
//        String retStr = customer.getCustomerName()
//    }
    @GetMapping("/Index")
    @JsonView(Views.Public.class)
    public Customer IndexView(String username) {
        return customerRepository.findByCustomerName(username);
    }

    @GetMapping("/Hailing")
    public String userHailing(String username, RequestOrder requestOrder) {
//        boolean flag = CustomerService.CustomerHailing(username,requestOrder);
        if (CustomerService.CustomerHailing(username, requestOrder)) {
            List<RequestOrder> requestOrders = (List<RequestOrder>) requestOrderRepository.findAll();
            int x = requestOrders.size();
            // 消息推送需实现



            return "您前面还有" + x + "位用户等待,请耐心等候";
//            return CustomerService.findDrivers(username,requestOrder);
        } else {
            return "您有正在请求的订单，请等待该订单处理完成";
        }
    }

    @GetMapping("/Cancel")
    public String userCancel(String username, RequestOrder requestOrder) {
        if (CustomerService.CustomerHailing(username, requestOrder)) {
            //取消该订单
            return "取消成功";
        } else {
            return "订单已取消";
        }
    }

    // 有司机接单
    @GetMapping("/takeOrder")
    public String takeOrder(String username, RequestOrder requestOrder) {

        //消息推送 需要实现
        return "预测还有 " + " 公里 约 " + " 分钟到达";
    }

    @GetMapping("/finishOrder")
    public String finshOrder(String username, String content, int commentLevel)
    {
        //找到订单所对应司机， 进行评价 默认 无评价 评价五星
        return "祝您生活愉快";
    }

    @GetMapping("/search")
    public String searchDriver(String driverName)
    {
        Driver driver = driverRepository.findByDriverName(driverName);
        String retStr = "Driver:" + driver.getDriverName() + "\n";
        retStr += "Service Level:" + driver.getServiceLevel() + "\n";
        retStr += "Driver Level:" + driver.getDriverLevel() + "\n";
        retStr += "Comments:\n";
        List<Comment> commentList = driver.getCommentList();
        for(int i = 0; i < commentList.size();++i)
        {
            retStr += (i +1)+":"+"Stars:"+commentList.get(i).getCommentLevel() + " Content:" +commentList.get(i).getContent() + "\n";
        }
        return retStr;
    }

}
