package com.zhangmingge.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Matcher.start()/ Matcher.end()/ Matcher.group()
 */
public class Demo8 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+");

        Matcher m = p.matcher("aaa2223bb");
        System.out.println(m.find());//匹配2223
        System.out.println(m.start());//返回3
        System.out.println(m.end());//返回7,返回的是2223后的索引号
        System.out.println(m.group());//返回2223

        Matcher m2 = p.matcher("2223bb");
        System.out.println(m2.lookingAt());   //匹配2223
        System.out.println(m2.start());   //返回0,由于lookingAt()只能匹配前面的字符串,所以当使用lookingAt()匹配时,start()方法总是返回0
        System.out.println(m2.end());   //返回4
        System.out.println(m2.group());   //返回2223
        System.out.println(m2.matches());
    }
}
