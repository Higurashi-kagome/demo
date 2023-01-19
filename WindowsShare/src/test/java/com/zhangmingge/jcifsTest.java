package com.zhangmingge;

import jcifs.smb.NtlmPasswordAuthentication;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;

import java.net.MalformedURLException;

public class jcifsTest {
    private static final String hostname = "10.32.103.18";

//    private static final String username = "znsw";
    private static final String username = "administrator";

//    private static final String password = "Znsw_2022";
    private static final String password = "123456";

    private static final String shareName = "mdb";

    public static void main(String[] args) throws MalformedURLException, SmbException {
        NtlmPasswordAuthentication auth = new NtlmPasswordAuthentication(null, username, password);
        SmbFile file = new SmbFile("smb://" + hostname + "/" + shareName + "/all_num.mdb", auth);
        if (!file.exists()) {
            System.out.println("exists is true");
        }
    }
}
