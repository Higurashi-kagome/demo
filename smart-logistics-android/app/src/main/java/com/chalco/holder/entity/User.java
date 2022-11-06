package com.chalco.holder.entity;

import org.litepal.annotation.Column;
import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户
 */
public class User extends LitePalSupport {

    @Column(unique = true, nullable = false)
    private String name;
    private String password;
    private List<Permission> permissionList = new ArrayList<>();

    public User() {
    }

    public User(String name, String password) {
        this.name = name;
        this.password = password;
    }



    public List<Permission> getPermissionList() {
        return permissionList;
    }

    public void setPermissionList(List<Permission> permissionList) {
        this.permissionList = permissionList;
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

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", permissionList=" + permissionList +
                '}';
    }

}
