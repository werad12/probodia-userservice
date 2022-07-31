package com.probodia.userservice.api.entity.record;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("SUGAR")
@Table(name = "BSUGAR")
public class BSugar extends Records {

    @NotNull
    @Column(name = "BLOOD_SUGAR")
    private Integer bloodSugar;

}
