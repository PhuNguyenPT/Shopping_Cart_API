package com.example.shopping_cart.models;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "lectures")
public class Lecture {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(
            name = "section_id"
    )
    private Section section;
//    @OneToOne
//    @JoinColumn(
//            name = "resource_id"
//    )
//    private Resource resource;
}
