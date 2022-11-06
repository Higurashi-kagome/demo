package com.zhangmingge.access;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;
import java.util.List;
import java.util.Map;
import java.util.Properties;

/**
 * 火车动态衡数据同步任务（Access）
 *
 * @AUTHOR kd
 * @CREATE_DATE 2022/10/31
 */
@Slf4j
public class TrainAccessDataSyncTask {
    /**
     * 不直接关闭连接因为连接很耗时
     */
    Connection connection = null;

    //    private static final String dbURL = "jdbc:ucanaccess:////10.32.103.18/mdb/all_num.mdb";
    private static final String dbURL = "jdbc:ucanaccess://C:/Users/liuhao/Desktop/all_num.mdb";

    public static void main(String[] args) {
//        getAccessData("SELECT top 20 * FROM num ORDER BY 日时 DESC");
        TrainAccessDataSyncTask trainAccessDataSyncTask = new TrainAccessDataSyncTask();
        trainAccessDataSyncTask.getAccessData("SELECT * FROM num WHERE 日时 >= '20221030120541' AND 日时 <= '20221031120541' ORDER BY 日时 DESC;");
        try {
            Thread.sleep(5000);
            System.out.println("__________________________________________________________");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        trainAccessDataSyncTask.getAccessData("SELECT * FROM num WHERE 日时 >= '20221030120541' AND 日时 <= '20221031120541' ORDER BY 日时 DESC;");
    }

    public void getAccessData(String sql) {
        Statement statement = null;
        ResultSet resultSet = null;
        try {
            Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
            if (connection == null) connection = DriverManager.getConnection(dbURL, getPron());
            statement = connection.createStatement();
            resultSet = statement.executeQuery(sql);
            ResultSetMetaData metaData = resultSet.getMetaData();
            List<Map<String, Object>> data = Lists.newArrayList();
            //把ResultSet的所有记录添加到vector里
            while (resultSet.next()) {
                Map<String, Object> map = Maps.newHashMap();
                for (int i = 0; i < metaData.getColumnCount(); i++) {
                    String key = metaData.getColumnName(i + 1),
                        value = resultSet.getNString(i + 1);
                    map.put(key, value);
                }
                System.out.println(map);
                data.add(map);
            }
//            System.out.println(JSON.toJSONString(data));
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
        properties.put("jackcessopener", "com.zhangmingge.access.JackcessOpener");
        return properties;
    }

}
