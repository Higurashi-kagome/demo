package com.zhangmingge.sqlserver;

import java.sql.*;

public class TestSQLServerJDBC {
    //驱动路径
    private static final String DBDRIVER = "com.microsoft.sqlserver.jdbc.SQLServerDriver";
    //数据库地址
    private static final String DBURL = "jdbc:sqlserver://localhost:1433;DataBaseName=test;encrypt=true;trustServerCertificate=true";
    //数据库登录用户名
    private static final String DBUSER = "sa";
    //数据库用户密码
    private static final String DBPASSWORD = "123";
    //数据库连接
    public static Connection conn = null;
    //数据库操作
    public static Statement stmt = null;
    //数据库查询结果集
    public static ResultSet rs = null;

    //程序入口函数
    public static void main(String[] args) {
        try {
            //加载驱动程序
            Class.forName(DBDRIVER);
            //连接数据库
            conn = DriverManager.getConnection(DBURL, DBUSER, DBPASSWORD);
            //实例化Statement对象
            stmt = conn.createStatement();
            rs = stmt.executeQuery("select * from students");
            while (rs.next()) {
                String Id = rs.getString(1);
                String Name = rs.getString(2);
                System.out.print("学号:" + Id);
                System.out.println(" 姓名:" + Name);
                System.out.println("------------------------------------");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
