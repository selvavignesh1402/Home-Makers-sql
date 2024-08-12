package com.example.demo;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/orders")
@CrossOrigin(origins = "http://localhost:3000") 
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping("/order")
    public OrderDetails placeOrder(@RequestBody OrderDetails order) {
    	return orderService.saveOrder(order);
    }

    @GetMapping
    public List<OrderDetails> getAllOrders() {
        return orderService.getAllOrders();
    }
    
    @GetMapping("/{orderId}")
    public OrderDetails getOrderByOrderId(@PathVariable String orderId) {
        return orderService.getOrderByOrderId(orderId);
    }
    
    @GetMapping("/user/{username}") 
    public List<OrderDetails> getOrdersByUsername(@PathVariable String username) {
        return orderService.getOrdersByUsername(username);
    }
    
    @PutMapping("/{orderId}/status")
    public void updateOrderStatus(@PathVariable String orderId, @RequestBody Map<String, String> request) {
        String status = request.get("status");
        orderService.updateOrderStatus(orderId, status);
    }
    
}

