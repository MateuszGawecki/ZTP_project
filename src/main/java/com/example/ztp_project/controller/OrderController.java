package com.example.ztp_project.controller;

import com.example.ztp_project.entity.Book;
import com.example.ztp_project.entity.Cart;
import com.example.ztp_project.entity.Order;
import com.example.ztp_project.repository.BookRepository;
import com.example.ztp_project.repository.CartRepository;
import com.example.ztp_project.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
@RequestMapping("/order")
public class OrderController {

    private OrderRepository orderRepository;
    private BookRepository bookRepository;
    private CartRepository cartRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository, BookRepository bookRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.bookRepository = bookRepository;
        this.cartRepository = cartRepository;
    }

    @PostMapping()
    public void createOrder(@RequestParam(required = false, name = "cartId") String cartId){
        Mono<Cart> cart = cartRepository.findById(cartId);

        cart.flatMap(cart1 -> {
            Order order = new Order(cart1.getBooks());
            return orderRepository.save(order);
        }).subscribe();
    }


    @GetMapping()
    public Flux<Order> getOrders(){
        return orderRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flux<Book> getOrderBooks(@PathVariable String id){
        Mono<Order> order = orderRepository.findById(id);

        return order.flux().flatMap(cart1 -> {
            List<String> bookIds = cart1.getBooks();
            return bookRepository.findAllById(bookIds);
        });
    }
}
