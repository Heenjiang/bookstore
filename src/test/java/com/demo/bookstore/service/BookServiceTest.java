package com.demo.bookstore.service;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldAddBook() {
        Book book = new Book();
        book.setTitle("Test Book");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book createdBook = bookService.addBook(book);

        assertEquals("Test Book", createdBook.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void shouldUpdateBook() {
        Book book = new Book();
        book.setTitle("Original Title");

        when(bookRepository.findById(anyLong())).thenReturn(Optional.of(book));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book updatedBook = new Book();
        updatedBook.setTitle("Updated Title");

        Book result = bookService.updateBook(1L, updatedBook);

        assertEquals("Updated Title", result.getTitle());
        verify(bookRepository, times(1)).save(book);
    }

    @Test
    void shouldDeleteBook() {
        bookService.deleteBook(1L);
        verify(bookRepository, times(1)).deleteById(1L);
    }
}
