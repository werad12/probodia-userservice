package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.record.MedicineDetail;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MedicineDetailRepository;
import com.probodia.userservice.api.repository.record.MedicineRepository;
import com.probodia.userservice.api.vo.MedicineDetailVO;
import com.probodia.userservice.api.vo.MedicineResponseVO;
import com.probodia.userservice.api.vo.MedicineUpdateVO;
import com.probodia.userservice.api.vo.MedicineVO;
import com.probodia.userservice.converter.RecordConverter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

import static com.probodia.userservice.converter.RecordConverter.*;
import static com.probodia.userservice.converter.RecordConverter.convertMedicine;

@Service
@Slf4j
@RequiredArgsConstructor
public class MedicineService {

    private final MedicineRepository medicineRepository;
    private final MedicineDetailRepository medicineDetailRepository;

    private void setRecordBase(Records record, User user, String timeTag, String recordDate){
        record.setUser(user);
        record.setRecordDate(recordDate);
        record.setTimeTag(TimeTagCode.findByValue(timeTag));

    }


    public Optional<Medicine> findMedicineByUserAndId(User user, Long recordId) {
        return medicineRepository.findByUserAndId(user,recordId);
    }


    private Medicine saveMedicine(User user, String timeTag, String recordDate) {
        Medicine medicine = new Medicine();
        setRecordBase(medicine,user,timeTag,recordDate);

        return medicine;
    }


    private MedicineResponseVO saveMedicineDetail(Medicine medicine, List<MedicineDetailVO> medicineDetails) {

        medicineDetails.stream().forEach(m ->{
            MedicineDetail col = new MedicineDetail();

            col.setMedicineName(m.getMedicineName());
            col.setMedicineCnt(m.getMedicineCnt());

            if(m.getMedicineId()!=null){
                col.setMedicineId(m.getMedicineId());
            }

            medicine.getMedicineDetails().add(col);
            medicineDetailRepository.save(col);

        });

        Medicine savedMedicine = medicineRepository.save(medicine);

        return convertMedicine(savedMedicine);
    }

    private void deleteDetailByRecordId(Medicine medicine){
        medicineDetailRepository.deleteByMedicine(medicine);
    }

    @Transactional
    public MedicineResponseVO updateMedicine(Medicine medicine, MedicineUpdateVO requestRecord) {

        medicine.setTimeTag(TimeTagCode.findByValue(requestRecord.getTimeTag()));
        medicine.setRecordDate(requestRecord.getRecordDate());

        deleteDetailByRecordId(medicine);

        Set<MedicineDetail> medicineDetails = new HashSet<>();

        requestRecord.getMedicineDetails().stream().forEach(m ->{
            MedicineDetail detail = new MedicineDetail();
            detail.setMedicineId(m.getMedicineId());
            detail.setMedicineCnt(m.getMedicineCnt());
            detail.setMedicineName(m.getMedicineName());
            detail.setMedicine(medicine);

            medicineDetailRepository.save(detail);
            medicineDetails.add(detail);
        });

        medicine.setMedicineDetails(medicineDetails);

        Medicine saved = medicineRepository.save(medicine);

        return convertMedicine(saved);
    }

    @Transactional
    public Long deleteMedicine(Medicine medicine) {
        Long ret = medicine.getId();
        medicineRepository.delete(medicine);
        return ret;
    }


    @Transactional
    public MedicineResponseVO saveMedicine(User user, MedicineVO request) {

        Medicine savedMedicine = saveMedicine(user, request.getTimeTag(), request.getRecordDate());
        MedicineResponseVO retValue = saveMedicineDetail(savedMedicine, request.getMedicineDetails());

        return retValue;
    }
}
