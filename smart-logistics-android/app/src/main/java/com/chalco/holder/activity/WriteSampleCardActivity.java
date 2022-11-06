package com.chalco.holder.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.chalco.holder.R;
import com.chalco.holder.base.BaseNfcActivity;
import com.chalco.holder.common.Constant;
import com.chalco.holder.common.LogUtil;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;
import com.chalco.holder.common.WriteTaskUtils;
import com.chalco.holder.entity.AcceptCardData;
import com.chalco.holder.entity.NeedWriteResponse;
import com.chalco.holder.entity.OperationRecord;

/**
 * 写正样卡
 */
public class WriteSampleCardActivity extends BaseNfcActivity implements NeedWriteResponse {
    private TextView dataTextView = null;
    /**
     * 写入到卡片中的数据
     */
    private String dataForWrite = null;
    private String cardId;
    private AlertDialog alertDialog;

    public static void to(Context ctx, AcceptCardData acceptCardData, String cardId) {
        Intent writeIntent = new Intent(ctx, WriteSampleCardActivity.class);
        writeIntent.putExtra(Constant.ACCEPT_SALES_CARD_DATA, acceptCardData);
        // 传递 cardId，方便写入界面确定检测到的卡是否为被读取的卡
        writeIntent.putExtra(Constant.CARD_ID, cardId);
        ctx.startActivity(writeIntent);
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
        LogUtil.d(TAG,"onResume");
        Intent intent = getIntent();
        // 采样登记
        AcceptCardData acceptCardData = (AcceptCardData) intent.getSerializableExtra(Constant.ACCEPT_SALES_CARD_DATA);
        if (acceptCardData != null) {
            String sampleCardData = Utils.getSampleCardData(acceptCardData.getCardStr());
            this.dataForWrite = sampleCardData;
            cardId = intent.getStringExtra(Constant.CARD_ID);
        }
        if (Validator.isNFCIntent(intent)) operateNFCIntent(intent);
    }

    /**
     * 处理 NFC Intent
     * @param intent NFC Intent
     */
    private void operateNFCIntent(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (Validator.isNotBlank(this.dataForWrite)){
            // 如果传递了 cardId，则检查被读卡是否与当前卡相同，相同则等待用户确认
            if (Validator.isNotBlank(cardId)) {
                String wCardId = Utils.getCardId(tag);
                if (cardId.equals(wCardId)) {
                    Toast.makeText(this, "该卡为被读卡", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
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
        // 防止用户误操作直接退出，错过写卡
        if (alertDialog != null && alertDialog instanceof AlertDialog) alertDialog.dismiss();
        String title = "暂未写入卡片，确认退出？";
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setNegativeButton("否", (dialog, which) -> {
                    dialog.dismiss();
                    LogUtil.d(title + "：否 dataForWrite: " + dataForWrite);
                })
                .setPositiveButton("是", (dialog, which) -> {
                    dialog.dismiss();
                    LogUtil.d(title + "：是 dataForWrite: " + dataForWrite);
                    finish();
                }).create();
        alertDialog.setOnShowListener(dialog -> Utils.initDialogButtonTextSize(alertDialog));
        alertDialog.show();
    }

    @Override
    public void processWriteResponse(Boolean output) {
        if (Validator.isTrue(output)){
            Toast.makeText(this, "写卡成功", Toast.LENGTH_SHORT).show();
            LogUtil.d("写卡成功 - dataForWrite: " + dataForWrite);
            SampleReadTextActivity.to(this);
            finish();
        } else {
            Toast.makeText(this, "未写入，请重试", Toast.LENGTH_SHORT).show();
            LogUtil.d("写卡失败 - dataForWrite: " + dataForWrite);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sample_write_text, menu);
        return true;
    }
}
