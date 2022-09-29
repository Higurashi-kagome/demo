package com.zhangmingge.regex;

import java.util.regex.Pattern;

/**
 * Pattern.matches(String regex,CharSequence input)
 */
public class Demo3 {
    public static void main(String[] args) {
        //返回true
        System.out.println(Pattern.matches("\\d+", "2223"));
        //返回false,需要匹配到所有字符串才能返回true,这里aa不能匹配到
        System.out.println(Pattern.matches("\\d+", "2223aa"));
        //返回false,需要匹配到所有字符串才能返回true,这里bb不能匹配到
        System.out.println(Pattern.matches("\\d+", "22bb23"));
    }
}
