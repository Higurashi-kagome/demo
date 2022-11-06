package com.zhangmingge.fs;

import java.nio.file.Paths;

/**
 * System.getProperty("user.dir") 获取工作目录<br>
 * https://blog.csdn.net/chaofanwei/article/details/15498383<br>
 * https://stackoverflow.com/questions/34459486/joining-paths-in-java<br>
 * Class.getResource("").getPath() 获取当前 .class 文件所在的路径
 */
public class Demo01 {
    public static void main(String[] args) {
        System.out.println("Working Directory = " + System.getProperty("user.dir"));
        System.out.println("Working Directory = " + Paths.get("").toAbsolutePath());
        String classPath = new Demo01().getClass().getResource("").getPath();
        System.out.println("classPath = " + classPath);
    }
}