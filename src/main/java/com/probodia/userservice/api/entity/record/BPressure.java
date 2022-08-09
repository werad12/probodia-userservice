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
    @Column(name = "MAX_PRESSURE")
    private Integer maxBloodPressure;

    @NotNull
    @Column(name = "MIN_PRESSURE")
    private Integer minBloodPressure;

    @NotNull
    @Column(name = "HEARTBEAT")
    private Integer heartBeat;


}
