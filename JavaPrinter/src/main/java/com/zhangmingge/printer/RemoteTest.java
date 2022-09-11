package com.zhangmingge.printer;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;

/**
 * Java 建立 Socket 连接，发送 ESC/POS 指令实现打印控制<br>
 * 相关指令参考厂家的开发手册，ESC/POS 通常是通用的，不限于指定机型，所以如果厂家没有提供，也可以自行在网上查找
 */
public class RemoteTest {

    private static final Logger logger = LoggerFactory.getLogger(RemoteTest.class);
    private static OutputStream socketOut;
    private static OutputStreamWriter writer;

    public RemoteTest(String ip, int port) {
        try {
            Socket socket = new Socket(ip, port);
            socket.setSoTimeout(1000);
            socketOut = socket.getOutputStream();
            writer = new OutputStreamWriter(socketOut, "GBK");
            socketOut.write(27);
            logger.debug("连接打印机{}:{}成功", ip, port);
        } catch (Exception e) {
            logger.error("连接打印机{}:{}失败 错误信息: {}", ip, port, e.getMessage());
        }
    }


    /**
     * 关闭 writer 和 socketOut
     */
    public void close() {
        try {
            writer.close();
            socketOut.close();
        } catch (IOException e) {
            logger.error("关闭失败，详细信息：{}", e.getMessage());
        }
    }

    /**
     * 控制打印机送纸
     */
    public void feed() throws IOException {
        writer.write(27);
        writer.write(100);
        writer.write(4);
        writer.write(10);
        writer.flush();
    }

    /**
     * 裁纸，功能能否正常工作取决于打印机硬件
     */
    public void cut() throws IOException {
        writer.write(0x1D);
        writer.write("V");
        writer.write(48);
        writer.write(0);
        writer.flush();
    }

    /**
     * 设置排版
     * val：0：居左 1：居中 2：居右
     */
    public void setJustification(int val) throws IOException {
        writer.write(0x1B);
        writer.write("a");
        writer.write(val);
        writer.flush();
    }

    /**
     * 打印字符串
     */
    public void printStr(String str) throws IOException {
        writer.write(str);
        writer.flush();
    }

    public static void main(String[] argus) {
        RemoteTest printer = new RemoteTest("192.168.2.8", 9100);
        try {
            String data = "检斤时间：2022-09-11 14:12:47\n检斤重量（吨）：40.0\n检斤点：南门检斤点\n车牌号：湘B12345\n订单类别：厂内普通";
            printer.setJustification(0);
            printer.printStr(data);
            printer.feed();
            printer.cut();
            logger.error("打印成功，详细信息：{}", data);
        } catch (IOException e) {
            logger.error("打印失败，详细信息：{}", e.getMessage());
        } finally {
            if (printer != null) {
                printer.close();
            }
        }
    }
}
