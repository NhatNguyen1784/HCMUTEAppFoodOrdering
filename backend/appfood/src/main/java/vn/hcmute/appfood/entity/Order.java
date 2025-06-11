package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import vn.hcmute.appfood.utils.DeliveryMethod;
import vn.hcmute.appfood.utils.OrderStatus;
import vn.hcmute.appfood.utils.PaymentOption;

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

    @Column(nullable = false)
    @NotNull(message = "Shipping address cannot be null")
    private String fullAddress;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Payment option cannot be null")
    private PaymentOption paymentOption;

    @Column(nullable = false, length = 20)
    @NotNull(message = "status not null")
    @Enumerated(EnumType.STRING)//Lưu tên enum dưới dạng string
    @JsonFormat(shape = JsonFormat.Shape.STRING)// Muốn enum ignore case khi deserialize từ JSON
    private OrderStatus orderStatus;

    @Column(nullable = false)
    @NotNull(message = "Total quantity not null")
    private int totalQuantity;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Delivery method cannot be null")
    private DeliveryMethod deliveryMethod;


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

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)//Khi delete order thì all orderdetail related sẽ xóa theo
    @JsonBackReference // Áp dụng cho bên "nhiều" trong mối quan hệ One-to-Many
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<OrderDetail> orderDetailSet;
}
