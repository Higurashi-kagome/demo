package com.est.est100;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface JavaCallCppService extends Library {
	/**
	 * 以下示例为est64.dll里面常用函数,其余有用到的看文档对照着写即可，JDK为32位的注意把est64改成est32
	 * 
	 * dll放到工程目录下才行（与.classpath一级），也可以放到C:\WINDOWS\system32下,具体放哪个目录具体看个人环境
	 * */
	JavaCallCppService INSTANCE = (JavaCallCppService) Native.loadLibrary(
			"est64", JavaCallCppService.class);

	
	// 打开指定的电脑接口.
	public int EU_Reader_Open(String data);
	// 关闭已打开的电脑接口.
	public int EU_Reader_Close(int ReaderHandle);
	// 蜂鸣 0~10
	public int EU_PosBeep(int ReaderHandle, byte time);
	// 设置读写器为读 typeA 卡
	public int PEU_Reader_SetTypeA(int ReaderHandle);
	// 请求卡片
	public int PEU_Reader_Request(int ReaderHandle);
	// 防碰撞
	public int PEU_Reader_anticoll(int ReaderHandle, byte[] uid);
	// 选择卡片
	public int PEU_Reader_Select(int ReaderHandle, byte cardtype);
	
	// M1卡认证密钥，该函数将使用参数PassWord传入的Key进行认证；
	public int PEU_Reader_Authentication_Pass(int ReaderHandle, byte charMode,
			byte secNr, byte[] passWord);
	// 读取M1卡片块数据
	public int PEU_Reader_Read(int ReaderHandle, byte addr, byte[] data);
	
	//社保卡
	public int PEU_Reader_SICARD(int ReaderHandle,
			byte[] SBKH,
			byte[] XM,
			byte[] XB,
			byte[] MZ,
			byte[] CSRQ,
			byte[] SHBZHM,
			byte[] FKRQ,
			byte[] KYXQ,
			byte[] pErrMsg);
	
	//身份证
	public int PEU_Reader_ReadIDCard(int ReaderHnalde, byte[] errMsg);
	public int GetName(byte[] outPutData);
	public int GetSex(byte[] outPutData);
	public int GetNation(byte[] outPutData);
	public int GetBirth(byte[] outPutData);
	public int GetAddress(byte[] outPutData);
	public int GetCertNo(byte[] outPutData);
	public int GetDepartemt(byte[] outPutData);
	public int GetEffectDate(byte[] outPutData);
	public int GetExpireDate(byte[] outPutData);
	public int GetBmpFile(String bmpFileName);
	public int GetCardType();
	
	public int PEU_Reader_CardInfo(int ReaderHandle,
			byte[] dst1 , 
			byte[] dst2,
			byte[] cardInfo,
			byte[] cardInfoLen,
			int t_IntMode);
	

}