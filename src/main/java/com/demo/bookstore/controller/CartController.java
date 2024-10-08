package com.demo.bookstore.controller;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.Cart;
import com.demo.bookstore.service.BookService;
import com.demo.bookstore.service.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private final CartService cartService;
    private final BookService bookService;

    public CartController(CartService cartService, BookService bookService) {
        this.cartService = cartService;
        this.bookService = bookService;
    }

    @PostMapping("/create")
    public Cart createCart() {
        return cartService.createCart();
    }

    @PostMapping("/{cartId}/add/{itemId}")
    public Cart addItemToCart(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam Integer quantity) {
        return cartService.addItemToCart(cartId, itemId, quantity);
    }

    @PutMapping("/{cartId}/update/{itemId}")
    public Cart updateItemQuantity(@PathVariable Long cartId, @PathVariable Long itemId, @RequestParam int quantity) {
        return cartService.updateItemQuantity(cartId, itemId, quantity);
    }

    @DeleteMapping("/{cartId}/remove/{itemId}")
    public Cart removeItemFromCart(@PathVariable Long cartId, @PathVariable Long itemId) {
        return cartService.removeItemFromCart(cartId, itemId);
    }

    @GetMapping("/{cartId}")
    public ResponseEntity<Cart> getCart(@PathVariable Long cartId) {
        Optional<Cart> cart = cartService.getCart(cartId);
        return cart.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/{cartId}/total")
    public double getTotalPrice(@PathVariable Long cartId) {
        return cartService.getTotalPrice(cartId);
    }
}
