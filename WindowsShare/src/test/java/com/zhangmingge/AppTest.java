package com.zhangmingge;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * 访问局域网内的共享文件夹
 */
public class AppTest
{

    private String hostname = "10.32.103.18";
//    private String hostname = "XL-201301101214";

    private String username = "znsw";

    private String password = "Znsw_2022";

    private String shareName = "mdb";

    public static void main(String[] args) {
        new AppTest().testSave();
    }

    /**
     * 测试下载文件
     */
    public void testSave() {
        Connection connection = SMBJUtils.getConnection(hostname);
        Session session = SMBJUtils.getSession(connection, username, password);
        DiskShare diskShare = SMBJUtils.getDiskShare(session, shareName);
        File file = SMBJUtils.openFile(diskShare, "all_num.mdb");
        saveToLocal(file, "C:\\Users\\liuhao\\Desktop\\temp\\test.mdb");
//        saveToLocal(file, "C:\\Users\\Administrator\\Desktop\\temp\\test.mdb");
        close(connection);
    }

    /**
     * 关闭连接
     */
    private void close(Connection connection) {
        try {
            if (connection != null) connection.close();
        } catch (IOException e) { }
    }

    /**
     * 读取文件内容到本地
     */
    private void saveToLocal(File file, String path) {
        if (file == null) return;
        InputStream in = file.getInputStream();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        try {
            int len;
            while ((len = in.read(buffer, 0, buffer.length)) != - 1) {
                out.write(buffer, 0, len);
            }
            out.flush();
            Path p = Paths.get(path);
            java.io.File f = new java.io.File(path);
            if (f.isDirectory()) return;
            java.io.File parentFile = f.getParentFile();
            if (!parentFile.exists()) parentFile.mkdirs();
            if (!f.exists()) f.createNewFile();
            Files.write(p, out.toByteArray());
        } catch (IOException e) {
            System.err.println("写入文件失败");
            e.printStackTrace();
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {}
        }
    }

    /**
     * 列出文件和文件夹
     */
    public void testListFiles() {
        Connection connection = SMBJUtils.getConnection(hostname);
        Session session = SMBJUtils.getSession(connection, username, password);
        DiskShare diskShare = SMBJUtils.getDiskShare(session, shareName);
        if (diskShare == null) {
            close(connection);
            return;
        }
        ArrayList<String> strings = new ArrayList<>();
        List<FileIdBothDirectoryInformation> list = diskShare.list("");
        for (FileIdBothDirectoryInformation information : list) {
            strings.add(information.getFileName());
        }
        System.out.println(strings);
        close(connection);
    }
}
