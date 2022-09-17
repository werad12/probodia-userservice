package com.probodia.userservice.api.entity.record;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.probodia.userservice.api.entity.base.BaseTime;
import com.probodia.userservice.api.entity.enums.base.TimeTagCode;
import com.probodia.userservice.api.entity.enums.converter.TimeTagCodeConverter;
import com.probodia.userservice.api.entity.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "RTYPE")
@Table(name = "RECORD")
public class Records extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @Convert(converter = TimeTagCodeConverter.class)
    @Column(name = "TIME_TAG")
    @NotNull
    private TimeTagCode timeTag;

    @Column(name = "RTYPE",insertable = false,updatable = false)
    protected String type;

    @Column(name = "RECORD_DATE",length = 40)
    @NotNull
    @Size(max = 40)
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss",timezone = "Asia/Seoul")
    private String recordDate;

}
