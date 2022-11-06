package com.zhangmingge.gson.demo;

import com.google.gson.Gson;
import com.zhangmingge.gson.entity.DriverInfo2;

/**
 * 演示 json 中存在多余地键时，转实体类不会出错<br>
 * 同时注意到，实体类中有，json 中没有的值将会是默认值
 */
public class Demo02 {
    public static void main(String[] args) {
        String json = "{\"orderCode\":\"CG2022100024\",\"orderId\":\"9cfa358a58864334990f6ff29c68a02b\",\"plateNo\":\"湘A66666\",\"orderMaterialId\":\"4b2dde1506dc43bfbc9aba3b2fcfad88\",\"transportMaterialId\":\"1235\",\"transportId\":\"a450e210218b4a70ba1fa947b1d0abd6\",\"materialName\":\"铝矿石\",\"grossWeight\":\"48.78\",\"supplierName\":\"方山8\",\"customerCode\":\"1b\",\"isVaria\":\"true\",\"isQualified\":\"false\"}";
        DriverInfo2 driverInfo = new Gson().fromJson(json, DriverInfo2.class);
        System.out.println(driverInfo);
    }
}
