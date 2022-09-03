package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MedicineRepository;
import com.probodia.userservice.api.vo.MedicineResponseVO;
import com.probodia.userservice.api.vo.MedicineUpdateVO;
import com.probodia.userservice.api.vo.MedicineVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static com.probodia.userservice.converter.RecordConverter.convertMedicine;

@Service
@Slf4j
public class MedicineService {

    private MedicineRepository medicineRepository;

    @Autowired
    public MedicineService(MedicineRepository medicineRepository) {
        this.medicineRepository = medicineRepository;
    }

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }
    public MedicineResponseVO saveMedicine(MedicineVO requestRecord, User user) {

        Medicine medicine = new Medicine();
        setRecordBase(medicine,user, requestRecord.getTimeTag(), requestRecord.getRecordDate());
        if(requestRecord.getMedicineId()!=null)
            medicine.setMedicineId(requestRecord.getMedicineId());

        medicine.setMedicineCnt(requestRecord.getMedicineCnt());
        medicine.setMedicineName(requestRecord.getMedicineName());
        Medicine saved = medicineRepository.save(medicine);


        return convertMedicine(saved);
    }


    public Optional<Medicine> findMedicineByUserAndId(User user, Long recordId) {
        return medicineRepository.findByUserAndId(user,recordId);
    }

    public Long deleteMedicine(Medicine medicine) {
        medicineRepository.delete(medicine);
        return medicine.getId();
    }

    public MedicineResponseVO updateMedicine(Medicine medicine, MedicineUpdateVO requestRecord) {
        if(requestRecord.getMedicineId()!=null)
            medicine.setMedicineId(requestRecord.getMedicineId());
        medicine.setMedicineCnt(requestRecord.getMedicineCnt());
        medicine.setMedicineName(requestRecord.getMedicineName());
        medicine.setRecordDate(requestRecord.getRecordDate());
        medicine.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));

        Medicine saved = medicineRepository.save(medicine);

        return convertMedicine(saved);
    }
}
