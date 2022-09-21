package api;

import com.sun.jna.Library;
//import com.sun.jna.Native;

public class Declare
{
	/**
	 * RFID 读写器（Radio Frequency Identification）API
	 */
	public interface RFID extends Library{
		/**
		 * 初始化串口
		 * @param port 串口号，取值为0～3
		 * @param buad 为通讯波特率9600～115200
		 * @return 成功则返回串口标识符>0，失败返回负值
		 */
		public int rf_init(short port,long buad);

		/**
		 * 中止对该卡操作<br>
		 * 执行该命令后如果是ALL寻卡模式则必须重新寻卡才能够对该卡操作，如果是IDLE模式则必须把卡移开感应区再进来才能寻得这张卡。
		 * @param icdev 通讯设备标识符
		 * @return 成功则返回0
		 */
		public int rf_halt(int icdev);

		/**
		 * 释放串口
		 * @param icdev 通讯设备标识符
		 * @return
		 */
		public short rf_exit(int icdev);

		/**
		 * 取得读写器硬件版本号，如“mwrf100_v3.0”
		 * @param icdev 通讯设备标识符
		 * @param ver 返回版本信息
		 * @return 成功则返回 0
		 */
		public short rf_get_status(int icdev,byte[] ver);

		/**
		 * 蜂鸣
		 * @param icdev 通讯设备标识符
		 * @param time 蜂鸣时间，单位是10毫秒
		 * @return 成功则返回 0
		 */
		public short rf_beep(int icdev,short time);

		/**
		 * 将密码装入读写模块RAM中
		 * @param icdev 通讯设备标识符
		 * @param mode 装入密码模式，同密码验证模式
		 * @param sector 扇区号（M1卡：0～15；  ML卡：0）
		 * @param key 写入读写器中的卡密码
		 * @return 成功则返回 0
		 */
		public short rf_load_key(int icdev,short mode,short sector,byte[] key);
		public short rf_load_key_hex(int icdev,short mode,short sector,String key);

		/**
		 * 寻卡，能返回在工作区域内某张卡的序列号
		 * @param icdev 通讯设备标识符
		 * @param mode 寻卡模式<br>
		 *              0——表示IDLE模式，一次只对一张卡操作；<br>
		 *    			1——表示ALL模式，一次可对多张卡操作；<br>
		 *    			2——表示指定卡模式，只对序列号等于snr的卡操作（高级函数才有）
		 * @param Snr 返回的卡序列号
		 * @return 成功则返回 0
		 */
		public short rf_card(int icdev,short mode,byte[] Snr);

		/**
		 * 验证某一扇区密码
		 * @param icdev 通讯设备标识符
		 * @param mode 密码验证模式
		 * @param sector 要验证密码的扇区号（0～15）
		 * @return 成功则返回 0
		 */
		public short rf_authentication(int icdev,short mode,short sector);

		/**
		 * 读取卡中数据<br>
		 * 	对于M1卡，一次读一个块的数据，为16个字节；<br>
		 * 	对于ML卡，一次读出相同属性的两页（0和1，2和3，...），为8个字节<br>
		 * @param icdev 通讯设备标识符
		 * @param addr M1卡——块地址（0～63）；ML卡——页地址（0～11）
		 * @param data 读出数据
		 * @return 成功则返回 0
		 */
		public short rf_read(int icdev,short addr,byte[] data);
		public short rf_read_hex(int icdev,short addr,byte[] data);

		/**
		 * 高级读函数
		 * @param icdev 通讯设备标识符
		 * @param mode 寻卡模式
		 * @param adr 块地址（0～63）
		 * @param Snr 卡的序列号
		 * @param _Data 读出的数据
		 * @param _NSnr 返回卡的序列号
		 * @return
		 */
		public short rf_HL_read(int icdev,short mode, short adr, byte[] Snr, byte[] _Data,  byte[] _NSnr);

		/**
		 * 向卡中写入数据<br>
		 *  对于M1卡，一次必须写一个块，为16个字节；<br>
		 *  对于ML卡，一次必须写一页，为4个字节<br>
		 * @param icdev 通讯设备标识符
		 * @param addr M1卡——块地址（0～63）；ML卡——页地址（0～11）
		 * @param data 要写入的数据
		 * @return 成功则返回 0
		 */
		public short rf_write(int icdev,short addr,byte[] data);
		public short rf_write_hex(int icdev,short addr,byte[] data);

		/**
		 * 修改块3的数据
		 * @param icdev 通讯设备标识符
		 * @param SecNr 扇区号（0～15）
		 * @param KeyA 密码A
		 * @param _B0 块0控制字，低3位（D2D1D0）对应C10、C20、C30
		 * @param _B1 块1控制字，低3位（D2D1D0）对应C11、C21、C31
		 * @param _B2 块2控制字，低3位（D2D1D0）对应C12、C22、C32
		 * @param _B3 块3控制字，低3位（D2D1D0）对应C13、C23、C33
		 * @param _Bk 保留参数，取值为0
		 * @param _KeyB 密码B
		 * @return 成功则返回 0
		 */
		public short rf_changeb3(int icdev,short SecNr,byte[] KeyA,short _B0,short _B1,short _B2,short _B3,short _Bk,byte[] _KeyB);

		/**
		 * 双界面卡非接触界面复位
		 * @param icdev
		 * @param _Data
		 * @return
		 */
		public short rf_pro_rst(int icdev, byte[] _Data);

		/**
		 * 双界面卡非接触界面发送指令
		 * @param icdev 通讯设备标识符
		 * @param problock 发送信息<br>
		 *
		 *           problock[0]     NAD(结点地址，一般为0)<br>
		 *
		 *           problock[1]     CID(卡标识符)<br>
		 *
		 *           problock[2]     pcb(协议控制字节，由卡片通讯协议决定)<br>
		 *
		 *           problock[3]     len(指令信息长度)<br>
		 *
		 *           problock[4]     指令信息<br>
		 *
		 *               .             .<br>
		 *
		 *               .             .<br>
		 *
		 *           problock[len+4] 指令信息<br>
		 * @param recv 返回的指令应答信息<br>
		 *
		 *        recv[0]      NAD<br>
		 *
		 *        recv[1]      CID（用于访问指定的卡片，目前不支持）<br>
		 *
		 *        recv[2]      pcb(由卡片和读写器的通讯协议决定)<br>
		 *
		 *        recv[3]      len(应答信息长度)<br>
		 *
		 *        recv[4]      应答信息<br>
		 *
		 *           .             .<br>
		 *
		 *           .             .<br>
		 *
		 *        recv[len+4]  应答信息<br>
		 * @return 成功则返回 0
		 */
		public short  rf_pro_trn(int icdev, byte[] problock,byte[] recv);

		/**
		 * 将 16 进制数转换为 ASCII 字符
		 * @param hex 16 进制数
		 * @param a 输出的 ASCII 字符
		 * @param len 16 进制数的长度
		 * @return 成功则返回 0
		 */
		public short hex_a(byte[] hex,byte[] a,short len);

		/**
		 * 将 ASCII 字符转换为 16 进制数
		 * @param a ASCII 字符
		 * @param hex 输出的 16 进制数
		 * @param len ASCII 字符的长度
		 * @return 成功则返回 0
		 */
		public short a_hex(byte[] a,byte[] hex,short len);
	}
}
