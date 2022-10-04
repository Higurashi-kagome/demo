package com.zhangmingge.sqlserver;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.zhangmingge.sqlserver")
public class SqlServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SqlServerApplication.class, args);
    }

}
