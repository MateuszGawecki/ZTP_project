package com.example.ztp_project.repository;

import com.example.ztp_project.entity.Book;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDate;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {

    Flux<Book> findByTitle(String title);

    Flux<Book> findByAuthor(String author);

    @Query("{'published' : { $gte: ?0, $lte: ?1 } }") //without this Between is exclusive
    Flux<Book> findBookByPublishedBetween(LocalDate startDate, LocalDate endDate);
}
