package com.zhangmingge.regex;

import java.util.regex.Pattern;

/**
 * Pattern
 */
public class Demo1 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\w+");
        System.out.println(p.pattern());//返回 \w+
    }
}
