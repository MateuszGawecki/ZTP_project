package com.example.ztp_project.repository;

import com.example.ztp_project.entity.Cart;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CartRepository extends ReactiveMongoRepository<Cart, String> {
}
