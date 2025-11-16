package com.akriti.akriti.modules.setting.enums;

import lombok.Getter;

@Getter
public enum TimeZone {
    ASIS_KATHMANDU("ASIS/KATHMANDU"),
    ASIA_CALCUTTA("ASIA/CALCUTTA"),
    UTC("UTC");

    private final String timeZone;

    TimeZone(String timeZone){
        this.timeZone = timeZone;
    }

}
