package distributed.sys.customer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import distributed.sys.customer.dao.DriverRepository;
import distributed.sys.customer.entity.Driver;
import distributed.sys.customer.entity.RequestOrder;
import distributed.sys.customer.entity.Views;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/user/driver")
public class DriverController {

    public DriverRepository driverRepository;

    @Autowired
    public void setDriverRepository(DriverRepository driverRepository) {
        this.driverRepository = driverRepository;
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
                    driverRepository.findByDriverName(driverName).setIfLogin(1);
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
    public String logout(String drivername) {
        driverRepository.findByDriverName(drivername).setIfLogin(0);
        System.out.println("司机" + drivername + "已登出");
        return "redirect:/login";
    }

    @RequestMapping("/register")
    public String register(Driver driver) {
        driver.setFinishCount(0);
        driver.setFinishDistance(0);
//        driver.setServiceLevel(1);
        driver.setDriverLevel(1);
        driver.setStars(0);
        driver.setIfBusy(0);
        driver.setIfLogin(0);
        driver.setDesX(driver.getCurX());
        driver.setDesY(driver.getCurY());
        return "redirect:/login";
    }

    @RequestMapping("/edit")
    public String edit(String driverName, int serviceLevel) {
        driverRepository.findByDriverName(driverName).setServiceLevel(serviceLevel);
        return "redirect://Index";
    }

    @RequestMapping("/Index")//初始页面 暂时返回视图
    @JsonView(Views.Public.class)
    public Driver Index(String driverName) {
        return driverRepository.findByDriverName(driverName);
    }


    @RequestMapping("/updateDriver")
    public void updateDriver(String driverName) {
        //暂定市中心人最多，司机们都住在那附近，因此在空闲状态司机将会前往市中心
        //城市大小50km * 50km, 每1km有一个检测点
        Driver driver = driverRepository.findByDriverName(driverName);
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

        //更新司机积分信息
        driver.setDriverPoint(driver.getFinishCount() * 10 + driver.getFinishDistance());
        driver.setServiceLevel(driver.getDriverPoint() % 50);

    }


    @RequestMapping("/handleOrder")
    public String receiveOrder(String driverName, int orderNum) {
        //消息推送 需要实现


        if (true)//如果有订单请求
        {
            Driver driver = driverRepository.findByDriverName(driverName);
            List<RequestOrder> requestOrders = driver.getRequestOrderList();
            if (requestOrders != null && requestOrders.size() >= orderNum) {
                //假设接受orderNum 需要实现


                return "接受" + orderNum + "号订单";
            } else {
                return "无该约车订单";
            }

        } else {
            return "无约车订单";
        }
    }
}
