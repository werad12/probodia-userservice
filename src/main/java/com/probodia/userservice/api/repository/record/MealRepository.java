package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Meal;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MealRepository extends JpaRepository<Meal,Long> {
    public Optional<Meal> findByUserAndId(User user,Long id);
}
