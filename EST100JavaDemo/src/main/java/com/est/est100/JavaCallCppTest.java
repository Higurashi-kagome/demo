package com.est.est100;

import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ByteUtil;
import cn.hutool.core.util.StrUtil;

import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JavaCallCppTest {
	
	
	public static void print_byteArray(byte[] src, int length) {
        for (int i = 0; i < length; ++i) {
            String hex = Integer.toHexString(src[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            System.out.print(hex.toUpperCase());
        }
    }
	
	public static void main(String[] args) {		

		//身份证
//		testReadIDCard();


		/*******************社保卡读取部分********************/
		/*int hReader = JavaCallCppService.INSTANCE.EU_Reader_Open("USB1");
	   	 System.out.println("hReader="+hReader);
	   	 
	   	 if(hReader <= 0){//    结果大于0表示成功 
	   		 System.err.println("==打开接口失败==hReader="+hReader+"=======状态码====="+hReader);
	   		 return;
	   	 }

		byte[] SBKH = new byte[100];
		byte[] XM = new byte[100];
		byte[] XB = new byte[100];
		byte[] MZ = new byte[100];
		byte[] CSRQ = new byte[100];
		byte[] SHBZHM = new byte[100];
		byte[] FKRQ = new byte[100];
		byte[] KYXQ = new byte[100];
		byte[] pErrMsg = new byte[100];
		
		int nRt = -99;
		nRt = JavaCallCppService.INSTANCE.PEU_Reader_SICARD(hReader, 
				SBKH,
				XM,
				XB,
				MZ,
				CSRQ,
				SHBZHM,
				FKRQ,
				KYXQ,
				pErrMsg);
		if(nRt != 0) {
			System.err.println("读卡失败，返回值："+nRt);
			return;
		}
		System.out.println("姓名："+  new String(XM));
		System.out.println("性別："+  new String(XB));
		System.out.println("民族："+  new String(MZ));
		System.out.println("出生日期："+  new String(CSRQ));
		System.out.println("社保卡号："+  new String(SBKH));
		System.out.println("身份证号："+  new String(SHBZHM));
		System.out.println("发卡日期："+  new String(FKRQ));
		System.out.println("卡有效期："+  new String(KYXQ));*/
		
		
		/*******************银行卡读取部分********************/
		/*int hReader = JavaCallCppService.INSTANCE.EU_Reader_Open("USB1");
	   	 System.out.println("hReader="+hReader);
	   	 
	   	 if(hReader <= 0){//    结果大于0表示成功 
	   		 System.err.println("==打开接口失败==hReader="+hReader+"=======状态码====="+hReader);
	   		 return;
	   	 }

		byte[] dst1 = new byte[100];
		byte[] dst2 = new byte[100];
		byte[] cardInfo = new byte[100];
		byte[] cardInfoLen = new byte[100];
		int iType = 1; //插卡方式  iType=1 插卡， iType=2感应
		
		int nRt = -99;
		nRt = JavaCallCppService.INSTANCE.PEU_Reader_CardInfo(hReader, 
				dst1,
				dst2,
				cardInfo,
				cardInfoLen,
				iType);
		if(nRt != 0) {
			System.err.println("读卡失败，返回值："+nRt);
			return;
		}
		System.out.println("卡信息："+  new String(cardInfo));*/
		
		
		
		/*******************M1卡读取部分********************/
		testReadM1();
		/******************* end M1卡读取部分********************/
    	 
    	 
		
     }

	private static void testReadM1() {
		byte[] anticollUid = new byte[10];

		int hReader = JavaCallCppService.INSTANCE.EU_Reader_Open("USB1");
		System.out.println(" hReader="+hReader);
		if(hReader <= 0){
			System.err.println("==打开接口失败,状态码:"+hReader);
			return;
		}
		while (true) {
			int request = JavaCallCppService.INSTANCE.PEU_Reader_Request(hReader);
			System.out.println(" request="+request);
			if(request != 0){
				System.err.println("==请求卡片接口失败==request=="+request+"=======状态码====="+request);
				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			} else break;
		}
		int anticoll = JavaCallCppService.INSTANCE.PEU_Reader_anticoll(hReader,anticollUid);
		System.out.println(" anticoll="+anticoll);
		if(anticoll != 0){//结果等于0 表示成功
			System.out.println("==防碰撞接口失败==anticoll=="+anticoll+"=======状态码====="+anticoll);
			return;
		}
		int select = JavaCallCppService.INSTANCE.PEU_Reader_Select(hReader, (byte) 0x41);
		System.out.println(" select="+select);
		if(select!=0){//结果等于0表示成功
			System.out.println("==选择接口失败==request=="+select+"=======状态码====="+select);
			return;
		}
//		---------------------
		byte[] allBytes = new byte[0];
		for (int b = 1, sector = 0; b < 64; b++) {
			// 每进入一个新的扇区，都要进行验证
			if ((b + 1) % 4 == 1) {
				sector++;
				byte[] key;
				if (b <= 3) { // 0 扇区
					key = new byte[]{ (byte)0xA0, (byte)0xA1, (byte)0xA2, (byte)0xA3, (byte)0xA4, (byte)0xA5 };
				} else { // 其他扇区
					key = new byte[]{ (byte) 0xd3, (byte) 0xf7, (byte) 0xd3, (byte) 0xf7, (byte) 0xd3, (byte) 0xf7};
				}
				int auPass = JavaCallCppService.INSTANCE.PEU_Reader_Authentication_Pass(hReader, (byte) 0x60, (byte) sector, key);
				System.out.println(" auPass="+auPass);
				if(auPass!=0){
					System.out.println("==验证接口失败==auPass=="+auPass+"=======状态码====="+auPass);
					return;
				}
			}
			// 每个扇区的第四块为特殊块，不存数据
			if ((b + 1) % 4 != 0) {
				byte[] data = new byte[16];
				int reader = JavaCallCppService.INSTANCE.PEU_Reader_Read(hReader, (byte) b, data);

				System.out.println(" reader="+reader);
				if(reader!=0){
					System.out.println("==读卡接口失败==reader=="+reader+"=======状态码====="+reader + " " + b);
					return;
				} else {
					allBytes = ArrayUtil.addAll(allBytes, data);
					if (ByteUtil.bytesToInt(data) == 0) {
						break;
					}
				}
			}
		}
		System.out.println("ASC:" + new String(allBytes));
		System.out.println("HEXSTR:");
		print_byteArray(allBytes, 16);
	}

	/*public byte[] readAllBytes() {
		byte[] allBytes = new byte[0];
		byte[] rdata = new byte[16];
		resultCode = rf.rf_authentication(icDev, (short) 0, (short) 0);
		if (resultCode != 0) {
			System.err.println("0 扇区密码验证错误！" + getErrCodeMsg());
		}
		// sector——扇区号（总共 16 个扇区）；b——块编号，从 0 开始（每个扇区 4 个块，16 个扇区，总共 64 块）；
		// 第 0 块数据被固化，不可写入，所以从块 1 开始读
		for (int b = 1, sector = 0; b < 64; b++) {
			// 每进入一个新的扇区，都要进行验证
			if ((b + 1) % 4 == 1) {
				sector++;
				resultCode = rf.rf_authentication(icDev, (short) 0, (short) sector);
				if (resultCode != 0) {
					System.err.println(sector + "扇区密码验证错误！" + getErrCodeMsg());
				}
			}
			// 每个扇区的第四块为特殊块，不存数据
			if ((b + 1) % 4 != 0) {
				resultCode = rf.rf_read(icDev, (short) b, rdata);
				if (resultCode == 0) {
					// System.out.println(HexUtil.encodeHex(rdata));
					allBytes = ArrayUtil.addAll(allBytes, rdata);
					// 如果某个块的数据全为 0 则认为之后的块没有存数据，直接跳过
					if (ByteUtil.bytesToInt(rdata) == 0) {
						break;
					}
				} else {
					System.err.println("读数据失败！块：" + b);
				}
			}
		}
		return allBytes;
	}*/

	private static void testReadIDCard() {
		int hReader = JavaCallCppService.INSTANCE.EU_Reader_Open("USB1");
		System.out.println("hReader="+hReader);
		if(hReader <= 0){//    结果大于0表示成功
			System.err.println("==打开接口失败==hReader="+hReader+"=======状态码====="+hReader);
			return;
		}
		JavaCallCppService.INSTANCE.EU_PosBeep(hReader, (byte) 2);
		String filepath = "C:\\Users\\liuhao\\Desktop\\EST100JavaDemo\\zp.bmp";
		byte[] pName = new byte[100];
		byte[] pSex = new byte[100];
		byte[] pNation = new byte[100];
		byte[] pBirth = new byte[100];
		byte[] pAddress = new byte[100];
		byte[] pCertNo = new byte[100];
		byte[] pDepartment = new byte[100];
		byte[] pEffectDate = new byte[100];
		byte[] pExpire = new byte[100];
		byte[] pErrMsg = new byte[100];

		int nRt = -99;
		nRt = JavaCallCppService.INSTANCE.PEU_Reader_ReadIDCard(hReader, pErrMsg);
		if(nRt != 0) {
			System.err.println("读卡失败，返回值："+nRt);
			return;
		}
		JavaCallCppService.INSTANCE.GetName(pName);
		JavaCallCppService.INSTANCE.GetSex(pSex);
		JavaCallCppService.INSTANCE.GetNation(pNation);
		JavaCallCppService.INSTANCE.GetBirth(pBirth);
		JavaCallCppService.INSTANCE.GetAddress(pAddress);
		JavaCallCppService.INSTANCE.GetCertNo(pCertNo);
		JavaCallCppService.INSTANCE.GetDepartemt(pDepartment);
		JavaCallCppService.INSTANCE.GetEffectDate(pEffectDate);
		JavaCallCppService.INSTANCE.GetExpireDate(pExpire);
		JavaCallCppService.INSTANCE.GetBmpFile(filepath);
		System.out.println("姓名："+ StrUtil.str(pName, Charset.forName("GBK")));
		System.out.println("性別："+  new String(pSex, Charset.forName("GBK")).substring(0,2));//性别这里截取一下，后面有个L
		System.out.println("民族："+  new String(pNation, Charset.forName("GBK")));
		System.out.println("出生日期："+  new String(pBirth, Charset.forName("GBK")));
		System.out.println("地址："+  new String(pAddress, Charset.forName("GBK")));
		String id = new String(pCertNo, Charset.forName("GBK"));
		Pattern re = Pattern.compile("^([a-zA-Z\\d]*)");
		Matcher matcher = re.matcher(id);
		if (matcher.find()) {
			System.out.println("身份证号：" + matcher.group(1));
		}
		System.out.println("签发机关："+  new String(pDepartment, Charset.forName("GBK")));
		System.out.println("发卡日期："+  new String(pEffectDate, Charset.forName("GBK")));
		System.out.println("有效期限："+  new String(pExpire, Charset.forName("GBK")));
	}
}
