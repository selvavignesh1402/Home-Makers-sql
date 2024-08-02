package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    private OrderRepository orderRepository;

    public OrderDetails saveOrder(OrderDetails order) {
        return orderRepository.save(order);
    }

    public List<OrderDetails> getAllOrders() {
        return orderRepository.findAll();
    }
}

