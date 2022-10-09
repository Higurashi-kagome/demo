package com.zhangmingge;

import cn.hutool.core.util.StrUtil;
import com.hierynomus.msdtyp.AccessMask;
import com.hierynomus.mssmb2.SMB2CreateDisposition;
import com.hierynomus.mssmb2.SMB2ShareAccess;
import com.hierynomus.smbj.SMBClient;
import com.hierynomus.smbj.SmbConfig;
import com.hierynomus.smbj.auth.AuthenticationContext;
import com.hierynomus.smbj.connection.Connection;
import com.hierynomus.smbj.session.Session;
import com.hierynomus.smbj.share.DiskShare;
import com.hierynomus.smbj.share.File;

import java.io.IOException;
import java.util.EnumSet;

public class SMBJUtils {

    /**
     * 获取连接
     */
    public static Connection getConnection(String hostname) {
        Connection connection;
        try {
            SMBClient client = new SMBClient(SmbConfig.createDefaultConfig());
            connection = client.connect(hostname);
            return connection;
        } catch (IOException e) {
            System.err.println("连接失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 Session（认证）
     */
    public static Session getSession(Connection connection, String username, String password) {
        if (connection == null) return null;
        Session session;
        try {
            AuthenticationContext auth = new AuthenticationContext(username, password.toCharArray(), "");
            session = connection.authenticate(auth);
            return session;
        } catch (Exception e) {
            System.err.println("认证失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取 DiskShare
     */
    public static DiskShare getDiskShare(Session session, String shareName) {
        if (session == null || StrUtil.isBlank(shareName)) return null;
        DiskShare diskShare;
        try {
            diskShare = (DiskShare) session.connectShare(shareName);
            return diskShare;
        } catch (Exception e) {
            System.err.println("获取共享文件夹失败");
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 打开文件
     */
    public static File openFile(DiskShare diskShare, String fileName) {
        if (diskShare == null || StrUtil.isBlank(fileName)) return null;
        if (".".equals(fileName) || "..".equals(fileName) || !diskShare.fileExists(fileName)) return null;
        File file = diskShare.openFile(
                fileName, EnumSet.of(AccessMask.GENERIC_READ), null,
                SMB2ShareAccess.ALL, SMB2CreateDisposition.FILE_OPEN, null);
        return file;
    }
}
