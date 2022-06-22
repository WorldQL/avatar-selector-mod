package com.nftworlds.avatarselector.enums;

public enum AvatarType {
    CLASSIC("classic"),
    SLIM("slim");

    private final String name;

    AvatarType(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
