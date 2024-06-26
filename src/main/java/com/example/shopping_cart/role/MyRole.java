package com.example.shopping_cart.role;

import com.example.shopping_cart.user.MyUser;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "roles")
public class MyRole implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String authority;
    @CreatedDate
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdDate;
    @LastModifiedDate
    @Column(insertable = false)
    private LocalDateTime lastModifiedDate;
    @Column(nullable = false, updatable = false)
    private ZoneId createdTimeZone;
    @Column(insertable = false)
    private ZoneId modifiedTimeZone;

    @ManyToMany(mappedBy = "roles")
    @JsonIgnore
    private List<MyUser> users = new ArrayList<>();

    @Override
    public String getAuthority() {
        return this.authority;
    }

    @PrePersist
    public void prePersist() {
        createdTimeZone = ZoneId.systemDefault();
    }

    @PreUpdate
    public void preUpdate() {
        modifiedTimeZone = ZoneId.systemDefault();
    }
    public enum Value {
        ADMIN,
        USER,
        DELIVERER
    }
}
