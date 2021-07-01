//package distributed.sys.customer.service.CustomerService;
//
//import distributed.sys.customer.dao.CustomerRepository;
//import distributed.sys.customer.dao.RequestOrderRepository;
//import distributed.sys.customer.entity.RequestOrder;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class CustomerService {
//
//    public RequestOrderRepository requestOrderRepository;
//
//    @Autowired
//    public void setRequestOrderRepository(RequestOrderRepository requestOrderRepository) {
//        this.requestOrderRepository = requestOrderRepository;
//    }
//
//    public CustomerRepository customerRepository;
//
//    @Autowired
//    public void setCustomerRepository(CustomerRepository customerRepository) {
//        this.customerRepository = customerRepository;
//    }
//
//    public static boolean CustomerHailing(String username, RequestOrder requestOrder) {
////        RequestOrderRepository requestOrderRepository;
//        if (requestOrderRepository.findByCustomerName(username) == null) {
//            System.out.println("Get a request Order from" + username);
////        System.out.print(username);
////            requestOrderRepository.save(requestOrder);
//            return true;
//        } else {
//            System.out.println(username + "is waiting");
//            return false;
//        }
//    }
//
////    public static String findDrivers(String username, RequestOrder requestOrder) {
////        List<RequestOrder> requestOrders = (List<RequestOrder>)requestOrderRepository.findAll();
////        int x = requestOrders.size();
////        return "您前面还有" + x + "位用户等待,请耐心等候";
////
////    }
//
//}
