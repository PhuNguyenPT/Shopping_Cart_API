package com.example.shopping_cart.file;

import com.example.shopping_cart.product.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FileRepository extends JpaRepository<File, Integer> {
    List<File> findByNameContainingIgnoreCase(String name);
    Long deleteByName(String name);

    List<File> findByProduct(Product product);

    File findById(Long id);
}
