package com.programacion.distribuida.servicios;

import com.programacion.distribuida.db.Author;
import com.programacion.distribuida.repository.AuthorRepository;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.List;


@ApplicationScoped
public class AuthorServiceImpl implements AuthorService {
    @Inject
    private AuthorRepository authorRepository;

    @Override
    public List<Author> getAuthors() {
        return authorRepository.listAll();
    }

    @Override
    public Author getAuthorById(Long id) {
        return authorRepository.findById(id);
    }


    @Transactional
    @Override
    public void createAuthor(Author author) {
        authorRepository.persist(author);

    }
    @Transactional
    @Override
    public void updateAuthor(Long id, Author author) {
        Author savedAuthor=getAuthorById(id);
        savedAuthor.setFirst_name(author.getFirst_name());
        savedAuthor.setLast_name(author.getLast_name());
        authorRepository.persist(savedAuthor);

    }

    @Transactional
    @Override
    public void delete(Long id) {
        authorRepository.deleteById(id);
    }
}
