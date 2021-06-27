package cn.bupt.eurekaconsumer.controller;

import cn.bupt.eurekaconsumer.MyFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    final MyFeignClient myFeignClient;

    @Autowired
    public ConsumerController(MyFeignClient myFeignClient) {
        this.myFeignClient = myFeignClient;
    }

    @RequestMapping("/hello")
    public String index() {
        return myFeignClient.hello();
    }
}