package com.akriti.akriti.modules.setting.enums;

public enum Language {
    EN("en"),
    NP("np"),
    IN("in");

    private final String language;

    Language(String language){
        this.language = language;
    }

    public String getLanguage(){
        return language;
    }
}
