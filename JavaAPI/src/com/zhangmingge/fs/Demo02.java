package com.zhangmingge.fs;

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 路径拼接<br>
 * https://stackoverflow.com/questions/34459486/joining-paths-in-java
 */
public class Demo02 {
    public static void main(String[] args) {
        Path currentPath = Paths.get(System.getProperty("user.dir"));
        Path filePath = Paths.get(currentPath.toString(), "data", "foo.txt");
        System.out.println(filePath);
    }
}
