package com.zhangmingge.access;

import org.junit.jupiter.api.Test;

import java.sql.*;

public class TestAccessRmi {
    @Test
    public void executeQuery() {
        Connection con = null;
        Statement stmt = null;
        try {
            //注册数据提供程序
            Class.forName("org.objectweb.rmijdbc.Driver").newInstance();
            //生产地址
            String url = "jdbc:rmi://10.32.103.18/jdbc:odbc:all_num";
            //建立连接
            con = DriverManager.getConnection(url, "admin", "Lx2008");
            stmt = con.createStatement();
//            ResultSet rst = stmt.executeQuery("select * from num");
//            if (rst != null) {
//                while (rst.next()) {
//                    String id = rst.getInt("id") + "";
////                    String name = new String(rst.getBytes("title"), "gbk");
////                    String time = rst.getDate("date").toString();
////                    String filePath = rst.getString("url");
//                    System.out.println(id);
//                }
//            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (con != null) con.close();
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
