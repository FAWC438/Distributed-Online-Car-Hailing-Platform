package distributed.sys.customer.controller;

import distributed.sys.customer.dao.CustomerRepository;
import distributed.sys.customer.dao.OrderRepository;
import distributed.sys.customer.dao.RequestOrderRepository;
import distributed.sys.customer.entity.RequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/handle")
public class HandleController {

    public static RequestOrderRepository requestOrderRepository;

    @Autowired
    public void setRequestOrderRepository(RequestOrderRepository requestOrderRepository) {
        HandleController.requestOrderRepository = requestOrderRepository;
    }

    public CustomerRepository customerRepository;

    @Autowired
    public void setCustomerRepository(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public OrderRepository orderRepository;

    @Autowired
    public void setOrdersRepository(OrderRepository ordersRepository) {
        this.orderRepository = ordersRepository;
    }

    @RequestMapping("/handleRequestOrder")
    public String handleRequest()
    {
        //定时处理未完成的订单

        List<RequestOrder> requestOrderList = (List<RequestOrder>) requestOrderRepository.findAll();
        if(requestOrderList != null)
        {
            // 处理逻辑 寻找司机 找到 没找到
            // 消息推送 给司机
        }
        else
        {
            // 没有新的约车请求
        }
        return "Handle over";
    }



}
