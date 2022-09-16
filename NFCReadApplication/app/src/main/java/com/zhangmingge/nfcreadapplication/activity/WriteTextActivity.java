package com.zhangmingge.nfcreadapplication.activity;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.device.ScanManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.zhangmingge.nfcreadapplication.R;
import com.zhangmingge.nfcreadapplication.base.BaseNfcActivity;
import com.zhangmingge.nfcreadapplication.common.ScannerUtil;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

/**
 * Author:Created by Ricky on 2017/8/25.
 * Email:584182977@qq.com
 * Description:
 */
public class WriteTextActivity extends BaseNfcActivity {

    Tag detectedTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text);
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onNewIntent(Intent intent) {
        //获取Tag对象
        detectedTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        writeStr(detectedTag, "format");
    }

    private void writeStr(Tag detectedTag, String str) {
        NdefMessage ndefMessage = new NdefMessage(
                new NdefRecord[]{createTextRecord(str)});
        boolean result = writeTag(ndefMessage, detectedTag);
        if (result) {
            Toast.makeText(this, "写入成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "写入失败", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 创建NDEF文本数据
     *
     * @param text
     * @return
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(Charset.forName("US-ASCII"));
        Charset utfEncoding = Charset.forName("UTF-8");
        //将文本转换为UTF-8格式
        byte[] textBytes = text.getBytes(utfEncoding);
        //设置状态字节编码最高位数为0
        int utfBit = 0;
        //定义状态字节
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //设置第一个状态字节，先将状态码转换成字节
        data[0] = (byte) status;
        //设置语言编码，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1到langBytes.length的位置
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //设置文本字节，使用数组拷贝方法，从0开始拷贝到data中，拷贝到data的1 + langBytes.length
        //到textBytes.length的位置
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //通过字节传入NdefRecord对象
        //NdefRecord.RTD_TEXT：传入类型 读写
        NdefRecord ndefRecord = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
                NdefRecord.RTD_TEXT, new byte[0], data);
        return ndefRecord;
    }

    /**
     * 写数据
     *
     * @param ndefMessage 创建好的NDEF文本数据
     * @param tag         标签
     * @return
     */
    public static boolean writeTag(NdefMessage ndefMessage, Tag tag) {
        try {
            Ndef ndef = Ndef.get(tag);
            ndef.connect();
            ndef.writeNdefMessage(ndefMessage);
            return true;
        } catch (Exception e) {
        }
        return false;
    }
}
