package com.probodia.userservice.api.dto.recordview;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiParam;
import lombok.*;

import javax.validation.constraints.NotNull;
import java.util.List;


@ApiModel(value = "페이징 필터 기록 조회 요청 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class PagingFilterRequestDto {


    @ApiParam(value = "필터링할 타입들", required = true,example = "MEAL")
    @NotNull(message = "Filter type cannot be null")
    List<String> filterType;

    @ApiParam(value = "페이지 번호", required = true,example = "12")
    @NotNull(message = "Page number cannot be null")
    Integer page;
    @ApiParam(value = "한 페이지의 사이즈", required = true,example = "123")
    @NotNull(message = "Paging size cannot be null")
    Integer size;
}
