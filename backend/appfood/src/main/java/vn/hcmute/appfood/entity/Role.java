package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String roleName;

    @OneToMany(mappedBy = "role") // Mối quan hệ One-to-Many
    @JsonBackReference // Áp dụng cho bên "nhiều" trong mối quan hệ One-to-Many
    private List<User> users;
}
