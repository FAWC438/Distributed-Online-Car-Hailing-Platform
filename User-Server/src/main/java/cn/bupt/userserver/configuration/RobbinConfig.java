package cn.bupt.userserver.configuration;

import com.netflix.loadbalancer.BestAvailableRule;
import com.netflix.loadbalancer.IRule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RobbinConfig {
    @Bean
    public IRule muRule() {
        // https://www.jianshu.com/p/d4e4de2ad3aa
        // return new WeightedResponseTimeRule();   // 根据响应时间，分配一个权重weight，响应时间越长，weight越小，被选中的可能性越低。
        // return new RoundRobinRule();    // 轮询策略
        // return new RandomRule(); // 使用jdk自带的随机数生成工具，生成一个随机数，然后去可用服务列表中拉取服务节点Server。如果当前节点不可用，则进入下一轮随机策略，直到选到可用服务节点为止。
        // return new AvailabilityFilteringRule(); // 过滤掉连接失败的服务节点，并且过滤掉高并发的服务节点，然后从健康的服务节点中，使用轮询策略选出一个节点返回。
        // return new RetryRule(); // 首先使用轮询策略进行负载均衡，如果轮询失败，则再使用轮询策略进行一次重试，相当于重试下一个节点，看下一个节点是否可用，如果再失败，则直接返回失败。
        return new BestAvailableRule(); // 选择一个并发量最小的server返回。
        // 如何判断并发量最小呢？ServerStats有个属性activeRequestCount，这个属性记录的就是server的并发量。
        // 轮询所有的server，选择其中activeRequestCount最小的那个server，就是并发量最小的服务节点。
        // return new ZoneAvoidanceRule(); // 复合判断server所在区域的性能和server的可用性，来选择server返回。
    }
}
