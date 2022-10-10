package com.zhangmingge.sqlserver.service;

import com.zhangmingge.sqlserver.entity.User;

import java.util.List;

public interface IUserService {
    List<User> getAllUsers();

    int addUser(User user);

    int deleteUser(User user);
}
