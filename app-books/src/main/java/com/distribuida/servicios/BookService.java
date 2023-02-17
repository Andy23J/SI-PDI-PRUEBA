package com.distribuida.servicios;

import com.distribuida.db.Book;
import java.util.List;

public interface BookService {

    List<Book> getBooks();
    Book getBookById(Integer id);
    void createBook(Book book);
    void updateBook(Integer id,Book book);
    void delete(Integer id);

}
