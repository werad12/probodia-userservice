package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Meal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MealRepository extends JpaRepository<Meal,Long> {
}
