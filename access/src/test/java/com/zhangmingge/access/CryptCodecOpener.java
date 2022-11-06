package com.zhangmingge.access;

import com.healthmarketscience.jackcess.CryptCodecProvider;
import com.healthmarketscience.jackcess.Database;
import com.healthmarketscience.jackcess.DatabaseBuilder;
import net.ucanaccess.jdbc.JackcessOpenerInterface;

import java.io.File;
import java.io.IOException;

/**
 * 需要输入密码时使用<br>
 * <a href="https://stackoverflow.com/questions/59468649/ucaexc3-0-7-decoding-not-supported-please-choose-a-codecprovider-which-suppo">...</a>
 */
public class CryptCodecOpener implements JackcessOpenerInterface {
	public Database open(File fl, String pwd) throws IOException {
		DatabaseBuilder dbd = new DatabaseBuilder(fl);
		dbd.setAutoSync(false);
		dbd.setCodecProvider(new CryptCodecProvider(pwd));
		dbd.setReadOnly(false);
		return dbd.open();
	}
}