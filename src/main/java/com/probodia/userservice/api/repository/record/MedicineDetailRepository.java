package com.probodia.userservice.api.repository.record;

import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.record.MedicineDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MedicineDetailRepository extends JpaRepository<MedicineDetail, Long> {
    void deleteByMedicine(Medicine medicine);
}
