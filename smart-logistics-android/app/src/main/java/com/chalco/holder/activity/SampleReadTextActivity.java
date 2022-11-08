package com.chalco.holder.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.os.Build;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.chalco.holder.R;
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
public class SampleReadTextActivity extends BaseReadTextActivity implements NeedWriteResponse {

    private Tag mTag;

    private AlertDialog alertDialog;

    /**
     * 收货卡数据
     */
    private String dataInCard;

    /**
     * 跳转到 SampleReadTextActivity
     * @param ctx 出发点
     */
    public static void to(Context ctx) {
        Utils.jumpTo(ctx, SampleReadTextActivity.class);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(TAG, "onResume");
        Intent intent = getIntent();
        if (Validator.isNFCIntent(intent)) {
            LogUtil.d(TAG, "isNFCIntent = true");
            mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            dataInCard = getCardData(intent);
            if (dataInCard == null) return;
            if (Validator.isAcceptCardData(dataInCard)) {
                updateAcceptCard(dataInCard);
            } else {
                String info = "不是收货卡！";
                Toast.makeText(this, info, Toast.LENGTH_SHORT).show();
                LogUtil.d(info);
            }
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
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showSampledConfirmDialog(String dataInCard) {
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
                updateAcceptCard(dataInCard);
            });
        });
        alertDialog.show();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void updateAcceptCard(String dataInCard) {
        Intent intent = getIntent();
        if (!Validator.isNFCIntent(intent)) return;
        AcceptCardData acceptCardData = AcceptCardData.build(dataInCard);
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        acceptCardData.setCnt(acceptCardData.getCnt() + 1);
        String dataForWrite = acceptCardData.getCardStr();
        LogUtil.d(TAG, "updateAcceptCard - dataForWrite - " + dataForWrite);
        NdefMessage ndefMessage = Utils.getNdefMessage(dataForWrite);
        // Utils.sleep(200);
        new WriteTaskUtils.WriteTask(this, ndefMessage, tag).execute();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void processWriteResponse(Boolean output) {
        if (Validator.isTrue(output)) {
            //跳转到写（取样）卡界面
            LogUtil.d("写卡成功");
            String cardId = Utils.getCardId(mTag);
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
