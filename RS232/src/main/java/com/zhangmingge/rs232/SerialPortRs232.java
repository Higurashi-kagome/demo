package com.zhangmingge.rs232;

import cn.hutool.core.util.StrUtil;
import gnu.io.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

/**
 * RS232 串口连接
 *
 * @author yly
 * @date 2020/8/6
 * "C:\\Windows\\System32\\" 指定 "rxtxParallel.dll" "rxtxSerial.dll"
 */
public class SerialPortRs232 implements IotConnection, SerialPortEventListener {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 串口相关参数
     */
    private final SerialPortParam serialPortParam;
    private final ArrayBlockingQueue<String> arrayBlockingQueue;
    /**
     * 232串口
     */
    private SerialPort serialPort;

    public SerialPortRs232(SerialPortParam serialPortParam) {
        this.serialPortParam = serialPortParam;
        this.arrayBlockingQueue = new ArrayBlockingQueue<>(8);
        init();
    }

    @SuppressWarnings("unchecked")
    public void init() {
        Enumeration<CommPortIdentifier> portList = CommPortIdentifier.getPortIdentifiers();
        // 记录是否含有指定串口
        boolean isExist = false;
        // 循环通讯端口
        while (portList.hasMoreElements()) {
            CommPortIdentifier commPortId = portList.nextElement();
            // 比较串口名称是否是指定串口
            if (StrUtil.equals(this.serialPortParam.getSerialNumber(), commPortId.getName())) {
                isExist = true;
                // 打开串口
                try {
                    // open:（应用程序名【随意命名】，阻塞时等待的毫秒数）
                    this.serialPort = (SerialPort) commPortId.open(SerialPortRs232.class.getSimpleName(), 2000);
                    // 设置串口监听
                    this.serialPort.addEventListener(this);
                    // 设置串口数据时间有效(可监听)
                    this.serialPort.notifyOnDataAvailable(true);
                    // 设置串口通讯参数:波特率，数据位，停止位,校验方式
                    this.serialPort.setSerialPortParams(this.serialPortParam.getBaudRate(), this.serialPortParam.getDataBit(),
                        this.serialPortParam.getStopBit(), this.serialPortParam.getCheckoutBit());
                    this.serialPort.enableReceiveTimeout(3000);
                } catch (PortInUseException e) {
                    throw new RuntimeException("端口被占用");
                } catch (TooManyListenersException e) {
                    throw new RuntimeException("监听器过多");
                } catch (UnsupportedCommOperationException e) {
                    throw new RuntimeException("不支持的COMM端口操作异常");
                }
                break;
            }
        }
        // 若不存在该串口则抛出异常
        if (!isExist) {
            throw new RuntimeException("不存在该串口！");
        }
    }

    @Override
    public boolean send(byte[] bytes) {
        try {
            OutputStream outputStream = this.serialPort.getOutputStream();
            outputStream.write(bytes);
            outputStream.flush();
            this.logger.info("串口{}, 发送命令:{}", this.serialPortParam.getSerialNumber(), ByteUtil.toString(bytes));
            return true;
        } catch (NullPointerException e) {
            this.logger.error("找不到串口。");
        } catch (IOException e) {
            this.logger.error("发送信息到串口时发生IO异常");
        }
        return false;
    }

    @Override
    public boolean send(String data) {
        return send(data.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * 读取一个单元的数据
     */
    @Override
    public byte[] read() {
        return read(1024);
    }

    @Deprecated
    @Override
    public byte[] read(int length) {
        try {
            //return ByteUtil.hexStr2Byte(this.arrayBlockingQueue.take());
            return ByteUtil.hexStr2Byte(this.arrayBlockingQueue.poll(100, TimeUnit.MILLISECONDS));
        } catch (Exception e) {
            this.logger.error("Socket在读取数据时发生错误！{}", e.getMessage());
            return new byte[0];
        }
    }

    @Override
    public String readLine() {
        return new String(read());
    }

    @Override
    public byte[] readByteLine() {
        return read();
    }

    @Override
    public String getPort() {
        return this.serialPortParam.getSerialNumber();
    }

    @Override
    public void close() {
        if (this.serialPort != null) {
            this.serialPort.notifyOnDataAvailable(false);
            this.serialPort.removeEventListener();
            this.serialPort.close();
            this.serialPort = null;
        }
    }

    @Override
    public void serialEvent(SerialPortEvent serialPortEvent) {
        switch (serialPortEvent.getEventType()) {
            case SerialPortEvent.BI: // 通讯中断
            case SerialPortEvent.OE: // 溢位错误
            case SerialPortEvent.FE: // 帧错误
            case SerialPortEvent.PE: // 奇偶校验错误
            case SerialPortEvent.CD: // 载波检测
            case SerialPortEvent.CTS: // 清除发送
            case SerialPortEvent.DSR: // 数据设备准备好
            case SerialPortEvent.RI: // 响铃侦测
            case SerialPortEvent.OUTPUT_BUFFER_EMPTY: // 输出缓冲区已清空
                break;
            case SerialPortEvent.DATA_AVAILABLE: // 有数据到达
                // 调用读取数据的方法
                readComm();
                break;
            default:
                break;
        }
    }

    /**
     * 读取数据事件
     */
    private void readComm() {
        try {
            InputStream inputStream = this.serialPort.getInputStream();
            // 通过输入流对象的available方法获取数组字节长度
            byte[] readBuffer = new byte[1024];
            // 从线路上读取数据流
            int len;
            while ((len = inputStream.read(readBuffer)) != -1) {
                if (len == 0) {
                    break;
                }
                byte[] bytes = Arrays.copyOf(readBuffer, len);
                this.logger.info("{} get data:{}", this.serialPortParam.getSerialNumber(), ByteUtil.toString(bytes));
                this.arrayBlockingQueue.put(ByteUtil.toString(bytes));
            }
            inputStream.close();
        } catch (IOException | InterruptedException e) {
            this.logger.error("读取串口数据时发生IO异常");
        }
    }

    @Override
    public boolean isClose() {
        return this.serialPort == null;
    }

    @Override
    public boolean isListening() {
        return this.serialPort != null;
    }

}
