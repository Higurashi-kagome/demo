package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class TestAccessUcanaccess {
    /**
     * 本地
     */
    @Test
    public void test1() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:ucanaccess://C:/Users/liuhao/Desktop/zz_smart_logistics_collection.accdb");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM sl_device");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }

    /**
     * 远程（共享文件夹）
     * https://stackoverflow.com/questions/33916380/how-to-access-a-database-file-on-a-remote-machine-via-ucanaccess
     */
    @Test
    public void test2() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:ucanaccess:////DESKTOP-G0NLRJN/Reader_Web_Service_V1.0.0.7_20211112/zz_smart_logistics_collection.accdb");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM sl_device");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}