package com.probodia.userservice.api.entity.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("MEDICINE")
@Table(name = "MEDICINE")
public class Medicine extends Records{

    @Column(name = "MEDICINE_NAME")
    @Size(max = 50)
    @NotNull
    private String medicineName;

    @Column(name = "MEDICINE_ID")
    private String medicineId;

    @Column(name = "MEDICINE_CNT")
    @NotNull
    private Integer medicineCnt;

}
