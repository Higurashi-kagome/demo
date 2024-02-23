package com.zhangmingge.rs232;

import java.nio.ByteBuffer;

/**
 * byte 工具类
 *
 * @author yly
 * @CREATE_DATE 2022/9/3
 */
public class ByteUtil {

    private ByteUtil() {
    }

    /**
     * 截取byte数组,不改变原数组<br/>
     * 简化 Arrays.copyOfRange，请注意错误的调用
     *
     * @param b      原数组
     * @param off    偏差值（索引）
     * @param length 长度
     * @return 截取后的数组
     */
    public static byte[] subByte(byte[] b, int off, int length) {
        byte[] b1 = new byte[length];
        System.arraycopy(b, off, b1, 0, length);
        return b1;
    }

    /**
     * 截取并反转数据组
     *
     * @param b      源数组
     * @param off    起始位
     * @param length 长度
     */
    public static byte[] subAndReverse(byte[] b, int off, int length) {
        byte[] bytes = subByte(b, off, length);
        byte[] data = new byte[bytes.length];
        for (int i = bytes.length - 1, j = 0; i >= 0; i--, j++) {
            data[j] = bytes[i];
        }
        return data;
    }


    /**
     * byte数组（16 bit）转 无符号 int,
     *
     * @param b byte arr
     */
    public static int toUnInt16(byte[] b) {
        if (b.length < 2) {
            return 0x0;
        }
        return ((b[0] & 0xff) << 8) | (b[1] & 0xff);
    }

    /**
     * byte数组（16 bit）转 int,
     *
     * @param b byte arr
     */
    public static int toInt16(byte[] b) {
        if (b.length < 2) {
            return 0x0;
        }
        return (b[0] << 8) | (b[1] & 0xff);
    }

    /**
     * byte数组（32 bit） 转 int
     *
     * @param b byte[]
     * @return int,
     */
    public static int toInt32(byte[] b) {
        if (b.length < 4) {
            return 0x0;
        }
        return (b[0]) << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff);
    }

    /**
     * byte数组（32 bit） 转 float
     * 高位在前
     *
     * @param b byte[]
     * @return float
     */
    public static float toFloat(byte[] b) {
        if (b.length < 4) {
            return 0.0f;
        }
        return Float.intBitsToFloat(toInt32(b));
    }

    /**
     * 合并byte[]数组 （不改变原数组）
     *
     * @param byteA 数组a
     * @param byteB 数组b
     * @return 合并后的数组
     */
    public static byte[] byteMerger(byte[] byteA, byte[] byteB) {
        byte[] byteC = new byte[byteA.length + byteB.length];
        System.arraycopy(byteA, 0, byteC, 0, byteA.length);
        System.arraycopy(byteB, 0, byteC, byteA.length, byteB.length);
        return byteC;
    }

    /**
     * 16进制字符串转byte
     *
     * @param hex 16进制字符串
     * @return byte[]
     */
    public static byte[] hexStr2Byte(String hex) {
        try {
            ByteBuffer bf = ByteBuffer.allocate(hex.length() >> 1);
            for (int i = 0; i < hex.length(); i++) {
                String hexStr = hex.charAt(i) + "";
                i += 1;
                hexStr += hex.charAt(i);
                byte b = (byte) Integer.parseInt(hexStr, 16);
                bf.put(b);
            }
            return bf.array();
        } catch (Exception e) {
            return new byte[0];
        }
    }

    /**
     * CRC16 编码
     *
     * @param bytes 编码内容
     * @return 编码结果, 【低位，高位】
     */
    public static byte[] crc16(byte[] bytes, int count) {
        int poly = 0xA001;
        int res = 0xffff;
        for (int i = 0; i < count; i++) {
            res = res ^ (bytes[i] & 0xff);
            for (int j = 0; j < 8; j++) {
                res = (res & 0x0001) == 1 ? (res >> 1) ^ poly : res >> 1;
            }
        }
        return new byte[]{
            (byte) (res & 0x00FF),
            (byte) ((res & 0xFF00) >> 8)
        };
    }

    /**
     * 翻转16位的高八位和低八位字节
     *
     * @param src 翻转数字
     * @return 翻转结果
     */
    public static int revert(int src) {
        int lowByte = (src & 0xFF00) >> 8;
        int highByte = (src & 0x00FF) << 8;
        return lowByte | highByte;
    }

    /**
     * byte[] 数组转string
     *
     * @param bytes
     */
    public static String toString(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bytes.length);
        String sTemp;
        for (byte aByte : bytes) {
            sTemp = Integer.toHexString(0xFF & aByte);
            if (sTemp.length() < 2) {
                sb.append(0);
            }
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * byte转字符串的bit
     */
    public static String byteToBitString(byte b) {
        return ""
            + (byte) ((b >> 7) & 0x1) + (byte) ((b >> 6) & 0x1)
            + (byte) ((b >> 5) & 0x1) + (byte) ((b >> 4) & 0x1)
            + (byte) ((b >> 3) & 0x1) + (byte) ((b >> 2) & 0x1)
            + (byte) ((b >> 1) & 0x1) + (byte) ((b) & 0x1);
    }

    /**
     * byte转换为一个长度为8的byte数组，数组每个值代表bit
     */
    public static byte[] byteToBit(byte b) {
        byte[] array = new byte[8];
        for (int i = 7; i >= 0; i--) {
            array[i] = (byte) (b & 1);
            b = (byte) (b >> 1);
        }
        return array;
    }

}
