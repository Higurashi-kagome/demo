package com.zhangmingge.gson.entity;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * 司机二维码信息
 */
public class DriverInfo2 implements Serializable {

    //运输记录id
    private String transportId;
    //运输物料id
    private String transportMaterialId;
    //订单物料id
    private String orderMaterialId;
    //订单编码
    private String orderCode;
    //运输物料名称
    private String materialName;
    //供应商名称
    private String supplierName;
    //车牌号
    private String plateNo;
    private String customerCode = "";
    /**
     * 订单类型：purchase | sale
     */
    private String orderType;
    private Integer type;
    /**
     * 检斤毛重
     */
    private BigDecimal grossWeight;

    public String getTransportId() {
        return transportId;
    }

    public void setTransportId(String transportId) {
        this.transportId = transportId;
    }

    public String getPlateNo() {
        return plateNo;
    }

    public void setPlateNo(String plateNo) {
        this.plateNo = plateNo;
    }

    public String getOrderType() {
        return orderType;
    }

    public void setOrderType(String orderType) {
        this.orderType = orderType;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getTransportMaterialId() {
        return transportMaterialId;
    }

    public void setTransportMaterialId(String transportMaterialId) {
        this.transportMaterialId = transportMaterialId;
    }

    public String getOrderMaterialId() {
        return orderMaterialId;
    }

    public void setOrderMaterialId(String orderMaterialId) {
        this.orderMaterialId = orderMaterialId;
    }

    public String getMaterialName() {
        return materialName;
    }

    public void setMaterialName(String materialName) {
        this.materialName = materialName;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public BigDecimal getGrossWeight() {
        return grossWeight;
    }

    public void setGrossWeight(BigDecimal grossWeight) {
        this.grossWeight = grossWeight;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    @Override
    public String toString() {
        return "DriverInfo{" +
                "transportId='" + transportId + '\'' +
                ", transportMaterialId='" + transportMaterialId + '\'' +
                ", orderMaterialId='" + orderMaterialId + '\'' +
                ", orderCode='" + orderCode + '\'' +
                ", materialName='" + materialName + '\'' +
                ", supplierName='" + supplierName + '\'' +
                ", plateNo='" + plateNo + '\'' +
                ", customerCode='" + customerCode + '\'' +
                ", orderType='" + orderType + '\'' +
                ", type=" + type +
                ", grossWeight=" + grossWeight +
                '}';
    }
}
