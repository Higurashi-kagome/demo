package com.chalco.holder.base;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import com.chalco.holder.R;
import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;

/**
 * NFC 监听基类
 */
public class BaseNfcActivity extends NfcFixActivity {
    /**
     * 添加“NFC 设置”
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.add(R.string.nfc_setting_label);
        return true;
    }

    /**
     * “NFC 设置”点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String title = String.valueOf(item.getTitle());
        String label1 = getResources().getString(R.string.nfc_setting_label);
        if (Validator.isNotBlank(title) && title.equals(label1)) {
            Utils.toNfcSetting(this);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 离开时清空 Tag Intent，解决有时候重复解析 Intent 的问题
     */
    @Override
    protected void onStop() {
        super.onStop();
        Intent intent = getIntent();
        if (intent == null) return;
        if (Validator.isNFCIntent(intent)) {
            Intent intent1 = new Intent();
            // 不能少，有的地方会回去 COMMAND_KEY 值
            intent1.putExtra(Constant.COMMAND_KEY, "BaseNfcActivity-onStop-empty-intent");
            setIntent(intent1);
        }
    }
}

