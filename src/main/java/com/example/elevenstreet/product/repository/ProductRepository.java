package com.example.elevenstreet.product.repository;

import com.example.elevenstreet.product.Product;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

public interface ProductRepository extends JpaRepository<Product, Long> {

	@Query(value = "select p from Product p where :displayDate between p.startsAt and p.endsAt")
	Page<Product> findAll(Pageable pageable, LocalDateTime displayDate);

	@Lock(value = LockModeType.PESSIMISTIC_WRITE)
	@Query("select p from Product p where p.id = :id")
	Optional<Product> findById(Long id);
}
