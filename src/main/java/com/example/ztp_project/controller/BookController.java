package com.example.ztp_project.controller;

import com.example.ztp_project.entity.Book;
import com.example.ztp_project.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

@RestController()
@RequestMapping("/book")
public class BookController {
    DateTimeFormatter format = new DateTimeFormatterBuilder()
            .appendPattern("yyyy")
            .parseDefaulting(ChronoField.MONTH_OF_YEAR, 1)
            .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
            .toFormatter();

    private BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @PostMapping
    public void createBook(@RequestBody Mono<Book> bookMono){
        bookMono.flatMap(bookRepository::save).subscribe();
    }

    @DeleteMapping(path = "/{id}")
    public void createBook(@PathVariable String id){
        bookRepository.deleteById(id).subscribe();
    }

    @GetMapping(produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
    public Flux<Book> getBooks(@RequestParam(required = false, name = "title") String title,
                               @RequestParam(required = false, name = "author") String author,
                               @RequestParam(required = false, name = "year") String year){

        if(title != null)
            return bookRepository.findByTitle(title);

        if(author != null)
            return bookRepository.findByAuthor(author);

        if(year != null){
            int year1 = Integer.parseInt(year);
            LocalDate start = LocalDate.of(year1,1,1);
            LocalDate end = LocalDate.of(year1,12,31);

            return bookRepository.findBookByPublishedBetween(start,end);
        }

        return bookRepository.findAll().delayElements(Duration.ofSeconds(1));
    }

}
