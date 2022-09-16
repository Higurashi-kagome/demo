package com.zhangmingge.nfcreadapplication;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.zhangmingge.nfcreadapplication.activity.ReadTextActivity;
import com.zhangmingge.nfcreadapplication.activity.WriteTextActivity;
import com.zhangmingge.nfcreadapplication.base.BaseNfcActivity;

public class MainActivity extends BaseNfcActivity {
    private TextView ifo_NFC;
    private static final String[] strs = new String[]{
            "读NFC标签中的文本数据",
            "写NFC标签中的文本数据",
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ifo_NFC = (TextView) findViewById(R.id.ifo_NFC);
        // NFC适配器，所有的关于NFC的操作从该适配器进行
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!ifNFCUse()) {
            return;
        }
        ListView listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, strs));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switchActivity(position);
            }
        });
    }

    private void switchActivity(int position) {
        switch (position) {
            case 0: //读NFC标签中的文本数据
                startActivity(new Intent(this, ReadTextActivity.class));
                break;
            case 1: //写NFC标签中的文本数据
                startActivity(new Intent(this, WriteTextActivity.class));
                break;
            default:
                break;
        }
    }

    /**
     * 检测工作,判断设备的NFC支持情况
     *
     * @return
     */
    protected Boolean ifNFCUse() {
        if (mNfcAdapter == null) {
            ifo_NFC.setText("设备不支持NFC！");
            return false;
        }
        if (mNfcAdapter != null && !mNfcAdapter.isEnabled()) {
            ifo_NFC.setText("请在系统设置中先启用NFC功能！");
            return false;
        }
        return true;
    }
}

