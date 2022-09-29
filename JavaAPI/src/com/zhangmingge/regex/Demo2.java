package com.zhangmingge.regex;

import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Pattern.split(CharSequence input)
 */
public class Demo2 {
    public static void main(String[] args) {
        Pattern p = Pattern.compile("\\d+");
        String[] str = p.split("我的QQ是:456456我的电话是:0532214我的邮箱是:aaa@aaa.com");
        System.out.println(Arrays.toString(str));
    }
}
