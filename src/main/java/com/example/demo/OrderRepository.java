package com.example.demo;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderDetails, Long> {
	
	Optional<OrderDetails> findByOrderId(String orderId);
	
	 List<OrderDetails> findByUsername(String username);
}


