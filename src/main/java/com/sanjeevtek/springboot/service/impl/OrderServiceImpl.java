package com.sanjeevtek.springboot.service.impl;

import com.sanjeevtek.springboot.exception.ResourceNotFoundException;
import com.sanjeevtek.springboot.model.Orders;
import com.sanjeevtek.springboot.repository.OrderRepository;
import com.sanjeevtek.springboot.service.OrderService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Orders saveOrder(Orders orders) {

        Optional<Orders> savedOrder = orderRepository.findByEmail(orders.getEmail());
        if(savedOrder.isPresent()){
            throw new ResourceNotFoundException("Stores already exist with given email:" + orders.getEmail());
        }
        return orderRepository.save(orders);
    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Orders> getOrderById(long id) {
        return orderRepository.findById(id);
    }

    @Override
    public Orders updateOrder(Orders updatedOrders) {
        return orderRepository.save(updatedOrders);
    }

    @Override
    public void deleteOrder(long id) {
        orderRepository.deleteById(id);
    }
}
