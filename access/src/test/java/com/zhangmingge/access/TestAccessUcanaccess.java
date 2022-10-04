package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class TestAccessUcanaccess {
    @Test
    public void test() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:ucanaccess://C:/Users/liuhao/Desktop/zz_smart_logistics_collection.accdb");
        Statement s = conn.createStatement();
        ResultSet rs = s.executeQuery("SELECT * FROM sl_device");
        while (rs.next()) {
            System.out.println(rs.getString(1));
        }
    }
}