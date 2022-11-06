package com.zhangmingge.text;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * 获取堆栈字符串<br>
 * https://stackoverflow.com/questions/1149703/how-can-i-convert-a-stack-trace-to-a-string
 */
public class Demo03 {
    /**
     * 获取异常堆栈字符串
     */
    public static String getStackTrace(Exception e) {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        e.printStackTrace(pw);
        String sStackTrace = sw.toString();
        try {
            sw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        pw.close();
        return sStackTrace;
    }

    public static void main(String[] args) {
        try {
            throw new IOException("An IOException");
        } catch (Exception e) {
            String stackTrace = getStackTrace(e);
            System.out.println(stackTrace);
        }
    }
}
