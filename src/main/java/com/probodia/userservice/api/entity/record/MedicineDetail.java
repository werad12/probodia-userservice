package com.probodia.userservice.api.entity.record;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("MEDICINE_DETAIL")
@Table(name = "MEDICINE_DETAIL")
public class MedicineDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MEDICINE_DETAIL_ID")
    private Long id;

    @Column(name = "MEDICINE_NAME")
    @Size(max = 50)
    @NotNull
    private String medicineName;

    @Column(name = "MEDICINE_REAL_ID")
    private String medicineId;

    @Column(name = "MEDICINE_CNT")
    @NotNull
    private Integer medicineCnt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEDICINE_ID", insertable = false,
            updatable = false)
    @JsonIgnore
    private Medicine medicine;

}
