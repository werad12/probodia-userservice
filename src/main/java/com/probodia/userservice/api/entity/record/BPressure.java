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

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DiscriminatorValue("PRESSURE")
@Table(name = "BPRESSURE")
public class BPressure extends Records {

    @NotNull
    @Column(name = "BLOOD_PRESSURE")
    private Integer bloodPressure;

}
