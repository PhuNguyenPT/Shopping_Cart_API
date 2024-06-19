package com.example.shopping_cart.author;

import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.models.Course;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@NamedQuery(
        name = "Author.findByNamedQuery",
        query = "select a from Author a where a.age >= :age"
)
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
