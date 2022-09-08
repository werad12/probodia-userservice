package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.record.MealDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface MealDetailRepository extends JpaRepository<MealDetail,Long> {
    void deleteByMeal(Meal meal);
}
