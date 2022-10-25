package com.probodia.userservice.api.service.record;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.record.Medicine;
import com.probodia.userservice.api.entity.record.MedicineDetail;
import com.probodia.userservice.api.entity.record.Records;
import com.probodia.userservice.api.entity.user.User;
import com.probodia.userservice.api.repository.record.MedicineDetailRepository;
import com.probodia.userservice.api.repository.record.MedicineRepository;
import com.probodia.userservice.api.dto.medicine.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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


    private MedicineResponseDto saveMedicineDetail(Medicine medicine, List<MedicineDetailDto> medicineDetails) {

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
    public MedicineResponseDto updateMedicine(Medicine medicine, MedicineUpdateDto requestRecord) {

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
    public MedicineResponseDto saveMedicine(User user, MedicineDto request) {

        Medicine savedMedicine = saveMedicine(user, request.getTimeTag(), request.getRecordDate());
        MedicineResponseDto retValue = saveMedicineDetail(savedMedicine, request.getMedicineDetails());

        return retValue;
    }



    private static MedicineResponseDto convertMedicine(Medicine saved){
        List<MedicineDetailResponseDto> detailConverted = new ArrayList<>();
        saved.getMedicineDetails().stream().forEach(s -> detailConverted.add(medicineDetailConvert(s)));

        return MedicineResponseDto.builder().recordId(saved.getId())
                .medicineDetails(detailConverted)
                .timeTag(saved.getTimeTag().getValue())
                .recordDate(saved.getRecordDate())
                .build();
    }

    private static MedicineDetailResponseDto medicineDetailConvert(MedicineDetail saved){
        return MedicineDetailResponseDto.builder().medicineDetailId(saved.getId())
                .medicineCnt(saved.getMedicineCnt())
                .medicineName(saved.getMedicineName())
                .medicineId(saved.getMedicineId())
                .build();
    }
}
