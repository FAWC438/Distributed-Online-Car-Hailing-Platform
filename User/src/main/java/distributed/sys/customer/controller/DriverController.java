package distributed.sys.customer.controller;

import distributed.sys.customer.dao.DriverRepository;
import distributed.sys.customer.entity.Driver;
import distributed.sys.customer.entity.RequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

public class DriverController {

    public DriverRepository driverRepository;

    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }


    @RequestMapping("/receiveOrder")
    public String receiveOrder(String driverName, int orderNum) {
        //消息推送 需要实现

        if (true)//如果有订单请求
        {
            Driver driver = driverRepository.findByDriverName(driverName);
            List<RequestOrder> requestOrders = driver.getRequestOrderList();
            if (requestOrders != null && requestOrders.size() >= orderNum)
            {
                //假设接受orderNum 需要实现


                return "接受" + orderNum + "号订单";
            }
            else {
                return "无该约车订单";
            }

        } else {
            return "无约车订单";
        }
    }
}
