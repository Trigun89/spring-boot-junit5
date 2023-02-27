package com.sanjeevtek.springboot.controller;

import com.sanjeevtek.springboot.model.Orders;
import com.sanjeevtek.springboot.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Orders createOrder(@RequestBody Orders orders){
        return orderService.saveOrder(orders);
    }

    @GetMapping
    public List<Orders> getAllOrders(){
        return orderService.getAllOrders();
    }

    @GetMapping("{id}")
    public ResponseEntity<Orders> getOrderById(@PathVariable("id") long OrderId){
        return orderService.getOrderById(OrderId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("{id}")
    public ResponseEntity<Orders> updateOrder(@PathVariable("id") long orderId,
                                                 @RequestBody Orders orders){
        return orderService.getOrderById(orderId)
                .map(savedOrder -> {

                    savedOrder.setFirstName(orders.getFirstName());
                    savedOrder.setLastName(orders.getLastName());
                    savedOrder.setEmail(orders.getEmail());

                    Orders updatedOrders = orderService.updateOrder(savedOrder);
                    return new ResponseEntity<>(updatedOrders, HttpStatus.OK);

                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteOrder(@PathVariable("id") long orderId){

        orderService.deleteOrder(orderId);

        return new ResponseEntity<String>("Orders deleted successfully!.", HttpStatus.OK);

    }

}
