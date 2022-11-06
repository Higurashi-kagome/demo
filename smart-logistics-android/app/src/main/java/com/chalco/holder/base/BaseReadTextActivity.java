package com.chalco.holder.base;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.widget.Toast;

import com.chalco.holder.R;
import com.chalco.holder.common.LogUtil;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;

public class BaseReadTextActivity extends BaseNfcActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_text);
    }

    /**
     * 获取卡片中的字符串数据
     * @param intent 检测到卡片后产生的意图
     * @return 卡片中的字符串数据，没获取到返回 null
     */
    protected String getCardData(Intent intent) {
        if (!Validator.isNFCIntent(intent)) return null;
        Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (mTag == null) return null;
        Ndef ndef = Ndef.get(mTag);
        if (ndef == null) {
            String info = "未读到卡数据";
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
            LogUtil.d(info);
        } else {
            String dataInCard = Utils.readNfcTag(intent);
            String info = "未读到，请重试";
            if (dataInCard == null) {
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                LogUtil.d(info);
            } else {
                return dataInCard;
            }
            Utils.closeNdef(ndef);
        }
        return null;
    }
}
