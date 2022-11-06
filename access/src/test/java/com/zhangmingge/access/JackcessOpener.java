package com.zhangmingge.access;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import net.ucanaccess.jdbc.JackcessOpenerInterface;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

/**
 * @AUTHOR kd
 * @CREATE_DATE 2022/10/31
 */
public class JackcessOpener implements JackcessOpenerInterface {

    @Override
    public Database open(File file, String s) throws IOException {
        DatabaseBuilder builder = new DatabaseBuilder(file);
        builder.setAutoSync(false);
        builder.setCodecProvider(new CryptCodecProvider(s));
        builder.setReadOnly(true);
        builder.setCharset(Charset.forName("GB2312"));
        return builder.open();
    }

}
