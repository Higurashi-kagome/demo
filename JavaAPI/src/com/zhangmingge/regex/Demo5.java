package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher.matches()
 */
public class Demo5 {
    public static void main(String[] args) {
        Pattern p= Pattern.compile("\\d+");
        Matcher m = p.matcher("22bb23");
        System.out.println(m.matches());//返回false,因为bb不能被\d+匹配,导致整个字符串匹配未成功.
        Matcher m2 = p.matcher("2223");
        System.out.println(m2.matches());//返回true,因为\d+匹配到了整个字符串
    }
}
