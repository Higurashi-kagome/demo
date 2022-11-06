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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.chalco.holder.R;
import com.chalco.holder.base.BaseNfcReaderActivity;
import com.chalco.holder.base.BaseReadTextActivity;
import com.chalco.holder.common.LogUtil;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;
import com.chalco.holder.common.WriteTaskUtils;
import com.chalco.holder.entity.AcceptCardData;
import com.chalco.holder.entity.NeedWriteResponse;

/**
 * 采样登记时读收货卡
 */
public class SampleReadTextActivity extends BaseNfcReaderActivity implements NeedWriteResponse {

    private Tag mTag;

    private AlertDialog alertDialog;

    /**
     * 收货卡数据
     */
    private String dataInCard;
    private String cardId;

    /**
     * 跳转到 SampleReadTextActivity
     * @param ctx 出发点
     */
    public static void to(Context ctx) {
        Utils.jumpTo(ctx, SampleReadTextActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_text);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
        mCallback = this::operateTag;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void operateTag(Tag tag) {
        LogUtil.d(TAG, "isNFCIntent = true");
        cardId = Utils.getCardId(mTag);
        dataInCard = Utils.readNfcTag(tag);
        if (dataInCard == null) {
            String info = "未读到";
            System.out.println("info = " + info);
            return;
        }
        if (Validator.isAcceptCardData(dataInCard)) {
            // 先判断是否完成两次采样
            if (Validator.isAcceptExhausted(dataInCard)) {
                String info = "该卡已完成两次采样";
                System.out.println("info = " + info);
                LogUtil.d(info);
                return;
            }
            // 再判断是否采样过
            if (Validator.canWriteAsAccept(dataInCard)) {
                showSampledConfirmDialog(dataInCard, tag);
                return;
            }
            updateAcceptCard(dataInCard, tag);
        } else {
            String info = "不是收货卡！";
            System.out.println("info = " + info);
            LogUtil.d(info);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (alertDialog != null && alertDialog instanceof AlertDialog) alertDialog.dismiss();
    }

    /**
     * 提示收货卡取样过一次
     * @param dataInCard
     * @param tag
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showSampledConfirmDialog(String dataInCard, Tag tag) {
        if (alertDialog != null && alertDialog instanceof AlertDialog) alertDialog.dismiss();
        String title = "该卡已完成一次采样，确认继续进行采样登记？";
        alertDialog = new AlertDialog.Builder(this)
                .setTitle(title)
                .setPositiveButton("确定", null)
                .setNegativeButton("取消", (dialog, which) -> {
                    LogUtil.d(title + "取消");
                    dialog.dismiss();
                })
                .setOnCancelListener(dialog -> dialog.dismiss()).create();
        alertDialog.setOnShowListener(dialog -> {
            Utils.initDialogButtonTextSize(alertDialog);
            Button button = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);
            button.setOnClickListener(view -> {
                LogUtil.d(title + "确定 - dataInCard: " + dataInCard);
                updateAcceptCard(dataInCard, tag);
            });
        });
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAcceptCard(String dataInCard, Tag mTag) {
        if (mTag == null) return;
        AcceptCardData acceptCardData = AcceptCardData.build(dataInCard);
        acceptCardData.setCnt(acceptCardData.getCnt() + 1);
        String dataForWrite = acceptCardData.getCardStr();
        LogUtil.d(TAG, "updateAcceptCard - dataForWrite - " + dataForWrite);
        NdefMessage ndefMessage = Utils.getNdefMessage(dataForWrite);
        new WriteTaskUtils.WriteTask(this, ndefMessage, mTag).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processWriteResponse(Boolean output) {
        if (Validator.isTrue(output)) {
            //跳转到写（取样）卡界面
            LogUtil.d("写卡成功");
            AcceptCardData acceptCardData = AcceptCardData.build(dataInCard);
            WriteSampleCardActivity.to(this, acceptCardData, cardId);
            LogUtil.d("跳转到写卡界面");
            // 不结束活动可能会出现多次读取的情况
            finish();
        } else {
            Toast.makeText(this, "未写入，请重试", Toast.LENGTH_SHORT).show();
        }
        if (alertDialog != null && alertDialog instanceof AlertDialog) alertDialog.dismiss();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.sample_read_text, menu);
        return true;
    }

    /**
     * 补卡菜单
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rewrite_accept_card:
                TestWriteTextActivity.to(this, "{123,3,0,v9,桂A66824,40.56,2022-10-19 20:48:39}");
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
