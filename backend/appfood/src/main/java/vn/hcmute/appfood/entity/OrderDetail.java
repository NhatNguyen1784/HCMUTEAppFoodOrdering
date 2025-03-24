package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Long id;

    @NotNull(message = "Quantity cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Integer quantity;

    @NotNull(message = "Price cannot be null")
    @PositiveOrZero(message = "Total price must be greater than or equal to 0")
    private Double price;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
    @NotNull(message = "Order cannot be null")
    @JsonIgnore // Ngăn không cho vòng lặp vô hạn khi trả về JSON
    private Order order;
}
