package com.nftworlds.avatarselector.enums;

public enum AvatarAge {

    OLD("old"),
    NEW("new"),
    HD("hd");

    private final String name;

    AvatarAge(String name){
        this.name = name;
    }

    public String getName(){
        return name;
    }
}
