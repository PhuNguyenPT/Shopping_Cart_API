package com.example.shopping_cart.repositories;

import com.example.shopping_cart.models.Video;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VideoRepository extends JpaRepository<Video, Integer> {
}
