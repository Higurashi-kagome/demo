package com.chalco.holder.entity;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * 收货卡数据
 */
public class AcceptCardData implements Serializable {
    private String transportMaterialId = "";
    /**
     * 卡类型
     */
    private final Integer cardType = Constant.CardType.accept;
    /**
     * 取样计数器
     */
    private Integer cnt = 0;
    private String orderCode = "";
    private String plateNo = "";
    private String materialName = "";
    private double grossWeight = 0;
    //收货时间（写卡时间）
    private String date = "";
    /**
     * 代号
     */
    private String customerCoder = "";

    {
        date = Constant.SIMPLE_DATE_FORMAT.format(new Date());
    }

    public static AcceptCardData build(DriverInfo driverInfo){
        AcceptCardData acceptCardData = new AcceptCardData();
        if (driverInfo == null) return acceptCardData;
        acceptCardData.setTransportMaterialId(driverInfo.getTransportMaterialId());
        acceptCardData.setOrderCode(driverInfo.getOrderCode());
        acceptCardData.setPlateNo(driverInfo.getPlateNo());
        acceptCardData.setMaterialName(driverInfo.getMaterialName());
        acceptCardData.setGrossWeight(driverInfo.getGrossWeight().doubleValue());
        acceptCardData.setCustomerCoder(driverInfo.getCustomerCode());
        return acceptCardData;
    }

    public static AcceptCardData build(OperationRecord record) {
        AcceptCardData acceptCardData = new AcceptCardData();
        if (record == null) return acceptCardData;
        acceptCardData.setTransportMaterialId(record.getTransportMaterialId());
        acceptCardData.setOrderCode(record.getOrderCode());
        acceptCardData.setPlateNo(record.getPlateNo());
        acceptCardData.setMaterialName(record.getMaterialName());
        acceptCardData.setGrossWeight(record.getGrossWeight());
        acceptCardData.setCustomerCoder(record.getCustomerCode());
        return acceptCardData;
    }

    /**
     * TODO 单元测试
     * 根据收货卡字符串创建实体类
     * @param acceptCardStr 收货卡字符串
     * @return AcceptCardData 对象
     */
    public static AcceptCardData build(String acceptCardStr) {
        AcceptCardData acceptCardData = new AcceptCardData();
        if (!Validator.isAcceptCardData(acceptCardStr)) return acceptCardData;
        acceptCardStr = acceptCardStr.substring(1, acceptCardStr.length() - 1);
        String[] split = acceptCardStr.split(",");
        acceptCardData.setTransportMaterialId(split[0]);
        acceptCardData.setCnt(Integer.valueOf(split[2]));
        acceptCardData.setCustomerCoder(split[3]);
        acceptCardData.setPlateNo(split[4]);
        acceptCardData.setGrossWeight(Double.parseDouble(split[5]));
        acceptCardData.setDate(split[6]);
        return acceptCardData;
    }

    public Integer getCardType() {
        return cardType;
    }

    public Integer getCnt() {
        return cnt;
    }

    public void setCnt(Integer cnt) {
        this.cnt = cnt;
    }

    public String getCustomerCoder() {
        return customerCoder;
    }

    public void setCustomerCoder(String customerCoder) {
        this.customerCoder = customerCoder;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTransportMaterialId() {
        return transportMaterialId;
    }

    public void setTransportMaterialId(String transportMaterialId) {
        this.transportMaterialId = transportMaterialId;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    /**
     * TODO 单元测试
     * 获取写入到卡片中的字符串数据
     */
    public String getCardStr() {
        // 第三个数字 0 表示该收货卡被采样登记过 0 次
        return String.format(Locale.US, "{%s,%d,%d,%s,%s,%.2f,%s}",
                transportMaterialId, Constant.CardType.accept, cnt, customerCoder, plateNo, grossWeight, date);
    }

    /**
     * 收货部门查卡显示的信息
     */
    public String getAcceptInfoStr(){
        String sampleInfo = "";
        if (cnt == 0) sampleInfo = "未取样";
        if (cnt == 1) sampleInfo = "取样一次";
        if (cnt == 2) sampleInfo = "取样两次";
        return String.format(Locale.US, "%s    %.2f吨    %s\n%s", plateNo, grossWeight, sampleInfo, date);
    }

    /**
     * 采样部门查卡显示的信息
     */
    public String getSampleInfoStr() {
        String sampleInfo = "";
        if (cnt == 0) sampleInfo = "未取样";
        if (cnt == 1) sampleInfo = "取样一次";
        if (cnt == 2) sampleInfo = "取样两次";
        return String.format(Locale.US, "%s    %s    %.2f吨\n%s  %s", customerCoder, plateNo, grossWeight, date, sampleInfo);
    }
}
