package com.chalco.holder.entity;

import androidx.annotation.NonNull;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Validator;

import java.io.Serializable;
import java.util.Date;
import java.util.Locale;

public class OperationRecord implements Serializable {

    public static OperationRecord build(DriverInfoAndInput data){
        OperationRecord record = new OperationRecord();
        record.setOperationDate(Constant.SIMPLE_DATE_FORMAT.format(new Date()));
        DriverInfo driverInfo = data.getDriverInfo();
        record.setMaterialName(driverInfo.getMaterialName());
        record.setSupplierName(driverInfo.getSupplierName());
        record.setPlateNo(driverInfo.getPlateNo());
        record.setUnloadPlaceCode(data.getUnloadPlaceCode());
        record.setImpurity(data.getImpurity());
        record.setOrderCode(driverInfo.getOrderCode());
        record.setRecipientCode(data.getRecipientCode());
        record.setTransportMaterialId(driverInfo.getTransportMaterialId());
        record.setGrossWeight(driverInfo.getGrossWeight().doubleValue());
        record.setCustomerCode(driverInfo.getCustomerCode());
        return record;
    }

    /**
     * 主要用来在采样登记时根据收货卡内容构建采样登记历史记录
     * @param acceptCardData 收货卡数据类
     * @return 采样登记 OperationRecord（在外面设置 operationName）
     */
    public static OperationRecord build(AcceptCardData acceptCardData) {
        OperationRecord record = new OperationRecord();
        record.setTransportMaterialId(acceptCardData.getTransportMaterialId());
        record.setCustomerCode(acceptCardData.getCustomerCoder());
        record.setGrossWeight(acceptCardData.getGrossWeight());
        record.setPlateNo(acceptCardData.getPlateNo());
        record.setAcceptCardDate(acceptCardData.getDate());
        return record;
    }

    /**
     * 用来在抽样登记时根据采样卡内容构建抽样登记历史记录
     * @param sampleOrSampledCardData 正样卡数据类
     * @return 抽样登记 OperationRecord（在外面设置 operationName）
     */
    public static OperationRecord build(SampleOrSampledCardData sampleOrSampledCardData) {
        OperationRecord record = new OperationRecord();
        record.setTransportMaterialId(sampleOrSampledCardData.getTransportMaterialId());
        record.setCustomerCode(sampleOrSampledCardData.getCustomerCoder());
        record.setGrossWeight(sampleOrSampledCardData.getGrossWeight());
        record.setPlateNo(sampleOrSampledCardData.getPlateNo());
        record.setAcceptCardDate(sampleOrSampledCardData.getDate());
        return record;
    }

    public OperationRecord() {
        setOperationDate(Constant.SIMPLE_DATE_FORMAT.format(new Date()));
    }

    //订单编码
    private String orderCode;
    //运输物料名称
    private String materialName;
    //供应商名称
    private String supplierName;
    //车牌号
    private String plateNo;
    //卸货地点
    private String unloadPlaceCode;
    //扣杂
    private Double impurity;
    //取样人编号
    private String recipientCode = "";
    private String transportMaterialId = "";
    /**
     * 毛重
     */
    private Double grossWeight = null;

    //操作时间
    private String operationDate = "";
    {
        operationDate = Constant.SIMPLE_DATE_FORMAT.format(new Date());
    }
    /**
     * 收货卡时间（写收货卡的时间）。采样、抽样需要
     */
    private String acceptCardDate;
    //操作名称
    private String operationName;
    /**
     * 代号
     */
    private String customerCode = "";

    public String getAcceptCardDate() {
        return acceptCardDate;
    }

    public void setAcceptCardDate(String acceptCardDate) {
        this.acceptCardDate = acceptCardDate;
    }

    public void setGrossWeight(Double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public double getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(double grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getTransportMaterialId() {
        return transportMaterialId;
    }

    public void setTransportMaterialId(String transportMaterialId) {
        this.transportMaterialId = transportMaterialId;
    }

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getUnloadPlaceCode() {
        return unloadPlaceCode;
    }

    public void setUnloadPlaceCode(String unloadPlaceCode) {
        this.unloadPlaceCode = unloadPlaceCode;
    }

    public Double getImpurity() {
        return impurity;
    }

    public void setImpurity(Double impurity) {
        this.impurity = impurity;
    }

    public String getOperationDate() {
        return operationDate;
    }

    public void setOperationDate(String operationDate) {
        this.operationDate = operationDate;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if (Validator.isNotBlank(customerCode)) stringBuilder.append(customerCode);
        if (Validator.isNotBlank(plateNo)) stringBuilder.append("    " + plateNo);
        if (grossWeight != null) stringBuilder.append(String.format(Locale.US, "    %.2f吨", grossWeight));
        if (impurity != null) stringBuilder.append("\n扣杂：" + impurity + "%");
        if (Validator.isNotBlank(operationDate)) stringBuilder.append("\n时间：" + operationDate);
        if (Validator.isNotBlank(operationName)) stringBuilder.append("    " + operationName);
        if (Validator.isNotBlank(recipientCode)) stringBuilder.append("    " + recipientCode);
        if (Validator.isNotBlank(acceptCardDate)) stringBuilder.append("\n收货时间：" + acceptCardDate);
        return stringBuilder.toString().trim();
    }

}
