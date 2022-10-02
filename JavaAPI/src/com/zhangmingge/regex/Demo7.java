package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher.find()
 */
public class Demo7 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m1 = p.matcher("22bb23");
        System.out.println(m1.find());//返回true
        Matcher m2 = p.matcher("aa2223");
        System.out.println(m2.find());//返回true
        Matcher m3 = p.matcher("aa2223bb");
        System.out.println(m3.find());//返回true
        Matcher m4 = p.matcher("aabb");
        System.out.println(m4.find());//返回false
    }
}
