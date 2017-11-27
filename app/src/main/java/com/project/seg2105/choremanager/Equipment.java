package com.project.seg2105.choremanager;

public class Equipment {

    private String key;
    private String name;
    private String avatar;

    public Equipment(){}

    public Equipment(String name, String avatar){
        this.name = name;
        this.avatar = avatar;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getKey(){
        return key;
    }

    public String getName(){
        return name;
    }

    public String getAvatar(){
        return avatar;
    }
}
