package cn.bupt.userhailingserver.service;

import cn.bupt.userhailingserver.entity.RequestOrder;

public interface RequestOrderService {
    RequestOrder findByCustomerName(String customerName);
}
