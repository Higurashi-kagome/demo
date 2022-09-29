package entity;

import api.RFID;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.HexUtil;
import com.sun.jna.Native;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NFCReader {
    /**
     * 函数调用后的结果码（0 为成功）
     */
    private short resultCode = 1;

    /**
     * 读卡器标识符
     */
    private Integer icDev = 0;
    /**
     * 当前卡的序列号
     */
    private byte[] curSnr = new byte[5];

    public RFID rf = null;

    /**
     * 端口号<br>
     * COM1 - 0<br>
     * COM2 - 1 ...<br>
     */
    private int port = 0;

    public NFCReader() {
        rf = Native.loadLibrary("rf32", RFID.class);
        if (rf != null) {
            System.out.println("DLL加载成功！");
        } else {
            System.err.println("DLL加载失败！");
            System.exit(100);
        }
    }

    /**
     * 错误消息
     * @return 有错："错误代码：" + resultCode <br>无错：""
     */
    private String getErrCodeMsg() {
        if (resultCode != 0) {
            return "错误代码：" + resultCode;
        } else {
            return "";
        }
    }

    /**
     * 连接读卡器，加载 NFC 密码到读卡器
     */
    public void connect() {
        byte[] version = new byte[20];
        icDev = rf.rf_init((short) port, 9600);
        resultCode = rf.rf_get_status(icDev, version);
        if (resultCode == 0) {
            String str = new String(version, 0, 15, Charset.forName("GBK"));
            System.out.println("设备初始化成功！硬件版本号：" + str);
        } else {
            System.err.println("设备连接失败！" + getErrCodeMsg());
        }
        loadNfcKeys();
    }

    /**
     * 加载 NFC 密码到读卡器
     */
    private void loadNfcKeys() {
        for (short i = 0; i < 16; i++) {
            byte[] key;
            if (i == 0) { // 0 扇区
                key = new byte[]{ (byte)0xA0, (byte)0xA1, (byte)0xA2, (byte)0xA3, (byte)0xA4, (byte)0xA5 };
            } else { // 其他扇区
                key = new byte[]{ (byte) 0xd3, (byte) 0xf7, (byte) 0xd3, (byte) 0xf7, (byte) 0xd3, (byte) 0xf7};
            }
            resultCode = rf.rf_load_key(icDev, (short) 0, i, key);
            if (resultCode != 0) {
                System.err.println("加载 " + i + " 扇区密码失败！" + getErrCodeMsg());
            }
        }
        System.out.println("加载扇区密码成功！");
    }

    /**
     * 读取卡中的 JSON 字符串
     * @return 找到返回 JSON 字符串，没找到返回空字符串
     */
    public String readJson() {
        byte[] allBytes = readAllBytes();
        System.out.println("allBytes = " + String.valueOf(HexUtil.encodeHex(allBytes)));
        String strData = new String(allBytes);
        //匹配 json 对象字符串
        String jsonStr = "";
        Pattern pattern = Pattern.compile("(\\{[^\\{\\}]*\\})");
        Matcher matcher = pattern.matcher(strData);
        if (matcher.find()) {
            jsonStr = matcher.group(0);
        } else {
            System.err.println("strData = " + strData);
        }
        // 选择IDLE模式，在对卡进行读写操作，执行rf_halt()rf_halt指令中止卡操作后，只有当该卡离开并再次进入操作区时，读写器才能够再次对它进行操作
        rf.rf_halt(icDev);
        return jsonStr;
    }

    /**
     * 获取所有可存位内容到 byte 数组
     * @return 读入了所有可存位 byte 数组
     */
    public byte[] readAllBytes() {
        byte[] allBytes = new byte[0];
        byte[] rdata = new byte[16];
        resultCode = rf.rf_authentication(icDev, (short) 0, (short) 0);
        if (resultCode != 0) {
            System.err.println("0 扇区密码验证错误！" + getErrCodeMsg());
        }
        // sector——扇区号（总共 16 个扇区）；b——块编号，从 0 开始（每个扇区 4 个块，16 个扇区，总共 64 块）；
        // 第 0 块数据被固化，不可写入，所以从块 1 开始读
        for (int b = 1, sector = 0; b < 64; b++) {
            // 每进入一个新的扇区，都要进行验证
            if ((b + 1) % 4 == 1) {
                sector++;
                resultCode = rf.rf_authentication(icDev, (short) 0, (short) sector);
                if (resultCode != 0) {
                    System.err.println(sector + "扇区密码验证错误！" + getErrCodeMsg());
                }
            }
            // 每个扇区的第四块为特殊块，不存数据
            if ((b + 1) % 4 != 0) {
                resultCode = rf.rf_read(icDev, (short) b, rdata);
                if (resultCode == 0) {
                    // System.out.println(HexUtil.encodeHex(rdata));
                    allBytes = ArrayUtil.addAll(allBytes, rdata);
                    // 如果某个块的数据全为 0 则认为之后的块没有存数据，直接跳过
                    if (ByteUtil.bytesToInt(rdata) == 0) {
                        break;
                    }
                } else {
                    System.err.println("读数据失败！块：" + b);
                }
            }
        }
        return allBytes;
    }

    /**
     * 断开设备
     */
    public void disconnect() {
        rf.rf_exit(icDev);
        System.out.println("断开设备");
    }

    /**
     * 寻卡，寻卡后卡序列号写入 snr
     * @return 找到则返回卡序列号字符串，没找到返回空字符串
     */
    public String findCard() {
        // 选择IDLE模式，在对卡进行读写操作，执行rf_halt()rf_halt指令中止卡操作后，只有当该卡离开并再次进入操作区时，读写器才能够再次对它进行操作
        resultCode = rf.rf_card(icDev, (short) 0, curSnr);
        String snrStr = "";
        if (resultCode == 0) {
            byte[] snrHex = new byte[9];
            rf.hex_a(curSnr, snrHex, (short) 4);
            snrStr = new String(snrHex, 0, 8);
            System.out.println("寻卡成功，卡序列号：" + snrStr);
        }
        return snrStr;
    }

    /**
     * 蜂鸣
     * @param millis 蜂鸣时长，单位毫秒
     */
    public void beep(int millis) {
        if (millis <= 0) return;
        if (icDev != null) {
            // rf_beep timeout 单位是10毫秒
            short beepTimout = (short) (millis / 10);
            resultCode = rf.rf_beep(icDev, beepTimout);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        NFCReader con = new NFCReader();
        con.connect();
        // 卡序列号字符串为键，卡中保存的 JSON 字符串为值
        Map<String, String> dataMap = new HashMap<>();
        while (true) {
            String snrStr = con.findCard();
            if (StringUtils.isBlank(snrStr)) {
                Thread.sleep(500);
            } else {
                String jsonStr = con.readJson();
                if (StringUtils.isBlank(jsonStr)) {
                    System.err.println("未找到 JSON 字符串");
                } else {
                    // 记录过该卡，且上次读取的数据和此次读取的数据相同时，跳过
                    if (dataMap.containsKey(snrStr)
                            && dataMap.get(snrStr).equals(jsonStr)) continue;
                    dataMap.put(snrStr, jsonStr);
                    System.out.println("jsonStr = " + jsonStr);
//                    con.beep(50);
                }
            }
        }
//		con.disconnect();
    }
}
