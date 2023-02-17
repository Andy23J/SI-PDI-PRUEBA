package com.distribuida.servicios;

import com.distribuida.db.Book;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import java.util.List;

@ApplicationScoped
public class BookServiceImpl implements BookService {
    @PersistenceContext(unitName = "jpa")
    private EntityManager entityManager;

    @Override
    public List<Book> getBooks()  {
        return entityManager
                .createNamedQuery("Book.findAll",Book.class)
                .getResultList();
    }

    @Override
    public Book getBookById(Integer id){
        return entityManager.find(Book.class,id);
    }


    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void createBook(Book book) {
        entityManager.persist(book);
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void updateBook(Integer id, Book book){
        var savedBook=this.getBookById(id);
        if (savedBook != null) {
            savedBook.setIsbn(book.getIsbn());
            savedBook.setAuthor(book.getAuthor());
            savedBook.setTitle(book.getTitle());
            savedBook.setPrice(book.getPrice());
            entityManager.persist(book);
        }
    }

    @Override
    @Transactional(Transactional.TxType.REQUIRED)
    public void delete(Integer id) {
        entityManager.remove(getBookById(id));
    }

}
