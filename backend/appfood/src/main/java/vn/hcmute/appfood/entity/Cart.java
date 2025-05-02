package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @NotNull(message = "User cannot be null")
    @JoinColumn(name = "user_id", nullable = false) // Khóa ngoại đến User
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private User user;

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "Total price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double totalPrice;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL)
    @JsonManagedReference // Áp dụng cho bên "nhiều" trong mối quan hệ One-to-Many
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<CartDetail> cartDetails;
}
