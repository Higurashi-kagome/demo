package entity;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.HexUtil;
import com.google.gson.Gson;
import com.sun.jna.Native;
import api.Declare;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NFCReader {
    /**
     * 函数调用后的结果码（0 为成功）
     */
    private short resultCode = 1;

    /**
     * ic 卡标识符
     */
    private int icDev = 0;
    /**
     * 卡序列号
     */
    private byte[] snr = new byte[5];

    Declare.RFID rf = null;

    /**
     * 端口号<br>
     * COM1 - 0<br>
     * COM2 - 1 ...<br>
     */
    private int port = 0;

    public NFCReader() {
        rf = Native.loadLibrary("rf32", Declare.RFID.class);
        if (rf != null) {
            System.out.println("DLL加载成功！");
        } else {
            System.out.println("DLL加载失败！");
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
            System.out.println("设备连接失败！" + getErrCodeMsg());
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
                System.out.println("加载 " + i + " 扇区密码失败！" + getErrCodeMsg());
            } else {
                System.out.println("加载 " + i + " 扇区密码成功！");
            }
        }
    }

    /**
     * 读取 M1 卡中的数据
     */
    public void readMifareOne() {
        byte[] allData = readAllData();
        String s = new String(allData);
        //匹配 json 对象字符串
        Pattern pattern = Pattern.compile("(\\{[^\\{\\}]*\\})");
        Matcher matcher = pattern.matcher(s);
        if (matcher.find()) {
            String jsonStr = matcher.group(0);
            Gson gson = new Gson();
            DataEntity dataEntity = gson.fromJson(jsonStr, DataEntity.class);
            System.out.println(dataEntity);
        } else {
            System.out.println("未知的数据格式");
        }
        // 采用ALL模式寻卡（见 findCard 中的 rf_card），结束时执行 rf_halt，避免寻卡总是一次对，一次错
        rf.rf_halt(icDev);
    }

    /**
     * 获取所有可存位内容到 byte 数组
     * @return 读入了所有可存位 byte 数组
     */
    public byte[] readAllData() {
        byte[] allData = new byte[0];
        byte[] rdata = new byte[16];
        int sector = 0;
        resultCode = rf.rf_authentication(icDev, (short) 0, (short) sector);
        if (resultCode != 0) {
            System.out.println(sector + "扇区密码验证错误！" + getErrCodeMsg());
        }
        for (int i = 1; i < 64; i++) {
            if ((i + 1) % 4 == 1) { // 每进入一个新的扇区，都要进行验证
                sector++;
                resultCode = rf.rf_authentication(icDev, (short) 0, (short) sector);
                if (resultCode != 0) {
                    System.out.println(sector + "扇区密码验证错误！" + getErrCodeMsg());
                }
            }
            if ((i + 1) % 4 != 0) { // 每个扇区的第四块为特殊块，不存数据
                resultCode = rf.rf_read(icDev, (short) i, rdata);
                if (resultCode == 0) {
                    System.out.println(HexUtil.encodeHex(rdata));
                    allData = ArrayUtil.addAll(allData, rdata);
                } else {
                    System.out.println("读数据失败！块：" + i);
                }
            }
        }
        return allData;
    }

    /**
     * 断开设备
     */
    public void disconnect() {
        rf.rf_exit(icDev);
        System.out.println("断开设备");
    }

    /**
     * 寻卡，寻卡后卡序列号写入 {@link snr}
     */
    public void findCard() {
        // 采用ALL模式寻卡，结束时执行 rf_halt(见 readMifareOne 中的 rf_halt)，避免寻卡总是一次对，一次错
        resultCode = rf.rf_card(icDev, (short) 1, snr);
        if (resultCode == 0) {
            byte[] snrHex = new byte[9];
            rf.hex_a(snr, snrHex, (short) 4);
            String SnrStr = new String(snrHex, 0, 8);
            System.out.println("寻卡成功，卡序列号：" + SnrStr);
        } else {
            System.out.println("寻卡失败！" + getErrCodeMsg());
        }
    }

    public static void main(String[] args) {
        // rf32.dll 位于 jna jar 包根目录
        NFCReader con = new NFCReader();
        con.connect();
        con.findCard();
		con.readMifareOne();
		con.disconnect();
    }
}