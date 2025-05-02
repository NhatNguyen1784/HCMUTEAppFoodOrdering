package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.SliderItem;

import java.util.Optional;

@Repository
public interface SliderItemRepository extends JpaRepository<SliderItem, Long> {
    Optional<SliderItem> findById(long sliderId);
}
