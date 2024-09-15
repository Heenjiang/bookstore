package com.demo.bookstore.integration;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.model.Cart;
import com.demo.bookstore.model.CartItem;
import com.demo.bookstore.service.BookService;
import com.demo.bookstore.service.CartService;
import com.demo.bookstore.controller.CartController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    @Autowired
    private CartService cartService;

    private Book testBook;
    private Cart testCart;

    @BeforeEach
    void setUp() {
        // Setup test data
        testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setPrice(20.0);
        testBook = bookService.addBook(testBook);

        testCart = cartService.createCart();
    }

    @Test
    void shouldCreateCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/create")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists());
    }

    @Test
    void shouldAddItemToCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/api/cart/{cartId}/add/{itemId}", testCart.getId(), testBook.getId())
                        .param("quantity", "3")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].book.title").value("Test Book"))
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }

    @Test
    @Transactional
    void shouldUpdateItemQuantity() throws Exception {
        // Add the item to the cart
        testCart = cartService.addItemToCart(testCart.getId(), testBook.getId(), 2);

        // Fetch the updated cart
        Cart updatedCart = cartService.getCart(testCart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Get the CartItem ID
        CartItem itemToRemove = updatedCart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(testBook.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));


        mockMvc.perform(MockMvcRequestBuilders.put("/api/cart/{cartId}/update/{itemId}", testCart.getId(), itemToRemove.getId())
                        .param("quantity", "5")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity").value(5));
    }

    @Transactional// Ensure the session remains open
    void shouldRemoveItemFromCart() throws Exception {
        // Add the item to the cart
        testCart = cartService.addItemToCart(testCart.getId(), testBook.getId(), 2);

        // Fetch the updated cart
        Cart updatedCart = cartService.getCart(testCart.getId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        // Get the CartItem ID
        CartItem itemToRemove = updatedCart.getItems().stream()
                .filter(item -> item.getBook().getId().equals(testBook.getId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("CartItem not found"));

        // Perform the delete request with the correct item ID
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/cart/{cartId}/remove/{itemId}", testCart.getId(), itemToRemove.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }
    @Test
    void shouldGetCart() throws Exception {
        int quantity = 3;
        testCart = cartService.addItemToCart(testCart.getId(), testBook.getId(), quantity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{cartId}", testCart.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(testCart.getId()))
                .andExpect(jsonPath("$.items[0].book.title").value("Test Book"))
                .andExpect(jsonPath("$.items[0].quantity").value(3));
    }

    @Test
    void shouldGetTotalPrice() throws Exception {
        int quantity = 3;
        testCart = cartService.addItemToCart(testCart.getId(), testBook.getId(), quantity);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{cartId}/total", testCart.getId())
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("60.0"));
    }

    @Test
    void shouldReturnNotFoundForInvalidCart() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/api/cart/{cartId}", 999L)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
