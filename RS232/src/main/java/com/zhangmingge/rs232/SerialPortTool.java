package com.zhangmingge.rs232;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import gnu.io.*;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
/**
 * 串口工具类
 */
@Slf4j
public class SerialPortTool {
    /**
     * 查找电脑上所有可用 com 端口
     *
     * @return 可用端口名称列表，没有时列表为空
     */
    public static List<String> findSystemAllComPort() {
        /*
         *  getPortIdentifiers：获得电脑主板当前所有可用串口
         */
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        List<String> portNameList = new ArrayList<>();
        /*
         *  将可用串口名添加到 List 列表
         */
        while (portList.hasMoreElements()) {
            String portName = portList.nextElement().getName();//名称如 COM1、COM2....
            portNameList.add(portName);
        }
        return portNameList;
    }
 
    /**
     * 打开电脑上指定的串口
     *
     * @param portName 端口名称，如 COM1，为 null 时，默认使用电脑中能用的端口中的第一个
     * @param b        波特率(baudrate)，如 9600
     * @param d        数据位（datebits），如 SerialPort.DATABITS_8 = 8
     * @param s        停止位（stopbits），如 SerialPort.STOPBITS_1 = 1
     * @param p        校验位 (parity)，如 SerialPort.PARITY_NONE = 0
     * @return 打开的串口对象，打开失败时，返回 null
     */
    public static SerialPort openComPort(String portName, int b, int d, int s, int p) {
        CommPort commPort = null;
        try {
            //当没有传入可用的 com 口时，默认使用电脑中可用的 com 口中的第一个
            if (StrUtil.isBlank(portName)) {
                List<String> comPortList = findSystemAllComPort();
                if (CollUtil.isNotEmpty(comPortList)) {
                    portName = comPortList.get(0);
                }
            }
            log.info("开始打开串口：portName={},baudrate={},datebits={},stopbits={},parity={}", portName, b, d, s, p);
            //通过端口名称识别指定 COM 端口
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            /*
             * open(String TheOwner, int i)：打开端口
             * TheOwner 自定义一个端口名称，随便自定义即可
             * i：打开的端口的超时时间，单位毫秒，超时则抛出异常：PortInUseException if in use.
             * 如果此时串口已经被占用，则抛出异常：gnu.io.PortInUseException: Unknown Application
             */
            commPort = portIdentifier.open(portName, 5000);
            /*
             * 判断端口是不是串口
             * public abstract class SerialPort extends CommPort
             */
            if (commPort instanceof SerialPort) {
                SerialPort serialPort = (SerialPort) commPort;
                /*
                 * 设置串口参数：setSerialPortParams( int b, int d, int s, int p )
                 * b：波特率（baudrate）
                 * d：数据位（datebits），SerialPort 支持 5,6,7,8
                 * s：停止位（stopbits），SerialPort 支持 1,2,3
                 * p：校验位 (parity)，SerialPort 支持 0,1,2,3,4
                 * 如果参数设置错误，则抛出异常：gnu.io.UnsupportedCommOperationException: Invalid Parameter
                 * 此时必须关闭串口，否则下次 portIdentifier.open 时会打不开串口，因为已经被占用
                 */
                serialPort.setSerialPortParams(b, d, s, p);
                log.info("打开串口 {} 成功...", portName);
                return serialPort;
            } else {
                log.error("当前端口 {} 不是串口...", commPort.getName());
            }
        } catch (NoSuchPortException e) {
            log.error("串口 " + portName + " 不存在...", e);
        } catch (PortInUseException e) {
            log.error("串口 " + portName + " 已经被占用，请先解除占用...", e);
        } catch (UnsupportedCommOperationException e) {
            log.error("串口参数设置错误，关闭串口，数据位[5-8]、停止位[1-3]、验证位[0-4]...", e);
            // 此时必须关闭串口，否则下次 portIdentifier.open 时会打不开串口，因为已经被占用
            if (commPort != null) commPort.close();
        }
        log.error("打开串口 {} 失败...", portName);
        return null;
    }
 
    /**
     * 往串口发送数据
     *
     * @param serialPort  串口对象
     * @param orders      待发送数据
     */
    public static void sendDataToComPort(SerialPort serialPort, byte[] orders) {
        OutputStream outputStream = null;
        try {
            if (serialPort != null) {
                outputStream = serialPort.getOutputStream();
                outputStream.write(orders);
                outputStream.flush();
                log.info("往串口 {} 发送数据：{} 完成...", serialPort.getName(), Arrays.toString(orders));
            } else {
                log.error("gnu.io.SerialPort 为 null，取消数据发送...");
            }
        } catch (IOException e) {
            log.error("往串口发生数据发生 IO 异常", e);
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
 
    /**
     * 关闭串口
     * @param serialPort 待关闭的串口对象
     */
    public static void closeComPort(SerialPort serialPort) {
        if (serialPort != null) {
            serialPort.close();
            log.info("关闭串口 " + serialPort.getName());
        }
    }
 
    /**
     * 16进制字符串转十进制字节数组
     * 这是常用的方法，如某些硬件的通信指令就是提供的16进制字符串，发送时需要转为字节数组再进行发送
     *
     * @param strSource 16进制字符串，如 "455A432F5600"，每两位对应字节数组中的一个10进制元素
     *                  默认会去除参数字符串中的空格，所以参数 "45 5A 43 2F 56 00" 也是可以的
     * @return 十进制字节数组, 如 [69, 90, 67, 47, 86, 0]
     */
    public static byte[] hexString2Bytes(String strSource) {
        if (strSource == null || "".equals(strSource.trim())) {
            System.out.println("hexString2Bytes 参数为空，放弃转换.");
            return null;
        }
        strSource = strSource.replace(" ", "");
        int l = strSource.length() / 2;
        byte[] ret = new byte[l];
        for (int i = 0; i < l; i++) {
            ret[i] = Integer.valueOf(strSource.substring(i * 2, i * 2 + 2), 16).byteValue();
        }
        return ret;
    }
 
    public static void main(String[] args) {
        //发送普通数据
        byte[] bytes = new byte[]{1, 2, 3, 4, 5};
        SerialPort serialPort = SerialPortTool.openComPort(null, 38400, 8, 1, 0);
        SerialPortTool.sendDataToComPort(serialPort, bytes);
        SerialPortTool.closeComPort(serialPort);
 
        //发送16进制数据——实际应用中串口通信传输的数据，大都是 16 进制
        String hexStrCode = "455A432F5600";
        serialPort = SerialPortTool.openComPort(null, 38400, 8, 1, 0);
        SerialPortTool.sendDataToComPort(serialPort, hexString2Bytes(hexStrCode));
        SerialPortTool.closeComPort(serialPort);
    }
}