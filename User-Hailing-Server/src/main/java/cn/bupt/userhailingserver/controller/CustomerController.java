package cn.bupt.userhailingserver.controller;

//import io.micrometer.core.instrument.Tag;

import cn.bupt.userhailingserver.entity.*;
import cn.bupt.userhailingserver.repository.*;
import cn.bupt.userhailingserver.service.*;
import com.alibaba.csp.sentinel.annotation.SentinelResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//@Tag(name = "乘车用户接口")
//@RequestMapping("/user/customer")
@RefreshScope
@RestController
public class CustomerController {
    final CommentRepository commentRepository;
    final OrderForUserRepository orderForUserRepository;
    final DriverRepository driverRepository;
    final RequestOrderRepository requestOrderRepository;
    final CustomerRepository customerRepository;
    final AreaRepository areaRepository;

    final AreaService areaService;
    final CustomerService customerService;
    final DriverService driverService;
    final OrderForUserService orderForUserService;
    final RequestOrderService requestOrderService;

    @Autowired
    public CustomerController(CommentRepository commentRepository, DriverRepository driverRepository, RequestOrderRepository requestOrderRepository,
                              OrderForUserRepository orderForUserRepository, CustomerRepository customerRepository, AreaRepository areaRepository,
                              AreaService areaService, CustomerService customerService, DriverService driverService, OrderForUserService orderForUserService,
                              RequestOrderService requestOrderService) {
        this.commentRepository = commentRepository;
        this.orderForUserRepository = orderForUserRepository;
        this.driverRepository = driverRepository;
        this.requestOrderRepository = requestOrderRepository;
        this.customerRepository = customerRepository;
        this.areaRepository = areaRepository;

        this.areaService = areaService;
        this.customerService = customerService;
        this.driverService = driverService;
        this.orderForUserService = orderForUserService;
        this.requestOrderService = requestOrderService;
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

    @GetMapping("/Hailing")
    @SentinelResource
    public String userHailing(String customerName, int desX, int desY) {
//        boolean flag = CustomerService.CustomerHailing(username,requestOrder);
        Customer customer = customerService.findByCustomerName(customerName);
//        if (CustomerService.CustomerHailing(customerName, requestOrder))
        //确定区域

        int tempX = customer.getCurX();
        int tempY = customer.getCurY();
        int sectorId = (tempY / 3) * 17 + tempX / 3;
        List<Integer> sectorList = new ArrayList<>();
        sectorList.add(sectorId);

        if (tempX / 3 == 0) {//最左一列区域
            if (tempY / 3 == 0) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId + 18);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 16);
            } else {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId - 16);
            }
        } else if (tempX / 3 == 17) {
            if (tempY / 3 == 0) {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId + 16);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 18);
            } else {
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 16);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 18);
            }
        } else {
            if (tempY / 3 == 0) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId + 16);
            } else if (tempY / 3 == 17) {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 18);
                sectorList.add(sectorId - 16);
            } else {
                sectorList.add(sectorId + 1);
                sectorList.add(sectorId - 17);
                sectorList.add(sectorId + 17);
                sectorList.add(sectorId - 1);
                sectorList.add(sectorId - 18);
                sectorList.add(sectorId + 18);
                sectorList.add(sectorId - 16);
                sectorList.add(sectorId + 16);
            }
        }

        RequestOrder requestOrder = requestOrderService.findByCustomerName(customerName);
        //找司机
        requestOrder.setCurX(customer.getCurX());
        requestOrder.setCurY(customer.getCurY());
        requestOrder.setDesX(desX);
        requestOrder.setDesY(desY);
        requestOrder.setIfCheck(0);
        requestOrder.setPriority(requestOrder.getPriority() + 1);
        int freeCnt = 0;
//        for (Integer integer : sectorList) {
//            List<Area> areaList = areaRepository.findBySectorId(integer);
//            for (Area area : areaList) {
//                Driver driver = driverRepository.findById(area.getDriverId()).orElse(null);
//                if (driver != null) {
//                    if (driver.getIfBusy() == 0) {
//                        freeCnt += 1;
//                    }
//                    if (driver.getRequestOrderList() == null) {
//                        List<RequestOrder> tempOrder = new ArrayList<>();
//                        tempOrder.add(requestOrder);
//                        driver.setRequestOrderList(tempOrder);
//                    } else {
//                        driver.getRequestOrderList().add(requestOrder);
//                    }
////                    System.out.println("-----------------");
////                    System.out.println(driver);
////                    System.out.println("-----------------");
//                    driverRepository.save(driver);
//                    if (freeCnt >= 5) break;
//                }
//            }
//            if (freeCnt >= 5) break;
//        }
        for (Integer integer : sectorList) {
            List<Area> areaList = areaService.findBySectorId(integer);
            for (Area area : areaList) {
                Driver driver = driverRepository.findById(area.getDriverId()).orElse(null);
                if (driver != null) {
                    if (driver.getIfBusy() == 0) {
                        freeCnt += 1;
                    }
                    if (driver.getRequestOrderList() == null) {
                        List<RequestOrder> tempOrder = new ArrayList<>();
                        tempOrder.add(requestOrder);
                        driver.setRequestOrderList(tempOrder);
                    } else {
                        List<RequestOrder> requestOrderList = driver.getRequestOrderList();
                        int flag = 0;
                        for (RequestOrder order : requestOrderList) {
                            if (order.getCustomerName().equals(customerName)) {
                                flag = 1;
                                break;
                            }
                        }
                        if (flag == 0) {
                            driver.getRequestOrderList().add(requestOrder);
                        }
                    }
//                    System.out.println("-----------------");
//                    System.out.println(driver);
//                    System.out.println("-----------------");
                    driverRepository.save(driver);
                    if (freeCnt >= 5) break;
                }
            }
            if (freeCnt >= 5) break;
        }


        if (requestOrder.getPriority() >= 100) {
            List<Driver> driverList = (List<Driver>) driverRepository.findAll();
            for (Driver tempDriver : driverList) {
                if (tempDriver.getIfBusy() == 0) {
                    tempDriver.getRequestOrderList().add(requestOrder);
                    driverRepository.save(tempDriver);
                }
            }
            requestOrder.setPriority(0);
        }
        requestOrderRepository.save(requestOrder);

        return "正在为您寻找司机，请耐心等候!!!!!";
    }

    @GetMapping("/Cancel")
    @SentinelResource
    public String userCancel(String customerName) {
//        if (CustomerService.CustomerHailing(username, requestOrder))
        if (requestOrderService.findByCustomerName(customerName) == null) {
            //取消该订单
            if (requestOrderService.findByCustomerName(customerName).getIfCheck() == 1) {
                return "司机马上赶来，请等待";
            } else {
                RequestOrder requestOrder = requestOrderService.findByCustomerName(customerName);
//                requestOrderRepository.deleteById(requestOrder.getId());
                requestOrder.setIfCheck(1);
                requestOrder.setPriority(0);
//                requestOrder.setDriverName();
                requestOrderRepository.save(requestOrder);
//                requestOrderRepository
                return "取消成功";
            }
        } else {
            return "订单已取消";
        }
    }

    // 有司机接单
//    @GetMapping("/takeOrder")
//    public String takeOrder(String username, RequestOrder requestOrder) {
//        //后端向前端发送消息推送
//        if (requestOrderRepository.findByCustomerName(username).getIfCheck() == 1) {
//            return "预测还有 " + " 公里 约 " + " 分钟到达";
//        }
//        return "请耐心等待";
//
//    }

    @RequestMapping("/finishOrder")
    @SentinelResource
    public String finishOrder(String customerName, String content, int commentLevel) {
        //用户付钱 完成订单
        //更新用户信息
        Customer customer = customerService.findByCustomerName(customerName);
        if (customer != null) {
            Driver driver = driverService.findByCurCustomerName(customerName);
            if (driver != null) {
                OrderForUser order = orderForUserRepository.findById(driver.getCurOrderId()).orElse(null);
                if (order != null && order.getCustomerName().equals(customerName)) {
//        Comment comment = new Comment(content, commentLevel);
                    Comment comment = new Comment();
                    customer.setTakeCount(customer.getTakeCount() + 1);
                    customer.setTakeDistance(customer.getTakeDistance() + order.getDistance()); //TODO
                    customer.setCurX(order.getDesX());
                    customer.setCurY(order.getDesY());

                    customerRepository.save(customer);
                    //更新 订单 及评论信息

                    driver.setCurOrderId((long) -1);
                    driver.setIfBusy(0);
                    driver.setFinishCount(driver.getFinishCount() + 1);
//                    driver.setFinishDistance(driver.getFinishCount() + order.getDistance());

                    order.setCurState(1); //TODO
                    if (content == null) {
                        comment.setContent("默认五星好评");
                    } else {
                        comment.setContent(content);
                        comment.setCommentLevel(commentLevel);
                    }
                    driver.setStars((commentLevel + driver.getStars() * driver.getFinishCount()) / driver.getFinishCount());
//        orderForCustomerRepository.save(order);//TODO
//        OrderForDriver tempOrder = orderForDriverRepository.findById(order.getOrderForDriver().getId()).orElse(null);
//        orderForDriverRepository.save(tempOrder);//TODO
                    orderForUserRepository.save(order);
                    commentRepository.save(comment);
                    driverRepository.save(driver);
                    return "祝您生活愉快";
                }
            }
        }
//        Order order = orderRepository.findById(driver.getCurOrderId()).orElse(null); TODO
//        OrderForCustomer order = orderForCustomerRepository.findById(driver.getCurOrderId()).orElse(null);

        return "误操作，请查看接口是否正确，或是否已经完成操作";
    }

    @GetMapping("/searchDriver")
    @SentinelResource
    public String searchDriver(String driverName) {
        Driver driver = driverService.findByDriverName(driverName);
        StringBuilder retStr = new StringBuilder("Driver:" + driver.getDriverName() + "\n");
        retStr.append("Service Level:").append(driver.getServiceLevel()).append("\n");
        retStr.append("Driver Level:").append(driver.getDriverLevel()).append("\n");
        retStr.append("Comments:\n");
        List<Comment> commentList = driver.getCommentList();
        for (int i = 0; i < commentList.size(); ++i) {
            retStr.append(i + 1).append(":").append("Stars:").append(commentList.get(i).getCommentLevel()).append(" Content:").append(commentList.get(i).getContent()).append("\n");
        }
        return retStr.toString();
    }


    @GetMapping("/searchOrder")
    @SentinelResource
    public String searchOrder(String customerName) {
        Customer customer = customerService.findByCustomerName(customerName);
        List<OrderForUser> orderList = orderForUserService.findByCustomerName(customerName);
        StringBuilder retStr = new StringBuilder();
        for (OrderForUser orderForUser : orderList) {
            retStr.append("Order0:").append(orderForUser.toString()).append("\n");
            System.out.println("Order0:" + orderForUser);
        }
        return retStr.toString();
    }
}