package com.example.shopping_cart.models;

import com.example.shopping_cart.file.Resource;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class Lecture {
    @Id
    @GeneratedValue
    private Integer id;
    private String name;
    @ManyToOne
    @JoinColumn(
            name = "section_id"
    )
    private Section section;
    @OneToOne
    @JoinColumn(
            name = "resource_id"
    )
    private Resource resource;
}
