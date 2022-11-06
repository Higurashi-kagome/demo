package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.util.Properties;

/**
 * 测试用 ODBC 访问 Access
 * <a href="https://blog.csdn.net/zjsdrs/article/details/77542263">...</a>
 */
public class TestAccessODBC {
    String testFile = "test.accdb";
    /**
     * DESKTOP-EI1U34O - server name（Windows 访问共享文件夹时文件管理器上显示的服务器名字）<br>
     * access - share name（共享文件夹名）<br>
     * test.accdb - share file（共享的文件）
     */
    private final String UNCPath = "//DESKTOP-EI1U34O/access/test.accdb";

    @Test
    public void test1() {
        Connection con = null;
        Statement statement = null;
        try {
//            Java 8 时移除（报错找不到类）
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
//            连接本地一：
            String url = "jdbc:odbc:TestJava";//databaseName就是刚刚添加的数据源名称
//            连接本地二：
//            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=" + Utils.getResourceFilePath(testFile);
//            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=C:/Users/liuhao/Desktop/all_num.mdb";
            System.out.println(url);
//            访问局域网内的共享文件夹：
//            String url = "jdbc:odbc:Driver={Microsoft Access Driver  (*.mdb)};Dbq=" + UNCPath;
            //没有用户名和密码的时候 user 和 password 为空
            Properties prop = new Properties();
            prop.put("charset", "utf-8");
            prop.put("user", "");
            prop.put("password", "LX2008");
            con = DriverManager.getConnection(url, prop);
            statement = con.createStatement();
//            ResultSet rs = statement.executeQuery("SELECT * FROM sl_device");
            ResultSet rs = statement.executeQuery("SELECT * FROM num");
            while (rs.next()) {
                System.out.println(rs.getString(1));
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
                if (statement != null) statement.close();
            } catch (SQLException ignored) { }
        }
    }
}
