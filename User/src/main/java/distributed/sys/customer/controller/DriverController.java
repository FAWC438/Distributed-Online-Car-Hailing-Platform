package distributed.sys.customer.controller;

import distributed.sys.customer.dao.DriverRepository;
import distributed.sys.customer.entity.Driver;
import distributed.sys.customer.entity.RequestOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/user/driver")
public class DriverController {

    public DriverRepository driverRepository;

    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
    }


    @RequestMapping("/updateDriver")
    public void updateDriver(String driverName)
    {
        //暂定市中心人最多，司机们都住在那附近，因此在空闲状态司机将会前往市中心
        //城市大小50km * 50km, 每1km有一个检测点
        Driver driver = driverRepository.findByDriverName(driverName);
        int  curX = driver.getCurX();
        int curY = driver.getCurY();
        double difX = Math.abs(curX- driver.getDesX());
        double difY = Math.abs(curY - driver.getDesY());
        if(difX == 0)
        {
            if(difY == 0)
            {
                //已到达 更新各种信息 busy free 订单状况 消息推送 等

            }
            else// difx = 0 dify != 0 y方向走
            {
                curY += (driver.getDesY() - curY)/difY;
            }
        }
        else
        {
            if(difY == 0)// difx!=0 dify=0 往x方向走
            {
                curX += (driver.getDesX() - curX)/difX;
            }
            else // 往短的一方走
            {
                if(difX <= difY)
                {
                    curX += (driver.getDesX() - curX)/difX;
                }
                else
                {
                    curY += (driver.getDesY() - curY)/difY;
                }
            }
        }
        driver.setCurX(curX);
        driver.setCurY(curY);
    }


    @RequestMapping("/handleOrder")
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
