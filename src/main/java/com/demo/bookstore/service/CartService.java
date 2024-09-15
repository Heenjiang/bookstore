package com.demo.bookstore.service;

import com.demo.bookstore.exception.ResourceNotFoundException;
import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.Cart;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.repository.CartRepository;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final BookService bookService;

    public CartService(CartRepository cartRepository, BookService bookService) {
        this.cartRepository = cartRepository;
        this.bookService = bookService;
    }

    @Transactional
    public Cart createCart() {
        return cartRepository.save(new Cart());
    }

    @Transactional
    public Optional<Cart> getCart(Long cartId) {
        return cartRepository.findById(cartId);
    }

    @Transactional
    public double getTotalPrice(Long cartId) {
        Cart cart = getCart(cartId).orElseThrow(() -> new ResourceNotFoundException("Cart not found " + cartId));
        return cart.getTotalPrice();
    }

    @Transactional
    public Cart addItemToCart(Long cartId, Long bookId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found " + cartId));

        Book book = bookService.getBookById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found " + bookId));

        CartItem item = new CartItem();
        item.setBook(book);
        item.setQuantity(quantity);
        item.setCart(cart); // Ensure bidirectional relationship is set

        cart.addItem(item);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateItemQuantity(Long cartId, Long itemId, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be greater than zero");
        }

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found " + cartId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found " + itemId));

        item.setQuantity(quantity);
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart removeItemFromCart(Long cartId, Long itemId) {
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart not found " + cartId));

        CartItem item = cart.getItems().stream()
                .filter(cartItem -> cartItem.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Item not found " + itemId));

        cart.removeItem(item);
        return cartRepository.save(cart);
    }


}
