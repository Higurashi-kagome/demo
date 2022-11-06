package com.chalco.holder.base;

import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

import com.chalco.holder.common.LogUtil;
import com.chalco.holder.common.Utils;

public class BaseNfcReaderActivity extends BaseActivity {

    protected NfcAdapter.ReaderCallback mCallback = null;

    private NfcAdapter.ReaderCallback callback = this::onTagDiscovered;

    @Override
    public void onResume() {
        super.onResume();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.enableReaderMode(this, callback, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_NFC_B | NfcAdapter.FLAG_READER_NFC_F | NfcAdapter.FLAG_READER_NFC_V | NfcAdapter.FLAG_READER_NFC_BARCODE, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableReaderMode(this);
    }

    private void onTagDiscovered(Tag tag) {
        LogUtil.d(TAG, "get new tag");
        String cardId = Utils.bytesToHexString(tag.getId());
        String[] techList = tag.getTechList();
        System.out.println("cardId = " + cardId);
        System.out.println("techList = " + techList);
        if (mCallback != null) mCallback.onTagDiscovered(tag);
    }
}
