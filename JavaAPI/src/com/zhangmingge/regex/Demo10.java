package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 捕获组
 */
public class Demo10 {
    public static void main(String[] args) {
        String line = "ABCDE";
        String pattern = "((A)(B(C)))D";
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(line);
        if (m.find()) {
            System.out.println(m.groupCount());
            System.out.println(m.group(0));
            System.out.println(m.group(1));
            System.out.println(m.group(2));
            System.out.println(m.group(3));
            System.out.println(m.group(4));
        } else {
            System.out.println("无匹配");
        }
    }
}
