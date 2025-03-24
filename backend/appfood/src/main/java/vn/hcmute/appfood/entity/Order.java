package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long id;

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "Total price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double totalPrice;

    @Column(name = "created_date", nullable = false, updatable = false)
    @CreationTimestamp // Hibernate tự động tạo giá trị thời gian
    private LocalDateTime createdDate;

    @ManyToOne
    @NotNull(message = "User cannot be null")
    @JoinColumn(name = "user_id", referencedColumnName = "user_id", nullable = false) // Khóa ngoại đến User
    private User user;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @JsonBackReference // Áp dụng cho bên "nhiều" trong mối quan hệ One-to-Many
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderDetail> orderDetailSet;
}
