package com.zhangmingge;

import com.hierynomus.msfscc.fileinformation.FileIdBothDirectoryInformation;
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

public class AppTest
{

    private String hostname = "192.168.124.16";

    private String username = "znwl";

    private String password = "Zlzn_2021";

    private String shareName = "Reader_Web_Service_V1.0.0.7_20211112";

    /**
     * 测试下载文件
     */
    @Test
    public void testSave() {
        Session session = SMBJUtils.getSession(hostname, username, password);
        DiskShare diskShare = SMBJUtils.getDiskShare(session, shareName);
        if (diskShare == null) return;
        File file = SMBJUtils.openFile(diskShare, "smartReader.html");
        saveToLocal(file);
    }

    /**
     * 读取文件内容到本地
     */
    private void saveToLocal(File file) {
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
            Path path = Paths.get("C:\\Users\\liuhao\\Desktop\\test.html");
            Files.write(path, out.toByteArray());
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
    @Test
    public void testListFiles() {
        Session session = SMBJUtils.getSession(hostname, username, password);
        DiskShare diskShare = SMBJUtils.getDiskShare(session, shareName);
        if (diskShare == null) return;
        ArrayList<String> strings = new ArrayList<>();
        List<FileIdBothDirectoryInformation> list = diskShare.list("");
        for (FileIdBothDirectoryInformation information : list) {
            strings.add(information.getFileName());
        }
        System.out.println(strings);
    }
}
