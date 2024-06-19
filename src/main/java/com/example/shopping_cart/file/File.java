package com.example.shopping_cart.file;

import com.example.shopping_cart.common.BaseEntity;
import com.example.shopping_cart.common.Resource;
import com.example.shopping_cart.product.Product;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigInteger;

//@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@SuperBuilder
@Entity
@Getter
@Setter
//@PrimaryKeyJoinColumn(name = "file_id") -> only with join table strategy
//@DiscriminatorValue("F") -> only with single table strategy
@Table(name = "files")
public class File extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "file_type")
    private String fileType;
    @Column(name = "file_content")
    private byte[] fileContent;

    private String name;
    private String url;
    private BigInteger size;

    @ManyToOne
    @JoinTable(name = "files_products",
            joinColumns = @JoinColumn(name = "file_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private Product product;
}
