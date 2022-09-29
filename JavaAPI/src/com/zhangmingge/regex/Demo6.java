package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher.lookingAt()
 */
public class Demo6 {
    public static void main(String[] args) {
        Pattern p= Pattern.compile("\\d+");
        Matcher m = p.matcher("22bb23");
        System.out.println(m.lookingAt());//返回true,因为\d+匹配到了前面的22
        Matcher m2 = p.matcher("aa2223");
        System.out.println(m2.lookingAt());//返回false,因为\d+不能匹配前面的aa
    }
}
