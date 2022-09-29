package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher.start(int i)/ Matcher.end(int i)/ Matcher.group(int i)
 */
public class Demo9 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("([a-z]+)(\\d+)");
        Matcher m = p.matcher("aaa2223bb");
        System.out.println(m.find());   //匹配aaa2223
        System.out.println(m.groupCount());   //返回2,因为有2组
        System.out.println(m.start(1));   //返回0 返回第一组匹配到的子字符串在字符串中的索引号
        System.out.println(m.start(2));   //返回3
        System.out.println(m.end(1));   //返回3 返回第一组匹配到的子字符串的最后一个字符在字符串中的索引位置.
        System.out.println(m.end(2));   //返回7
        System.out.println(m.group(0));   //返回aaa2223
        System.out.println(m.group(1));   //返回aaa,返回第一组匹配到的子字符串
        System.out.println(m.group(2));   //返回2223,返回第二组匹配到的子字符串
        p = Pattern.compile("[a-z]+(\\d+)");
        m = p.matcher("aaa2223bb");
        System.out.println(m.find());
        System.out.println(m.group());
        System.out.println(m.group(0));
        System.out.println(m.group(1));
    }
}
