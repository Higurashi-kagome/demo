package com.chalco.holder.entity;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

/**
 * 正样卡/被抽样卡/抽样卡数据
 */
public class SampleOrSampledCardData implements Serializable {
    private String transportMaterialId = "";
    private String orderCode = "";
    private String plateNo = "";
    private String materialName = "";
    private double grossWeight = 0;
    /**
     * 代号
     */
    private String customerCoder = "";
    //收货时间
    private String date = "";

    {
        date = Constant.SIMPLE_DATE_FORMAT.format(new Date());
    }

    /**
     * 默认为正样卡
     */
    private Integer cardType = Constant.CardType.sample;

    /**
     * TODO 单元测试
     * @param dataInCard 正样卡/抽样卡/被抽样卡数据
     * @return 由字符串数据构建的对象
     */
    public static SampleOrSampledCardData build(String dataInCard) {
        SampleOrSampledCardData sampleOrSampledCardData = new SampleOrSampledCardData();
        if (!Validator.isNotBlank(dataInCard) || dataInCard.length() < 4) return sampleOrSampledCardData;
        dataInCard = dataInCard.replace("}[", ",");
        dataInCard = dataInCard.replaceAll("[\\{\\}\\[\\]]", "");
        String[] split = dataInCard.split(",");
        if (split.length != 6) return sampleOrSampledCardData;
        sampleOrSampledCardData.setTransportMaterialId(split[0]);
        sampleOrSampledCardData.setCardType(Integer.parseInt(split[1]));
        sampleOrSampledCardData.setCustomerCoder(split[2]);
        sampleOrSampledCardData.setPlateNo(split[3]);
        sampleOrSampledCardData.setGrossWeight(Double.parseDouble(split[4]));
        sampleOrSampledCardData.setDate(split[5]);
        return sampleOrSampledCardData;
    }

    /**
     * 从直接收货历史记录构建正样卡数据、从采样登记历史记录构建正样卡数据、从抽样登记历史记录构建抽样卡数据
     * @param record 直接收货历史记录/采样登记历史记录/抽样登记历史记录
     * @return 正样卡/抽样卡数据
     */
    public static SampleOrSampledCardData build(OperationRecord record){
        SampleOrSampledCardData sampleOrSampledCardData = new SampleOrSampledCardData();
        if (record == null) return sampleOrSampledCardData;
        sampleOrSampledCardData.setTransportMaterialId(record.getTransportMaterialId());
        sampleOrSampledCardData.setOrderCode(record.getOrderCode());
        sampleOrSampledCardData.setPlateNo(record.getPlateNo());
        sampleOrSampledCardData.setMaterialName(record.getMaterialName());
        sampleOrSampledCardData.setGrossWeight(record.getGrossWeight());
        sampleOrSampledCardData.setCustomerCoder(record.getCustomerCode());
        String operationName = record.getOperationName();
        if (Constant.Commands.CARD_TRANSFER.equals(operationName)) {
            sampleOrSampledCardData.setDate(record.getAcceptCardDate());
        }
        if (Constant.Commands.SAMPLING.equals(operationName)) {
            sampleOrSampledCardData.setDate(record.getAcceptCardDate());
        }
        if (Constant.Commands.ACCEPT_SALES1.equals(operationName)) {
            sampleOrSampledCardData.setDate(record.getOperationDate());
        }
        return sampleOrSampledCardData;
    }

    /**
     * TODO 保存的历史记录时间和卡中写的收货时间并不完全一致
     * 从直接收货、二次确认的 DriverInfo 构建写入到正样卡中的 SampleOrSampledCardData
     */
    public static SampleOrSampledCardData build(DriverInfo driverInfo){
        SampleOrSampledCardData sampleOrSampledCardData = new SampleOrSampledCardData();
        if (driverInfo == null) return sampleOrSampledCardData;
        sampleOrSampledCardData.setTransportMaterialId(driverInfo.getTransportMaterialId());
        sampleOrSampledCardData.setOrderCode(driverInfo.getOrderCode());
        sampleOrSampledCardData.setPlateNo(driverInfo.getPlateNo());
        sampleOrSampledCardData.setMaterialName(driverInfo.getMaterialName());
        sampleOrSampledCardData.setGrossWeight(driverInfo.getGrossWeight().doubleValue());
        sampleOrSampledCardData.setCustomerCoder(driverInfo.getCustomerCode());
        return sampleOrSampledCardData;
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

    public Integer getCardType() {
        return cardType;
    }

    public void setCardType(Integer cardType) {
        this.cardType = cardType;
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
        return String.format(Locale.US, "{%s,%d}[%s,%s,%.2f,%s]",
                transportMaterialId, cardType, customerCoder, plateNo, grossWeight, date);
    }

    public String getInfoStr() {
        return String.format(Locale.US, "%s    %s    %.2f吨\n%s", customerCoder, plateNo, grossWeight, date);
    }
}
