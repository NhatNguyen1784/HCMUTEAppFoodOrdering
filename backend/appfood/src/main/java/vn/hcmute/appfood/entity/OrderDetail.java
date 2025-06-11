package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_detail_id")
    private Long id;

    @NotNull(message = "Not null")
    @Column(nullable = false)
    private String foodName;

    @Column(nullable = false)
    @NotNull(message = "Not null")
    private Double unitPrice;

    @NotNull(message = "Quantity cannot be null")
    @Column(nullable = false)
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Not null")
    @Column(nullable = false)
    private String foodImage;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double price; //= Quantity * unitPrice

    @Column(nullable = false)
    private Boolean isReview = false;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    @NotNull(message = "Order cannot be null")
    @JsonIgnore // Ngăn không cho vòng lặp vô hạn khi trả về JSON
    private Order order;

    @OneToOne(mappedBy = "orderDetail", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference // nếu cần serialize sang JSON
    private ProductReview review;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "food_id", referencedColumnName = "food_id", nullable = false)
    @JsonIgnore
    private Food food;
}
