package cn.bupt.userserver;

import cn.bupt.userserver.configuration.RobbinConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.cloud.netflix.ribbon.RibbonClients;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
@EnableDiscoveryClient
//@LoadBalancerClients({@LoadBalancerClient(name = "provider", configuration = CloudProviderConfiguration.class)})
@RibbonClients({@RibbonClient(name = "User-Hailing-Server", configuration = RobbinConfig.class)})
public class UserServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserServerApplication.class, args);
    }

}
