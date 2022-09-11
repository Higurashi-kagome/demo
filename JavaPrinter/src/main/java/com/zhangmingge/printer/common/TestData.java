package com.zhangmingge.printer.common;

import com.zhangmingge.printer.entity.CheckWeightData;

import java.util.Date;

/**
 * 测试数据
 */
public class TestData {
    public static CheckWeightData getCheckWeightData() {
        CheckWeightData checkWeightData = new CheckWeightData();
        checkWeightData.setWeightDate(new Date());
        checkWeightData.setAccordWeight(40.0);
        checkWeightData.setDeviceName("南门检斤点");
        checkWeightData.setPlateNo("湘B12345");
        checkWeightData.setOrderType("厂内普通");
        return checkWeightData;
    }
}
