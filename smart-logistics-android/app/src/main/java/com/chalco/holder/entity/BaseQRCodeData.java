package com.chalco.holder.entity;

/**
 * 手持机二维码数据基类
 */
public class BaseQRCodeData {

    /**
     * QR+密钥通过 MD5 加密后的字符串
     */
    private String key;
    /**
     * 二维码类型
     */
    private Integer type;

    public BaseQRCodeData() {
    }

    public BaseQRCodeData(String key, Integer type) {
        this.key = key;
        this.type = type;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
