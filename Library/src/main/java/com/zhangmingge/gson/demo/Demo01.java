package com.zhangmingge.gson.demo;

import com.google.gson.Gson;
import com.zhangmingge.gson.entity.DriverInfo;

/**
 * 演示将字符串值转成对应地属性值（比如字符串 true 转成布尔值）
 */
public class Demo01 {
    public static void main(String[] args) {
        String json = "{\"orderCode\":\"CG2022100024\",\"orderId\":\"9cfa358a58864334990f6ff29c68a02b\",\"plateNo\":\"湘A66666\",\"orderMaterialId\":\"4b2dde1506dc43bfbc9aba3b2fcfad88\",\"transportMaterialId\":\"1235\",\"transportId\":\"a450e210218b4a70ba1fa947b1d0abd6\",\"materialName\":\"铝矿石\",\"grossWeight\":\"48.78\",\"supplierName\":\"方山8\",\"customerCode\":\"1b\",\"isVaria\":\"true\",\"isQualified\":\"false\"}";
        DriverInfo driverInfo = new Gson().fromJson(json, DriverInfo.class);
        System.out.println(driverInfo);
    }
}
