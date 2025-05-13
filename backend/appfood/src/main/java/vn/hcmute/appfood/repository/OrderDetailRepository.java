package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.OrderDetail;

import java.util.Set;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {
    Set<OrderDetail> findByOrderId(Long orderId);
}
