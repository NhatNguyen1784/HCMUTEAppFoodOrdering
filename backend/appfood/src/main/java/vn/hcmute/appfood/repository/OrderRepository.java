package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.Order;
import vn.hcmute.appfood.utils.OrderStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findAllByUserId(long userId);
    Long countByUserId(long userId);
    Long countByUserIdAndOrderStatus(long userId, OrderStatus orderStatus);
    Optional<Order> findById(long id);
}
