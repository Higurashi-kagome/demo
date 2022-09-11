package com.zhangmingge.printer.entity;

import lombok.*;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CheckWeightData implements Printable {

    /**
     * 显示重量(吨)
     */
    private Double accordWeight;
    /**
     * 皮重(吨)
     */
    private Double tareWeight;
    /**
     * 检斤时间
     */
    private Date weightDate;

    /**
     * 订单id
     */
    private String orderId;
    /**
     * 订单类型
     */
    private String orderType;
    /**
     * 扫码器设备编码
     */
    private String qrCode;
    /**
     * 车牌号
     */
    private String plateNo;
    /**
     * 检斤设备id
     */
    private String deviceId;
    /**
     * 检斤设备名称
     */
    private String deviceName;

    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        if (pageIndex > 0) {
            return NO_SUCH_PAGE;
        }
        Graphics2D g2d = (Graphics2D) graphics;
        g2d.setColor(Color.BLACK);
        g2d.setFont(new Font("Default", Font.PLAIN, 12));
        String[] lines = new String[]{
                "------------------------------------------------",
                "检斤时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(getWeightDate()),
                "检斤重量（吨）：" + getAccordWeight(),
                "检斤点：" + getDeviceName(),
                "车牌号：" + getPlateNo(),
                "订单类别：" + getOrderType(),
                "------------------------------------------------"};
        int startY = 2;
        for (String line : lines) {
            // 参数1：显示内容 参数2：横向偏移 参数3：纵向偏移
            g2d.drawString(line, 0, startY);
            startY += 20;
        }
        return PAGE_EXISTS;
    }
}
