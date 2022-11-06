package com.zhangmingge.access;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.io.IOException;

public class Utils {
    /**
     * 获取 Resource 文件夹下的文件路径
     * @param path 文件相对于 Resource 的路径
     * @return 文件的全路径
     */
    public static String getResourceFilePath(String path) {
        if (path == null) return null;
        Resource resource = new ClassPathResource(path);
        try {
            File file = resource.getFile();
            return file.getPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
