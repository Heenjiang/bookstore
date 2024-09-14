package com.demo.bookstore.integration;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
    }

    @Test
    void shouldCreateBook() throws Exception {
        String bookJson = "{ \"title\": \"Spring Boot\", \"author\": \"John\", \"category\": \"Tech\", \"price\": 49.99 }";

        mockMvc.perform(post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Spring Boot")))
                .andExpect(jsonPath("$.author", is("John")));
    }

    @Test
    void shouldGetAllBooks() throws Exception {
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        bookRepository.save(book);

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title", is("Spring Boot")));
    }

    @Test
    void shouldUpdateBook() throws Exception {
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        String updatedBookJson = "{ \"title\": \"Spring Boot 2.0\", \"author\": \"John Updated\", \"category\": \"Tech\", \"price\": 59.99 }";

        mockMvc.perform(put("/api/books/{id}/update", savedBook.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updatedBookJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title", is("Spring Boot 2.0")))
                .andExpect(jsonPath("$.author", is("John Updated")));
    }

    @Test
    void shouldDeleteBook() throws Exception {
        Book book = new Book();
        book.setTitle("Spring Boot");
        book.setAuthor("John");
        book.setCategory("Tech");
        book.setPrice(49.99);
        Book savedBook = bookRepository.save(book);

        mockMvc.perform(delete("/api/books/{id}/delete", savedBook.getId()))
                .andExpect(status().isOk());

        mockMvc.perform(get("/api/books/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }
}
