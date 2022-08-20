package com.example.elevenstreet.order.repository;

import com.example.elevenstreet.order.Order;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface OrderRepository extends JpaRepository<Order, Long> {

	@Query(value = "select distinct o from Order o "
		+ "inner join o.orderProducts op "
		+ "inner join op.product "
		+ "where o.userId = :userId "
		+ "and o.createdAt between :startDate and :endDate")
	Page<Order> findByUserIdAndDateCondition(String userId, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
