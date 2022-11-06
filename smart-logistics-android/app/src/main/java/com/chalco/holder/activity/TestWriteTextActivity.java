package com.chalco.holder.activity;

import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.chalco.holder.R;
import com.chalco.holder.base.BaseNfcActivity;
import com.chalco.holder.common.LogUtil;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;
import com.chalco.holder.common.WriteTaskUtils;
import com.chalco.holder.entity.NeedWriteResponse;

public class TestWriteTextActivity extends BaseNfcActivity implements NeedWriteResponse {
    private TextView dataTextView = null;
    /**
     * 写入到卡片中的数据
     */
    private String dataForWrite = null;

    private static final String extraKey = "dataForWrite";

    public static void to(Context ctx, String dataForWrite) {
        Intent intent = new Intent(ctx, TestWriteTextActivity.class);
        intent.putExtra(extraKey, dataForWrite);
        ctx.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_text);
        dataTextView = findViewById(R.id.nfc_data);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        Intent intent = getIntent();
        LogUtil.d(TAG,"onResume");
        if (Validator.isNFCIntent(intent)) {
            operateNFCIntent(intent);
        } else if (intent.hasExtra(extraKey)) {
            dataForWrite = intent.getStringExtra(extraKey);
            dataTextView.setText(dataForWrite);
        }
    }

    /**
     * 处理 NFC Intent
     * @param intent NFC Intent
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void operateNFCIntent(Intent intent) {
        if (Validator.isNotBlank(this.dataForWrite)){
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            NdefMessage ndefMessage = Utils.getNdefMessage(dataForWrite);
            new WriteTaskUtils.WriteTask(this, ndefMessage, tag).execute();
        } else {
            String info = "无待写入的卡数据，请重试";
            Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
            LogUtil.d(info);
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void processWriteResponse(Boolean output) {
        if (Validator.isTrue(output)) {
            finish();
            Toast.makeText(this, "写卡成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "未写入", Toast.LENGTH_SHORT).show();
        }
    }
}
