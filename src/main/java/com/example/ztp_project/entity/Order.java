package com.example.ztp_project.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document
public class Order {
    @Id
    private String id;
    private List<String> books;

    public Order() {
    }

    public Order(List<String> books) {
        this.books = books;
    }

    public Order(String id, List<String> books) {
        this.id = id;
        this.books = books;
    }

    public String getId() {
        return id;
    }

    public List<String> getBooks() {
        return books;
    }
}
