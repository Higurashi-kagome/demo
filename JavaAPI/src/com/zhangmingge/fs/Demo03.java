package com.zhangmingge.fs;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * 创建目录<br>
 * <a href="https://mkyong.com/java/how-to-create-directory-in-java/">...</a>
 */
public class Demo03 {
    public static void main(String[] args) {
        // https://blog.csdn.net/shijiujiu33/article/details/85345815
        String curPath = Demo03.class.getResource("").getPath().substring(1);
        Path a = Paths.get(curPath, "a");
        Path bc = Paths.get(curPath, "b", "c");
        Path classFile = Paths.get(curPath, "Demo03.class");
        try {
            // 测试创建一级目录
            Files.createDirectories(a);
            // 测试创建多级目录
            Files.createDirectories(bc);
            // 测试创建已存在的目录
            Files.createDirectories(bc);
            // 测试路径为文件时创建目录（抛 FileAlreadyExistsException）
            Files.createDirectories(classFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
