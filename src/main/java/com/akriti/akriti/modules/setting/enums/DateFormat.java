package com.akriti.akriti.modules.setting.enums;

import lombok.Getter;

@Getter
public enum DateFormat {
    DD_MM_YYYY("DD-MM-YYYY"),
    MM_DD_YYYY("MM-DD-YYYY"),
    YYYY_MM_DD("YYYY-MM-DD");

    private final String pattern;

    DateFormat(String pattern){
        this.pattern = pattern;
    }

}
