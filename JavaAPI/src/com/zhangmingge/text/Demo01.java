package com.zhangmingge.text;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * SimpleDateFormat 中 yyyy.MM.dd 为年月日而如果希望格式化时间为 12 小时制的，则使用 hh:mm:ss 如果希望格式化时间为 24 小时制的，则使用 HH:mm:ss<br>
 * https://blog.csdn.net/xymyeah/article/details/1654364
 */
public class Demo01 {
    public static void main(String[] args) {
        Date date = new Date();
        String formatData1 = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss").format(date);
        System.out.println(formatData1);
        String formatData2 = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss").format(date);
        System.out.println(formatData2);
    }
}
