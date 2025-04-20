package vn.hcmute.appfood.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Food {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "food_id")
    private Long id;

    private String foodName;
    private String foodDescription;
    private Double foodPrice;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "category_id")
    @JsonBackReference // Để tránh vòng lặp khi serialize JSON
    private Category category;

    @OneToMany(mappedBy = "food")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @JsonManagedReference // Áp dụng cho bên "nhiều" trong mối quan hệ One-to-Many
    private List<FoodImage> foodImages;
}
