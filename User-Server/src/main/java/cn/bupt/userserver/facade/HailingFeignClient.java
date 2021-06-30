package cn.bupt.userserver.facade;

import cn.bupt.userserver.entity.RequestOrder;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

//@FeignClient(name = "User-Hailing-Server")
@FeignClient(name = "User-Hailing-Server", url = "127.0.0.1:5010")
public interface HailingFeignClient {
    @GetMapping("/Hailing")
    String userHailing(@RequestParam("customerName") String customerName, @RequestParam("desX") int desX,
                       @RequestParam("desY") int desY, @RequestParam("serviceLevel") int serviceLevel);

    @GetMapping("/Cancel")
    String userCancel(String username);

    @RequestMapping("/finishOrder")
    String finishOrder(@RequestParam("customerName") String customerName, @RequestParam("content") String content,
                       @RequestParam("commentLevel") int commentLevel);

    @GetMapping("/search")
    String searchDriver(String driverName);
}
