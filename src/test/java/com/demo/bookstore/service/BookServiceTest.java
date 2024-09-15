package com.demo.bookstore.service;

import com.demo.bookstore.model.Book;
import com.demo.bookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book testBook;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        Book testBook = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(testBook);
        testBook = bookService.addBook(testBook);
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book result = bookService.addBook(book);

        assertNotNull(result);
        verify(bookRepository).save(book);
    }

    @Test
    void testGetBooks() {
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findAll()).thenReturn(books);

        List<Book> result = bookService.getBooks();

        assertEquals(2, result.size());
        verify(bookRepository).findAll();
    }

    @Test
    void testGetBookById() {
        Long id = 1L;
        Book book = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> result = bookService.getBookById(id);

        assertTrue(result.isPresent());
        assertEquals(book, result.get());
        verify(bookRepository).findById(id);
    }

    @Test
    void testGetBookById_NotFound() {
        Long id = 1L;
        when(bookRepository.findById(id)).thenReturn(Optional.empty());

        Optional<Book> result = bookService.getBookById(id);

        assertFalse(result.isPresent());
        verify(bookRepository).findById(id);
    }

    @Test
    void testUpdateBook() {
        Long id = 1L;
        Book existingBook = new Book();
        Book bookDetails = new Book();
        when(bookRepository.findById(id)).thenReturn(Optional.of(existingBook));
        when(bookRepository.save(any(Book.class))).thenReturn(existingBook);

        Book result = bookService.updateBook(id, bookDetails);

        assertNotNull(result);
        verify(bookRepository).findById(id);
        verify(bookRepository).save(existingBook);
    }

    @Test
    void testDeleteBook_Notfound() {
        Long testBookId = 999L;
        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                bookService.deleteBook(testBookId)
        );

        assertEquals("Book not found with id " + testBookId, thrown.getMessage());
    }

    @Test
    void testSearchBooks() {
        String keyword = "test";
        List<Book> books = List.of(new Book(), new Book());
        when(bookRepository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword, keyword, keyword)).thenReturn(books);

        List<Book> result = bookService.searchBooks(keyword);

        assertEquals(2, result.size());
        verify(bookRepository).findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCaseOrCategoryContainingIgnoreCase(keyword, keyword, keyword);
    }
}
