package com.example.elevenstreet.order.repository;

import com.example.elevenstreet.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
