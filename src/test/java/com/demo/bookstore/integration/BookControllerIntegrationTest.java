package com.demo.bookstore.integration;

import com.demo.bookstore.model.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import com.demo.bookstore.service.BookService;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookService bookService;

    private Book existingBook;

    @BeforeEach
    void setUp() {
        // Clear existing books
        bookService.getAllBooks().forEach(book -> bookService.deleteBook(book.getId()));

        // Add a test book
        Book testBook = new Book();
        testBook.setTitle("Test Book");
        testBook.setPrice(20.0);
        existingBook = bookService.addBook(testBook);
    }

    @Test
    void shouldReturnNotFoundForInvalidBook() throws Exception {
        Long nonExistentBookId = 999L;
        String updatedBookJson = "{"
                + "\"title\":\"Non-existent Book\","
                + "\"author\":\"Non-existent Author\","
                + "\"category\":\"Non-existent Category\","
                + "\"price\":0.0,"
                + "\"discount\":0.0,"
                + "\"publisher\":\"Non-existent Publisher\","
                + "\"publicationDate\":\"2024-12-01\""
                + "}";

        mockMvc.perform(put("/api/books/{id}/update", nonExistentBookId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.message").value("Book not found with id " + nonExistentBookId));
    }

    @Test
    void shouldUpdateExistingBook() throws Exception {
        String updatedBookJson = "{"
                + "\"title\":\"Updated Test Book\","
                + "\"author\":\"Updated Author\","
                + "\"category\":\"Updated Category\","
                + "\"price\":25.0,"
                + "\"discount\":5.0,"
                + "\"publisher\":\"Updated Publisher\","
                + "\"publicationDate\":\"2025-01-01\""
                + "}";

        mockMvc.perform(put("/api/books/{id}/update", existingBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Updated Test Book"))
                .andExpect(jsonPath("$.price").value(25.0));
    }

    @Test
    void shouldGetBooks() throws Exception {
        mockMvc.perform(get("/api/books/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Test Book"))
                .andExpect(jsonPath("$[0].price").value(20.0));
    }
}
