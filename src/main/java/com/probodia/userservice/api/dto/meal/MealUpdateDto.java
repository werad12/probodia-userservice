package com.probodia.userservice.api.dto.meal;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ApiModel(value = "음식 수정 모델")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class MealUpdateDto {

    @ApiModelProperty(value = "음식 ID", required = true,example = "123123")
    @NotNull(message = "Record Id cannot be null")
    private Long recordId;

    @ApiModelProperty(value = "시간 태그", required = true,example = "아침")
    @NotNull(message = "Time tag cannot be null")
    @Size(max = 10, message="Time tag not be more than 10 charaters")
    private String timeTag;

    @ApiModelProperty(value = "종류에 따른 음식의 정보", required = true)
    @NotNull(message = "Meal Detail cannot be null")
    private List<MealDetailDto> mealDetails;
    @ApiModelProperty(value = "기록 시간", example = "2017-11-12 13:11:34")
    @NotNull(message = "Record time cannot be null")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private String recordDate;
}
