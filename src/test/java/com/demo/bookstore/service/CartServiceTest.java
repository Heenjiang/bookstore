package com.demo.bookstore.service;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.Cart;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateCart() {
        Cart cart = new Cart();
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart createdCart = cartService.createCart();

        assertNotNull(createdCart);
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void shouldAddItemToCart() {
        Cart cart = new Cart();
        Book book = new Book();
        book.setTitle("Spring Boot");

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.addItemToCart(1L, book, 2);

        assertEquals(1, updatedCart.getItems().size());
        assertEquals(2, updatedCart.getItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void shouldUpdateItemQuantityInCart() {
        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setQuantity(2);
        cart.addItem(item);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.updateItemQuantity(1L, 1L, 5);

        assertEquals(5, updatedCart.getItems().get(0).getQuantity());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void shouldRemoveItemFromCart() {
        Cart cart = new Cart();
        CartItem item = new CartItem();
        cart.addItem(item);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart updatedCart = cartService.removeItemFromCart(1L, 1L);

        assertTrue(updatedCart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void shouldGetTotalPrice() {
        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setQuantity(2);
        Book book = new Book();
        book.setPrice(49.99);
        item.setBook(book);
        cart.addItem(item);

        when(cartRepository.findById(anyLong())).thenReturn(Optional.of(cart));

        double totalPrice = cartService.getTotalPrice(1L);

        assertEquals(99.98, totalPrice);
    }
}
