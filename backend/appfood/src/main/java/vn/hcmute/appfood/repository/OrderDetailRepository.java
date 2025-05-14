package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.OrderDetail;

import java.util.Set;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Set<OrderDetail> findByOrderId(Long orderId);

    /**
     * Đếm số lượng đánh giá dựa vào foodId và trạng thái đơn hàng là SUCCESSFUL
     * @param foodId id của món ăn
     * @return tổng số lượng đánh giá
     */
    @Query("SELECT COUNT(od) FROM OrderDetail od JOIN od.order o WHERE od.food.id = :foodId AND o.orderStatus = 'SUCCESSFUL'")
    Long countReviewsByFoodId(@Param("foodId") Long foodId);
}
