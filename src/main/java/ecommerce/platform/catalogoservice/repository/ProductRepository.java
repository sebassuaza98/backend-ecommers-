package ecommerce.platform.catalogoservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ecommerce.platform.catalogoservice.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
}