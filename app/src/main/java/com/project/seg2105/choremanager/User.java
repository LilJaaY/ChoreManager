package com.project.seg2105.choremanager;

/**
 * Created by jalilcompaore on 06/11/17.
 */

public class User {
    private int id;
    private String name;
    private String password;
    private String avatar;
    private String accountRecovery;

    public User(String name, String password, String avatar) {
        this.avatar = avatar;
        this.password = password;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAccountRecovery(){ return accountRecovery; }

    public void setAccountRecovery(String accountRecovery){this.accountRecovery = accountRecovery;}
}
