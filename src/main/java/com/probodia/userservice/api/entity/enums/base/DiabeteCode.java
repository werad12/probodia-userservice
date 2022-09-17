package com.probodia.userservice.api.entity.enums.base;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiabeteCode implements BaseEnumCode<String>{

    DIABETE_CODE_1("1형 당뇨"),
    DIABETE_CODE_2("2형 당뇨"),
    DIABETE_CODE_3("임신성 당뇨")
    ;

    private final String value;

    public static DiabeteCode findByValue(String codeValue){
        for(DiabeteCode code : DiabeteCode.values()){
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
