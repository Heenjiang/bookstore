package com.demo.bookstore.integration;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.repository.BookRepository;
import com.demo.bookstore.repository.CartRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CartControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        cartRepository.deleteAll();
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateCart() throws Exception {
        mockMvc.perform(post("/api/cart/create"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldAddItemToCart() throws Exception {
        // Create a book
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        // Create a cart
        String createCartResponse = mockMvc.perform(post("/api/cart/create"))
                .andReturn().getResponse().getContentAsString();
        Long cartId = Long.parseLong(createCartResponse.replaceAll("\\D+", ""));

        // Add item to cart
        mockMvc.perform(post("/api/cart/{cartId}/add", cartId)
                        .param("bookId", savedBook.getId().toString())
                        .param("quantity", "2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity", is(2)));
    }

    @Test
    void shouldUpdateItemQuantityInCart() throws Exception {
        // Create a book
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        // Create a cart and add item to cart
        String createCartResponse = mockMvc.perform(post("/api/cart/create"))
                .andReturn().getResponse().getContentAsString();
        Long cartId = Long.parseLong(createCartResponse.replaceAll("\\D+", ""));

        mockMvc.perform(post("/api/cart/{cartId}/add", cartId)
                        .param("bookId", savedBook.getId().toString())
                        .param("quantity", "2"))
                .andExpect(status().isOk());

        // Update item quantity
        mockMvc.perform(put("/api/cart/{cartId}/update/{itemId}", cartId, 1L)
                        .param("quantity", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].quantity", is(5)));
    }

    @Test
    void shouldRemoveItemFromCart() throws Exception {
        // Create a book
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        // Create a cart and add item to cart
        String createCartResponse = mockMvc.perform(post("/api/cart/create"))
                .andReturn().getResponse().getContentAsString();
        Long cartId = Long.parseLong(createCartResponse.replaceAll("\\D+", ""));

        mockMvc.perform(post("/api/cart/{cartId}/add", cartId)
                        .param("bookId", savedBook.getId().toString())
                        .param("quantity", "2"))
                .andExpect(status().isOk());

        // Remove item from cart
        mockMvc.perform(delete("/api/cart/{cartId}/remove/{itemId}", cartId, 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").isEmpty());
    }

    @Test
    void shouldGetTotalPriceOfCart() throws Exception {
        // Create a book
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        // Create a cart and add item to cart
        String createCartResponse = mockMvc.perform(post("/api/cart/create"))
                .andReturn().getResponse().getContentAsString();
        Long cartId = Long.parseLong(createCartResponse.replaceAll("\\D+", ""));

        mockMvc.perform(post("/api/cart/{cartId}/add", cartId)
                        .param("bookId", savedBook.getId().toString())
                        .param("quantity", "2"))
                .andExpect(status().isOk());

        // Get total price
        mockMvc.perform(get("/api/cart/{cartId}/total", cartId))
                .andExpect(status().isOk())
                .andExpect(content().string("99.98"));
    }
}
