package cn.bupt.eurekaconsumer.controller;

import cn.bupt.eurekaconsumer.MyFeignClient;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    @Value("${eureka.instance.instance-id}")
    private String uniqueId;

    final MyFeignClient myFeignClient;

    @Autowired
    public ConsumerController(MyFeignClient myFeignClient) {
        this.myFeignClient = myFeignClient;
    }

    @RequestMapping("/hello")
    @SentinelResource
    public String helloFunction() {
        return myFeignClient.hello();
    }

    @RequestMapping("/config")
    @SentinelResource
    public String configFunction() {
        return uniqueId;
    }
}