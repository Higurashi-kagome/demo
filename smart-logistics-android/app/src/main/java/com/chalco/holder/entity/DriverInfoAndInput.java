package com.chalco.holder.entity;

import androidx.annotation.NonNull;

import com.chalco.holder.common.Validator;

import java.io.Serializable;
import java.util.Locale;

/**
 * 【扫描到的司机信息+手持机使用者输入的信息】所对应的数据类
 */
public class DriverInfoAndInput implements Serializable {

    private DriverInfo driverInfo;

    /**
     * 卸货地点编号
     */
    private String unloadPlaceCode;
    /**
     * 扣除杂质数量
     */
    private Double impurity;
    /**
     * 取样人编号（单独收货没有）
     */
    private String recipientCode = "";

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

    public DriverInfoAndInput() {
    }

    public Double getImpurity() {
        return impurity;
    }

    public void setImpurity(Double impurity) {
        this.impurity = impurity;
    }

    public String getUnloadPlaceCode() {
        return unloadPlaceCode;
    }

    public void setUnloadPlaceCode(String unloadPlaceCode) {
        this.unloadPlaceCode = unloadPlaceCode;
    }

    public DriverInfo getDriverInfo() {
        return driverInfo;
    }

    public void setDriverInfo(DriverInfo driverInfo) {
        this.driverInfo = driverInfo;
    }

    @NonNull
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("卸货点：" + unloadPlaceCode);
        if (Validator.isNotBlank(recipientCode)) stringBuilder.append("    取样人：" + recipientCode);
        String s = String.format(Locale.US, "\n扣杂：%s%%", impurity);
        stringBuilder.append(s);
        return stringBuilder.toString();
    }

    public String toString2() {
        if (driverInfo == null) return toString();
        else return driverInfo + "\n" + this;
    }

    public String toAcceptInfoStr() {
        if (driverInfo == null) return toString();
        else return driverInfo.toAcceptInfoStr() + "\n" + this;
    }
}
