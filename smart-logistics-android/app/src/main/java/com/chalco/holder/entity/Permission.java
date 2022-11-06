package com.chalco.holder.entity;

import org.litepal.crud.LitePalSupport;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 权限
 */
public class Permission extends LitePalSupport {
    /**
     * 以入口元素的 id 作为 pid
     */
    private int pid;
    private List<User> userList = new ArrayList<>();

    public Permission(int pid) {
        this.pid = pid;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public String toString() {
        return "Permission{" +
                "pid=" + pid +
                ", userList=" + userList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Permission that = (Permission) o;
        return pid == that.pid && Objects.equals(userList, that.userList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pid, userList);
    }
}
