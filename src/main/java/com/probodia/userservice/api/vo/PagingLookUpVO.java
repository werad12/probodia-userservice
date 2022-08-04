package com.probodia.userservice.api.vo;

import com.probodia.userservice.utils.PageInfoUtil;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import java.util.List;

@ApiModel(value = "페이징 기록 조회 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingLookUpVO {

    @ApiModelProperty(value = "요청 정보가 들어있다.")
    private List<RecordLookUpVO> data;
    @ApiModelProperty(value = "페이징 정보가 들어있다.")
    private PageInfoUtil pageInfo;

}
