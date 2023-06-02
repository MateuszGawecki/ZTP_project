package com.example.ztp_project.controller;

import com.example.ztp_project.entity.Book;
import com.example.ztp_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController()
@RequestMapping("/book")
public class BookController {

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public void createBook(@RequestBody Mono<Book> bookMono){
        bookMono.flatMap(bookRepository::save).subscribe();
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Book> getAll(){
        return bookRepository.findAll().delayElements(Duration.ofSeconds(1));
    }
}
