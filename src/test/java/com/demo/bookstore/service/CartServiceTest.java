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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private BookService bookService;

    @InjectMocks
    private CartService cartService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddItemToCart() {
        Long cartId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        Cart cart = new Cart();
        Book book = new Book();

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addItemToCart(cartId, bookId, quantity);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(book, result.getItems().get(0).getBook());
        assertEquals(quantity, result.getItems().get(0).getQuantity());

        verify(cartRepository).findById(cartId);
        verify(bookService).getBookById(bookId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testAddItemToCart_CartNotFound() {
        Long cartId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        when(cartRepository.findById(cartId)).thenReturn(Optional.empty());

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                cartService.addItemToCart(cartId, bookId, quantity)
        );

        assertEquals("Cart not found", thrown.getMessage());
    }

    @Test
    void testUpdateItemQuantity() {
        Long cartId = 1L;
        Long itemId = 2L;
        int newQuantity = 5;

        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setId(itemId);
        item.setQuantity(3);
        cart.addItem(item);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.updateItemQuantity(cartId, itemId, newQuantity);

        assertNotNull(result);
        assertEquals(newQuantity, result.getItems().get(0).getQuantity());

        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testUpdateItemQuantity_ItemNotFound() {
        Long cartId = 1L;
        Long itemId = 2L;
        int newQuantity = 5;

        Cart cart = new Cart();
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                cartService.updateItemQuantity(cartId, itemId, newQuantity)
        );

        assertEquals("Item not found", thrown.getMessage());
    }

    @Test
    void testRemoveItemFromCart() {
        Long cartId = 1L;
        Long itemId = 2L;

        Cart cart = new Cart();
        CartItem item = new CartItem();
        item.setId(itemId);
        cart.addItem(item);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.removeItemFromCart(cartId, itemId);

        assertNotNull(result);
        assertTrue(result.getItems().isEmpty());

        verify(cartRepository).findById(cartId);
        verify(cartRepository).save(any(Cart.class));
    }

    @Test
    void testRemoveItemFromCart_ItemNotFound() {
        Long cartId = 1L;
        Long itemId = 2L;

        Cart cart = new Cart();
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                cartService.removeItemFromCart(cartId, itemId)
        );

        assertEquals("Item not found", thrown.getMessage());
    }

    @Test
    void testAddItemToCart_Success() {
        Long cartId = 1L;
        Long bookId = 2L;
        int quantity = 3;

        Cart cart = new Cart();
        Book book = new Book();
        book.setId(bookId);

        when(cartRepository.findById(cartId)).thenReturn(Optional.of(cart));
        when(bookService.getBookById(bookId)).thenReturn(Optional.of(book));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Cart result = cartService.addItemToCart(cartId, bookId, quantity);

        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        CartItem addedItem = result.getItems().get(0);
        assertEquals(book, addedItem.getBook());
        assertEquals(quantity, addedItem.getQuantity());

        verify(cartRepository).findById(cartId);
        verify(bookService).getBookById(bookId);
        verify(cartRepository).save(any(Cart.class));
    }


}
