package com.chalco.holder.entity;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Utils;

/**
 * 直接收货二维码
 */
public class AcceptSalesQR extends BaseQRCodeData {

    public static AcceptSalesQR build(DriverInfoAndInput driverInfoAndInput) {
        if (driverInfoAndInput == null) return null;
        AcceptSalesQR qr = new AcceptSalesQR();
        qr.setImpurity(driverInfoAndInput.getImpurity());
        qr.setUnloadPlaceCode(driverInfoAndInput.getUnloadPlaceCode());
        qr.setRecipientCode(driverInfoAndInput.getRecipientCode());
        qr.setKey(Utils.getMD5Str(driverInfoAndInput.getDriverInfo().getTransportMaterialId() + Constant.UUID));
        return qr;
    }

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

    public String getRecipientCode() {
        return recipientCode;
    }

    public void setRecipientCode(String recipientCode) {
        this.recipientCode = recipientCode;
    }

}
