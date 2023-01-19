package com.est.est100;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface JavaCallCppService extends Library {
	/**
	 * ����ʾ��Ϊest64.dll���泣�ú���,�������õ��Ŀ��ĵ�������д���ɣ�JDKΪ32λ��ע���est64�ĳ�est32
	 * 
	 * dll�ŵ�����Ŀ¼�²��У���.classpathһ������Ҳ���Էŵ�C:\WINDOWS\system32��,������ĸ�Ŀ¼���忴���˻���
	 * */
	JavaCallCppService INSTANCE = (JavaCallCppService) Native.loadLibrary(
			"est64", JavaCallCppService.class);

	
	// ��ָ���ĵ��Խӿ�.
	public int EU_Reader_Open(String data);
	// �ر��Ѵ򿪵ĵ��Խӿ�.
	public int EU_Reader_Close(int ReaderHandle);
	// ���� 0~10
	public int EU_PosBeep(int ReaderHandle, byte time);
	// ���ö�д��Ϊ�� typeA ��
	public int PEU_Reader_SetTypeA(int ReaderHandle);
	// ����Ƭ
	public int PEU_Reader_Request(int ReaderHandle);
	// ����ײ
	public int PEU_Reader_anticoll(int ReaderHandle, byte[] uid);
	// ѡ��Ƭ
	public int PEU_Reader_Select(int ReaderHandle, byte cardtype);
	
	// M1����֤��Կ���ú�����ʹ�ò���PassWord�����Key������֤��
	public int PEU_Reader_Authentication_Pass(int ReaderHandle, byte charMode,
			byte secNr, byte[] passWord);
	// ��ȡM1��Ƭ������
	public int PEU_Reader_Read(int ReaderHandle, byte addr, byte[] data);
	
	//�籣��
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
	
	//���֤
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