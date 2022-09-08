package com.probodia.userservice.api.entity.enums.converter;

import com.probodia.userservice.api.entity.enums.base.DiabeteCode;
import com.probodia.userservice.api.entity.enums.base.TimeTagCode;

public class DiabeteCodeConverter extends AbstractBaseEnumConverter<DiabeteCode,String> {

    @Override
    protected DiabeteCode[] getValueList() {
        return DiabeteCode.values();
    }
}
