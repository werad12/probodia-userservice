package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MedicineRepository extends JpaRepository<Medicine,Long> {
    public Optional<Medicine> findByUserAndId(User user,Long id);
}
