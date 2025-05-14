package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.OrderDetail;
import vn.hcmute.appfood.entity.ProductReview;
import vn.hcmute.appfood.entity.User;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ProductReview, Long> {

    // ham kiem tra xem user da danh gia san pham trong don hang chua
    boolean existsByOrderDetailAndUser(OrderDetail orderDetail, User user);

    // Lấy danh sách đánh giá món ăn theo foodId và trạng thái đơn hàng là "DELIVERED"
    @Query("SELECT pr FROM ProductReview pr " +
            "JOIN pr.orderDetail od " +
            "JOIN od.order o " +
            "WHERE od.food.id = :foodId " +
            "AND o.orderStatus = 'DELIVERED'")
    List<ProductReview> findByOrderDetail_Food(Long foodId);
}
