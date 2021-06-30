package cn.bupt.driverserver.facade;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "Driver-Hailing-Server", url = "127.0.0.1:5030")
public interface HailingFeignClient {
    @RequestMapping("/updateDriver")
    String updateDriver(@RequestParam("driverName") String driverName);

    @RequestMapping("/handleRequestOrder")
    String handleRequestOrder(@RequestParam("driverName") String driverName, @RequestParam("orderNum") int orderNum);

    @RequestMapping("/takeCustomer")
    String takeCustomer(@RequestParam("driverName") String driverName);

    @RequestMapping("/finishOrder")
    String finishOrder(@RequestParam("driverName") String driverName);

    @GetMapping("/searchOrder")
    String searchOrder(String driverName);
}
