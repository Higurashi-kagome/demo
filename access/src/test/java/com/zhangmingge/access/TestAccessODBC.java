package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * 测试用 ODBC 访问 Access
 * https://blog.csdn.net/zjsdrs/article/details/77542263
 */
public class TestAccessODBC {
    @Test
    public void test1() {
        Connection con = null;
        Statement statement = null;
        try {
//            Java 8 时移除（报错找不到类）
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            连接本地一：
//            String url = "jdbc:odbc:databaseName";//databaseName就是刚刚添加的数据源名称
//            连接本地二：
            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=C:/Users/liuhao/Desktop/zz_smart_logistics_collection.accdb";
//            访问局域网内的共享文件夹：
//            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=//DESKTOP-G0NLRJN/Reader_Web_Service_V1.0.0.7_20211112/zz_smart_logistics_collection.accdb";
            //没有用户名和密码的时候 user 和 password 为空
            con = DriverManager.getConnection(url, "", "");
            statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sl_device");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
                if (statement != null) statement.close();
            } catch (SQLException e) { }
        }
    }
}
