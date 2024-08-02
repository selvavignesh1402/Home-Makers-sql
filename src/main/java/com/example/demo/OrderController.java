package com.example.demo;

import java.util.List;

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
}

