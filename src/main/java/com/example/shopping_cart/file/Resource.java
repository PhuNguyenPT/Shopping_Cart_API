package com.example.shopping_cart.file;

import com.example.shopping_cart.models.Lecture;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.math.BigInteger;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Inheritance(
        strategy = InheritanceType.TABLE_PER_CLASS
)
//@DiscriminatorColumn(
//        name = "resource_type"
//) -> only with single table strategy
public class Resource {
    @Id
    @GeneratedValue
    private Integer id;
    @Column(unique = true)
    private String name;
    private String url;
    private BigInteger size;
    @OneToOne()
    @JoinColumn(
            name = "lecture_id"
    )
    private Lecture lecture;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public BigInteger getSize() {
        return size;
    }

    public void setSize(BigInteger size) {
        this.size = size;
    }

    public Lecture getLecture() {
        return lecture;
    }

    public void setLecture(Lecture lecture) {
        this.lecture = lecture;
    }
}
