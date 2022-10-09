package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class TestAccessODBC {
    @Test
    public void test1() {
        try {
//            Java 8 时移除
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            方法一（连接本地）：
//            String url = "jdbc:odbc:databaseName";//databaseName就是刚刚添加的数据源名称
//            方法二（连接本地）：
            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=C:/Users/liuhao/Desktop/zz_smart_logistics_collection.accdb";
//            方法三（远程-共享文件夹）：
//            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=//DESKTOP-G0NLRJN/Reader_Web_Service_V1.0.0.7_20211112/zz_smart_logistics_collection.accdb";
            //没有用户名和密码的时候直接为空
            Connection con = DriverManager.getConnection(url, "", "");
            Statement s = con.createStatement();
            ResultSet rs = s.executeQuery("SELECT * FROM sl_device");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
