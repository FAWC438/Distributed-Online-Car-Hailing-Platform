package cn.bupt.driverhailingserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableCaching
public class DriverHailingServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(DriverHailingServerApplication.class, args);
    }

}
