package com.zhangmingge.rs232;

/**
 * 设备统一连接接口
 *
 * @author yly
 * @CREATE_DATE 2022/9/3
 */
public interface IotConnection {

    /**
     * 向设备发送信息
     *
     * @param bytes 字节数组
     * @return 是否发送成功
     */
    boolean send(byte[] bytes);

    /**
     * 向设备发送信息
     *
     * @param data 字符串数据
     * @return 是否发送成功
     */
    boolean send(String data);

    /**
     * 读取所有数据
     *
     * @return 字节数组
     */
    byte[] read();

    /**
     * 读取指定长度数据
     *
     * @param length 读取数据的长度（按字节）
     * @return 字节数组
     */
    byte[] read(int length);

    /**
     * 读取一行
     *
     * @return 字符串
     */
    String readLine();

    /**
     * 读取一行，1024字节以内
     *
     * @return byte数组
     */
    byte[] readByteLine();

    /**
     * 获取本地端口
     */
    String getPort();

    /**
     * 关闭当前连接
     */
    void close();

    /**
     * 连接是否已关闭
     */
    boolean isClose();

    /**
     * 是否正在监听
     */
    boolean isListening();

    /**
     * 重新开始监听
     */
    default void reListen() {
    }

}
