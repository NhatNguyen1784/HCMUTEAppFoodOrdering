package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Food cannot be null")
    @JoinColumn(name = "food_id", nullable = false)
    @JsonIgnore // Để tránh vòng lặp khi serialize JSON
    @JsonBackReference
    private Food food;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Integer quantity;

    private Double unitPrice; // gia goc cua san pham

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double price; // = unitPrice * quantity

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    // Để tránh vòng lặp khi serialize JSON
    @JsonBackReference
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Cart cart;
}
