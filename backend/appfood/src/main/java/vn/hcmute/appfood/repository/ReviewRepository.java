package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.OrderDetail;
import vn.hcmute.appfood.entity.ProductReview;
import vn.hcmute.appfood.entity.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ProductReview, Long> {

    // ham kiem tra xem user da danh gia san pham trong don hang chua
    boolean existsByOrderDetailAndUser(OrderDetail orderDetail, User user);

    // ham de lay danh gia mon an, dua tren ten mon an
    List<ProductReview> findByOrderDetail_FoodName(String foodName);
}
