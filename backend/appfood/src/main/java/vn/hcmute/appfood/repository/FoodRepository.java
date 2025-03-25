package vn.hcmute.appfood.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import vn.hcmute.appfood.entity.Food;

import java.util.Optional;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {
    Optional<Food> findById(Long id);
    Optional<Food> findByFoodName(String foodName);
    Optional<Food> findByCategoryId(Long categoryId);
}
