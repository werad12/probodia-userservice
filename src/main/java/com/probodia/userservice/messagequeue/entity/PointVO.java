package com.probodia.userservice.messagequeue.entity;

import lombok.*;

import java.io.Serializable;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PointVO implements Serializable {

    private static final long serialVersionUID = 6529685098267757690L;
    String userId;
    Integer point;
}
