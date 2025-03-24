package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
public class CartDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @NotNull(message = "Food cannot be null")
    @JoinColumn(name = "food_id", nullable = false)
    @JsonBackReference // Để tránh vòng lặp khi serialize JSON
    private Food food;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    @JsonBackReference // Để tránh vòng lặp khi serialize JSON
    private Cart cart;
}
