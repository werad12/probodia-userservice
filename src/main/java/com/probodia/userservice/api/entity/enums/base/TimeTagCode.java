package com.probodia.userservice.api.entity.enums.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeTagCode implements BaseEnumCode<String>{
    MORNING_BEFORE("아침 식전"),
    MORNING_AFTER("아침 식후"),
    NOON_BEFORE("점심 식전"),
    NOON_AFTER("점심 식후"),
    NIGHT_BEFORE("저녁 식전"),
    NIGHT_AFTER("저녁 식후"),
    ETC("기타"),
    MORNING("아침"),
    NOON("점심"),
    NIGHT("저녁")
    ;

    private final String value;

    public static TimeTagCode findByValue(String codeValue){
        for(TimeTagCode code : TimeTagCode.values()){
            if(code.getValue().equals(codeValue))
                return code;
        }
        throw new IllegalArgumentException(String.format("Unsupported type for %s.", codeValue));
    }

    @Override
    public String getValue() {
        return value;
    }
}
