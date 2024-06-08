package com.example.shopping_cart.author;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuthorRepository extends JpaRepository <Author, Integer> {
    @Transactional
    List<Author> findByNamedQuery(@Param("age")int age);
    // update Author a set a.age = 22 where a.id = 1
    @Modifying
    @Transactional
    @Query("update Author a set a.age = :age where a.id = :id")
    int updateAuthor(int age, int id);
    // select * from author where first_name = 'abc'
    List<Author> findAllByFirstName(String fn);
    // select * from author where first_name = 'ab'
    List<Author> findAllByFirstNameIgnoreCase(String fn);
    // select * from author where first_name like '%ab%'
    List<Author> findAllByFirstNameContainingIgnoreCase(String fn);
    // select * from author where first_name like 'ab%'
    List<Author> findAllByFirstNameStartsWithIgnoreCase(String fn);
    // select * from author where first_name like '%ab'
    List<Author> findAllByFirstNameEndsWithIgnoreCase(String fn);
    // select * from author where first_name in ('abc', 'def', 'hij')
    List<Author> findAllByFirstNameInIgnoreCase(List<String> fn);

}
