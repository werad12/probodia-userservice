package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.record.BSugar;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BSugarRepository extends JpaRepository<BSugar,Long> {
    Optional<BSugar> findByUserAndId(User user, Long recordId);
}
