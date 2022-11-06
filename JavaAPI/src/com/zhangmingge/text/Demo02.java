package com.zhangmingge.text;

import java.util.Arrays;
import java.util.List;

/**
 * 数组、列表按指定分隔符 join
 * https://stackoverflow.com/questions/1978933/a-quick-and-easy-way-to-join-array-elements-with-a-separator-the-opposite-of-sp
 */
public class Demo02 {
    public static void main(String[] args) {
        String joined1 = String.join(",", "a", "b", "c");
        System.out.println("joined1 = " + joined1);
        String[] array = new String[] { "a", "b", "c" };
        String joined2 = String.join(",", array);
        System.out.println("joined2 = " + joined2);
        List<String> list = Arrays.asList(array);
        String joined3 = String.join(",", list);
        System.out.println("joined3 = " + joined3);
    }
}
