package com.chalco.holder.base;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.view.View;

import com.chalco.holder.R;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;
import com.google.android.material.snackbar.Snackbar;

import java.util.Arrays;

/**
 * 继承该类，避免标签靠近时跳转到其他标签活动（比如系统自带的标签检测）
 */
public class NfcFixActivity extends BaseActivity {
    protected NfcAdapter mNfcAdapter;
    protected PendingIntent mPendingIntent;

    @Override
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (Validator.isNFCIntent(intent)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            System.out.println("action = " + intent.getAction());
            System.out.println("tag.getTechList() = " + Arrays.toString(tag.getTechList()));
        }
    }

    /**
     * onCreat->onStart->onResume->onPause->onStop->onDestroy
     * 启动Activity，界面可见时.
     */
    @Override
    protected void onStart() {
        super.onStart();
        //此处adapter需要重新获取，否则无法获取message
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //一旦截获NFC消息，就会通过PendingIntent调用窗口
        mPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
    }

    /**
     * 获得焦点，按钮可以点击
     */
    @Override
    public void onResume() {
        super.onResume();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        //设置处理优于所有其他NFC的处理
        if (mNfcAdapter != null)
        {
            if (!mNfcAdapter.isEnabled()) {
                View parentView = findViewById(R.id.snackbar_parent);
                if (parentView == null) return;
                Snackbar.make(parentView, "NFC未打开", Snackbar.LENGTH_LONG).setAction("去开启", v -> {
                    //点击右侧的按钮之后，跳转到 NFC 开启页面
                    Utils.toNfcSetting(this);
                }).show();
            } else {
                mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, null, null);
            }
        }
    }

    /**
     * 暂停Activity，界面获取焦点，按钮可以点击
     */
    @Override
    public void onPause() {
        super.onPause();
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (mNfcAdapter != null) {
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }
}

