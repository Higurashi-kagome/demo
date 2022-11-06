package com.zhangmingge.access;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

/**
 * 火车动态衡数据同步任务（Access）
 *
 * @AUTHOR kd
 * @CREATE_DATE 2022/10/31
 */
@Slf4j
public class TrainAccessDataSyncTask2 {

    private static final String dbURL = "jdbc:odbc:driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=//10.32.103.18/mdb/all_num.mdb";

    public static void main(String[] args) {
        getAccessData("SELECT * FROM num");
    }

    public static void getAccessData(String sql) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
            connection = DriverManager.getConnection(dbURL, getPron());
            connection.setReadOnly(true);
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
        } catch (Exception e) {
            log.error("获取Access数据错误：{}", e.getMessage());
            e.printStackTrace();
        } finally {
            try {
                if (connection != null) connection.close();
                if (statement != null) statement.close();
                if (resultSet != null) resultSet.close();
            } catch (Exception e) {
                log.error("关闭连接失败：{}", e.getMessage());
                e.printStackTrace();
            }
        }
    }

    private static Properties getPron() {
        Properties properties = new Properties();
        properties.put("user", "");
        properties.put("password", "LX2008");
        properties.put("jackcessopener", "com.chalco.smart.train.JackcessOpener");
        return properties;
    }

}
