package cn.bupt.driverserver.controller;

import cn.bupt.driverserver.entity.Area;
import cn.bupt.driverserver.entity.Driver;
import cn.bupt.driverserver.entity.Views;
import cn.bupt.driverserver.facade.HailingFeignClient;
import cn.bupt.driverserver.repository.AreaRepository;
import cn.bupt.driverserver.repository.DriverRepository;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;


@RefreshScope
@RestController
public class DriverController {

    final DriverRepository driverRepository;
    final AreaRepository areaRepository;
    final HailingFeignClient hailingFeignClient;


    @Autowired
    public DriverController(DriverRepository driverRepository, AreaRepository areaRepository, HailingFeignClient hailingFeignClient) {
        this.driverRepository = driverRepository;
        this.areaRepository = areaRepository;
        this.hailingFeignClient = hailingFeignClient;
    }

    @GetMapping("/")
    @SentinelResource
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
    @SentinelResource
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
                    return "登陆成功";
                } else {
                    return "密码错误";
                }
            } else {
                return "用户不存在";
            }
        }
    }

    @RequestMapping("/logout")
    @SentinelResource
    public String logout(String driverName) {// TODO：更改代码
        Driver driver = driverRepository.findByDriverName(driverName);
        if (driver == null) {
            return "该用户不存在";
        }
        driver.setIfLogin(0);
        System.out.println("司机" + driverName + "已登出");
        return "登出成功";
    }

    @RequestMapping("/register")
    @SentinelResource
    public String register(String driverName, String password) {    //TODO:改了接口参数
        if (driverRepository.findByDriverName(driverName) == null) {
            Driver driver = new Driver();
            driver.setDriverName(driverName);
            driver.setPassword(password);
            driver.setFinishCount(0);
            driver.setFinishDistance(0);
//        driver.setServiceLevel(1);
            driver.setDriverLevel(1);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setIfLogin(0);
            driver.setStars(0);
            driver.setIfBusy(0);
            driver.setCurX(25);
            driver.setCurY(25);
            driver.setDesX(driver.getCurX());
            driver.setDesY(driver.getCurY());

//            driver.setOrderList(new ArrayList<>());
//            driver.setCurOrder(new Order());
//            driver.setCommentList(new ArrayList<>());
//            driver.setRequestOrderList(new ArrayList<>());
            driver.setCurCustomerName("");
            driverRepository.save(driver);
            Area area = new Area();
            area.setDriverId(driverRepository.findByDriverName(driver.getDriverName()).getId());
            area.setSectorId((driver.getCurY() / 3) * 17 + driver.getCurX() / 3);
            areaRepository.save(area);
            return "注册成功";
        } else {
            System.out.println("司机用户名已存在");
            return "司机用户名已存在请重新注册";
        }

    }

    @RequestMapping("/edit")
    @SentinelResource
    public String edit(String driverName, int serviceLevel) {//TODO 更新代码
        Driver driver = driverRepository.findByDriverName(driverName);
        if (driver == null) {
            return "该司机不存在";
        }
        driver.setServiceLevel(serviceLevel);
        driverRepository.save(driver);
        return "编辑成功";
    }

    @RequestMapping("/index")//初始页面 暂时返回视图
    @SentinelResource
    @JsonView(Views.Public.class)
    public Driver index(String driverName) {
        return driverRepository.findByDriverName(driverName);
    }


    @RequestMapping("/updateDriver")
    @SentinelResource
    public String updateDriver(String driverName) {
        return hailingFeignClient.updateDriver(driverName);
    }


    @RequestMapping("/handleRequestOrder")
    @SentinelResource
    public String handleRequestOrder(String driverName, int orderNum) {
        return hailingFeignClient.handleRequestOrder(driverName, orderNum);
    }

    @RequestMapping("/takeCustomer")
    @SentinelResource
    public String takeCustomer(String driverName) {
        return hailingFeignClient.takeCustomer(driverName);
    }

    @RequestMapping("/finishOrder")
    @SentinelResource
    public String finishOrder(String driverName) {
        return hailingFeignClient.finishOrder(driverName);
    }

    @GetMapping("/searchOrder")
    @SentinelResource
    public String searchOrder(String driverName) {
        return hailingFeignClient.searchOrder(driverName);
    }
}

