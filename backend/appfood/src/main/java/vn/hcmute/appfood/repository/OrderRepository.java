package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o.user.email FROM Order o WHERE o.id = :orderId")
    String findEmailByOrderId(@Param("orderId") Long orderId);
    List<Order> findAllByUserId(long userId);
    Long countByUserId(long userId);
    Long countByUserIdAndOrderStatus(long userId, OrderStatus orderStatus);
    Optional<Order> findById(long id);
}
