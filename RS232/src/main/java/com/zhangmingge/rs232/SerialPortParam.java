package com.zhangmingge.rs232;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * 串口入参
 *
 * @author yly
 * @date 2020/8/6
 */
@Getter
@Setter
@Builder
public class SerialPortParam implements Serializable {

    private static final long serialVersionUID = 3109397017411925969L;
    /**
     * 串口号 如：COM255
     */
    private String serialNumber;
    /**
     * 波特率
     */
    private Integer baudRate;
    /**
     * 校验位
     */
    private Integer checkoutBit;
    /**
     * 数据位
     */
    private Integer dataBit;
    /**
     * 停止位
     */
    private Integer stopBit;

}
