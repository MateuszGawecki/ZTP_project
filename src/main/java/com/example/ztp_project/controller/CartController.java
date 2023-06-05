package com.example.ztp_project.controller;

import com.example.ztp_project.entity.Book;
import com.example.ztp_project.entity.Cart;
import com.example.ztp_project.repository.BookRepository;
import com.example.ztp_project.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController()
@RequestMapping("/carts")
public class CartController {
    private CartRepository cartRepository;
    private BookRepository bookRepository;

    @Autowired
    public CartController(CartRepository cartRepository, BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.bookRepository = bookRepository;
    }

    @GetMapping()
    public Flux<Cart> getCarts(){
        return cartRepository.findAll();
    }

    @GetMapping("/{id}")
    public Flux<Book> getCartBooks(@PathVariable String id){
        Mono<Cart> cart = cartRepository.findById(id);

        return cart.flux().flatMap(cart1 -> {
            List<String> bookIds = cart1.getBooks();
            return bookRepository.findAllById(bookIds);
        });
    }

    @PostMapping
    public void createCart(@RequestBody Mono<Cart> cartMono){
        cartMono.flatMap(cartRepository::save).subscribe();
    }

    @PutMapping("/{cartId}")
    public void addBookToCart(@PathVariable String cartId, @RequestParam(name = "bookId") String bookId){
        cartRepository.findById(cartId).flatMap(cart -> {
            cart.addBook(bookId);
            return cartRepository.save(cart);
        }).subscribe();
    }

    @DeleteMapping("/{cartId}")
    public void deleteBookFromCartOrCart(@PathVariable String cartId,
                                         @RequestParam(required = false, name = "bookId") String bookId){

        if(bookId != null){
            cartRepository.findById(cartId).flatMap(cart -> {
                cart.removeBook(bookId);
                return cartRepository.save(cart);
            }).subscribe();
        }

        cartRepository.deleteById(cartId).subscribe();
    }
}
