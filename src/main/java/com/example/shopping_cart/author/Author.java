package com.example.shopping_cart.author;

import com.example.shopping_cart.models.BaseEntity;
import com.example.shopping_cart.models.Course;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@NamedQuery(
        name = "Author.findByNamedQuery",
        query = "select a from Author a where a.age >= :age"
)
public class Author {
    @Id
    @GeneratedValue
    private Integer id;
    @Embedded
    private BaseEntity baseEntity;
    private String firstName;
    private String lastName;
    @Column(
            unique = true
    )
    private String email;
    private int age;
    @ManyToMany(
            mappedBy = "authors"
    )
    private List<Course> courses;
}
