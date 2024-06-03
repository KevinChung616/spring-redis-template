package com.jtsp.springredistemplate.repository;

import com.jtsp.springredistemplate.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}
