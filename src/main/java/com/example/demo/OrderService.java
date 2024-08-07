package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
    
    public OrderDetails getOrderByOrderId(String orderId) {
        Optional<OrderDetails> order = orderRepository.findByOrderId(orderId);
        return order.orElse(null);
    }
    
    public List<OrderDetails> getOrdersByUsername(String username) {
    	return orderRepository.findByUsername(username);
    }
    
    public void updateOrderStatus(String orderId, String status) {
        Optional<OrderDetails> order = orderRepository.findByOrderId(orderId);
        if (order.isPresent()) {
            OrderDetails updatedOrder = order.get();
            updatedOrder.setOrderStatus(status);
            orderRepository.save(updatedOrder);
        }
    }
    
}

