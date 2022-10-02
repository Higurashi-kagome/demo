package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Mathcer.start(int i)/ Matcher.end(int i)/ Matcher.group(int i)
 */
public class Demo11 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher("我的QQ是:456456 我的电话是:0532214 我的邮箱是:aaa123@aaa.com");
        while (m.find()) {
            System.out.println(m.group());
            System.out.printf("start:%d end:%d%n", m.start(), m.end());
        }
    }
}
