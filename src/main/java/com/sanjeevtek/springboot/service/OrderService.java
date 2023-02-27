package com.sanjeevtek.springboot.service;

import com.sanjeevtek.springboot.model.Orders;
import com.sanjeevtek.springboot.model.Stores;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    Orders saveOrder(Orders orders);
    List<Orders> getAllOrders();
    Optional<Orders> getOrderById(long id);
    Orders updateOrder(Orders updatedOrders);
    void deleteOrder(long id);
}
