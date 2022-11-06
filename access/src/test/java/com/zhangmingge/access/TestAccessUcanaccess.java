package com.zhangmingge.access;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Properties;

/**
 * 测试用 Ucanaccess 访问 Access
 */
public class TestAccessUcanaccess {

    private final String testFile = "test.accdb";

    /**
     * DESKTOP-EI1U34O - server name（Windows 访问共享文件夹时文件管理器上显示的服务器名字）<br>
     * access - share name（共享文件夹名）<br>
     * test.accdb - share file（共享的文件）
     */
//    private final String UNCPath = "//DESKTOP-EI1U34O/access/test.accdb";
    private final String UNCPath = "//10.32.103.18/mdb/all_num.mdb";

    /**
     * 访问本地
     */
    @Test
    public void test1() {
        String testFilePath = Utils.getResourceFilePath(testFile);
        if (testFilePath == null) return;
        Connection conn = null;
        Statement statement = null;
        try {
            conn = DriverManager.getConnection("jdbc:ucanaccess://" + testFilePath);
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM sl_device");
            while (rs.next()) {
                String column1 = rs.getString(1);
                System.out.println("column1 = " + column1);
                Date create_date = rs.getDate("create_date");
                System.out.println("create_date = " + create_date);
                Time create_date_time = rs.getTime("create_date");
                System.out.println("create_date_time = " + create_date_time);
                int del_flag = rs.getInt("del_flag");
                System.out.println("del_flag = " + del_flag);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
            } catch (SQLException ignored) {}
        }
    }

    /**
     * 访问远程（共享文件夹）
     * <a href="https://stackoverflow.com/questions/33916380/how-to-access-a-database-file-on-a-remote-machine-via-ucanaccess">...</a>
     */
    @Test
    public void test2() {
        Connection conn = null;
        Statement statement = null;
        try {
            Properties prop = new Properties();
            prop.put("user", "");
            prop.put("password", "LX2008");
            // 不需要密码时不传入数据
//            conn = DriverManager.getConnection("jdbc:ucanaccess://" + UNCPath);
            // 需要密码时设置 jackcessOpener 属性，并传入用户名和密码
//            conn = DriverManager.getConnection(
//                    "jdbc:ucanaccess://" + UNCPath + ";jackcessOpener=com.zhangmingge.access.CryptCodecOpener", prop);
            statement = conn.createStatement();
            ResultSet rs = statement.executeQuery("SELECT * FROM num");
            while (rs.next()) {
                System.out.println(rs.getString(2));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (conn != null) conn.close();
                if (statement != null) statement.close();
            } catch (SQLException ignored) {}
        }
    }
}