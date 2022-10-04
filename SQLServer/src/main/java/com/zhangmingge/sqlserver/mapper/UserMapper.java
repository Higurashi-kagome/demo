package com.zhangmingge.sqlserver.mapper;

import com.zhangmingge.sqlserver.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserMapper {
    List<User> getAllUsers();
    int addUser( User user );
    int deleteUser( User user );
}
