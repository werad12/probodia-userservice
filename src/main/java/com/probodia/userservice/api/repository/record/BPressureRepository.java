package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.BPressure;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.lang.management.BufferPoolMXBean;
import java.util.Optional;

@Repository
public interface BPressureRepository extends JpaRepository<BPressure,Long> {

    public Optional<BPressure> findByUserAndId(User user,Long id);

}
