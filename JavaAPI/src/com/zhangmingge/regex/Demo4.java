package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Pattern.matcher(CharSequence input)
 */
public class Demo4 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher("22bb23");
        System.out.println(m.pattern());//返回p 也就是返回该Matcher对象是由哪个Pattern对象的创建的
    }
}
