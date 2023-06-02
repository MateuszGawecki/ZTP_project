package com.example.ztp_project.repository;

import com.example.ztp_project.entity.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
