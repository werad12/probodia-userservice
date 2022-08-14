package com.probodia.userservice.api.entity.enums.converter;

import com.probodia.userservice.api.entity.enums.base.TimeTagCode;

import javax.persistence.Convert;
import javax.persistence.Converter;

@Converter(autoApply = true)
public class TimeTagCodeConverter extends AbstractBaseEnumConverter<TimeTagCode,String> {

    @Override
    protected TimeTagCode[] getValueList() {
        return TimeTagCode.values();
    }
}
