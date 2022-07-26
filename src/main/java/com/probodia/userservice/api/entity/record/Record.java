package com.probodia.userservice.api.entity.record;

import com.probodia.userservice.api.entity.base.BaseTime;
import com.probodia.userservice.api.entity.user.User;
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
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "RTYPE")
@Table(name = "RECORD")
public class Record extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECORD_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_SEQ")
    private User user;

    @Column(name = "TIME_TAG", length = 10)
    @NotNull
    @Size(max = 10)
    private String timeTag;

    @Column(name = "RTYPE",insertable = false,updatable = false)
    protected String type;

}
