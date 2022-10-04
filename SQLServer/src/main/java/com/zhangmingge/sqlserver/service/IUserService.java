package com.zhangmingge.sqlserver.service;

import com.zhangmingge.sqlserver.entity.User;

import java.util.List;

public interface IUserService {
    public List<User> getAllUsers();

    public int addUser(User user);

    public int deleteUser(User user);
}
