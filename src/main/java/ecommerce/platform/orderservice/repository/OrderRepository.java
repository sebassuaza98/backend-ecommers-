package ecommerce.platform.orderservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.platform.orderservice.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}