package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

/**
 * 测试用 Ucanaccess 访问 Access
 */
public class TestAccessUcanaccess {
    /**
     * 访问本地
     */
    @Test
    public void test1() {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:ucanaccess://C:/Users/liuhao/Desktop/zz_smart_logistics_collection.accdb");
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sl_device");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {}
        }
    }

    /**
     * 访问远程（共享文件夹）
     * https://stackoverflow.com/questions/33916380/how-to-access-a-database-file-on-a-remote-machine-via-ucanaccess
     */
    @Test
    public void test2() {
        Connection conn = null;
        Statement statement = null;
        try {
            conn = DriverManager.getConnection(
                    "jdbc:ucanaccess:////DESKTOP-G0NLRJN/Reader_Web_Service_V1.0.0.7_20211112/zz_smart_logistics_collection.accdb");
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sl_device");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
            } catch (SQLException e) {}
        }
    }
}