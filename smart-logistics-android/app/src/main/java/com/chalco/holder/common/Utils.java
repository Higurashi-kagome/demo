package com.chalco.holder.common;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Triggering;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.nfc.tech.NfcA;
import android.os.Build;
import android.os.Parcelable;
import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import com.chalco.holder.entity.AcceptCardData;
import com.chalco.holder.entity.DriverInfoAndInput;
import com.chalco.holder.entity.OperationRecord;
import com.chalco.holder.entity.SampleOrSampledCardData;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class Utils {
    /**
     * byte[] toHex String
     *
     * @param src
     * @return String
     */
    public static String bytesToHexString(byte[] src) {
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (int i = 0; i < src.length; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    /**
     * ????????????
     * @param dialog ???????????????????????? Dialog
     */
    public static void showSoftInput(Dialog dialog) {
        if (dialog == null) return;
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
    }

    /**
     * ?????????????????? cxt ?????? activity
     * @param cxt ???????????????
     * @param activity ?????????????????????
     */
    public static void jumpTo(Context cxt, Class activity) {
        Intent intent = new Intent(cxt, activity);
        cxt.startActivity(intent);
    }

    /**
     * ?????????????????? cxt ?????? activity?????????????????????
     * @param cxt ???????????????
     * @param activity ?????????????????????
     * @param command ???????????????
     */
    public static void jumpToWithCommand(Context cxt, Class activity, String command) {
        Intent intent = new Intent(cxt, activity);
        intent.putExtra(Constant.COMMAND_KEY, command);
        cxt.startActivity(intent);
    }

    /**
     * ?????????????????? cxt ?????? intent?????????????????????
     * @param cxt ???????????????
     * @param intent ???????????? Intent
     * @param command ???????????????
     */
    public static void jumpToWithCommand(Context cxt, Intent intent, String command) {
        intent.putExtra(Constant.COMMAND_KEY, command);
        cxt.startActivity(intent);
    }

    /**
     * MD5 ??????
     * @param str ?????????????????????
     */
    public static String getMD5Str(String str) {
//        byte[] digest = null;
//        try {
//            MessageDigest md5 = MessageDigest.getInstance("md5");
//            digest  = md5.digest(str.getBytes("utf-8"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        //16??????????????????16?????????
//        String md5Str = new BigInteger(1, digest).toString(16);
//        return md5Str;
        try{
            byte[] bytesOfMessage = str.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] theMD5digest = md.digest(bytesOfMessage);
            String hexString = Utils.bytesToHexString(theMD5digest);
            return hexString;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void sleep(int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void closeNdef(Ndef ndef) {
        if (ndef == null) return;
        try {
            if (ndef.isConnected()) ndef.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    public static NdefMessage getNdefMessage(String str) {
        NdefRecord[] records = new NdefRecord[]{NdefRecord.createTextRecord(Locale.CHINA.getLanguage(), str)};
        return new NdefMessage(records);
    }

    /**
     * ??????NDEF????????????
     */
    public static NdefRecord createTextRecord(String text) {
        byte[] langBytes = Locale.CHINA.getLanguage().getBytes(StandardCharsets.US_ASCII);
        Charset utfEncoding = StandardCharsets.UTF_8;
        //??????????????????UTF-8??????
        byte[] textBytes = text.getBytes(utfEncoding);
        //???????????????????????????????????????0
        int utfBit = 0;
        //??????????????????
        char status = (char) (utfBit + langBytes.length);
        byte[] data = new byte[1 + langBytes.length + textBytes.length];
        //????????????????????????????????????????????????????????????
        data[0] = (byte) status;
        //???????????????????????????????????????????????????0???????????????data???????????????data???1???langBytes.length?????????
        System.arraycopy(langBytes, 0, data, 1, langBytes.length);
        //???????????????????????????????????????????????????0???????????????data???????????????data???1 + langBytes.length
        //???textBytes.length?????????
        System.arraycopy(textBytes, 0, data, 1 + langBytes.length, textBytes.length);
        //??????????????????NdefRecord??????
        //NdefRecord.RTD_TEXT??????????????? ??????
        return new NdefRecord(NdefRecord.TNF_WELL_KNOWN, NdefRecord.RTD_TEXT, new byte[0], data);
    }

    /**
     * ???????????????
     * @param cardData ???????????????????????????
     * @return Constant.CardType ?????????
     */
    public static int getCardType(String cardData) {
        if (cardData == null) return Constant.CardType.unknown;
        if (Validator.isAcceptCardData(cardData)) return Constant.CardType.accept;
        if (Validator.isSampleCardData(cardData)) return Constant.CardType.sample;
        if (Validator.isSampledCardData(cardData)) return Constant.CardType.sampled;
        if (Validator.isSamplingCardData(cardData)) return Constant.CardType.sampling;
        return Constant.CardType.unknown;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void saveHistory(Context ctx, DriverInfoAndInput driverInfoAndInput, String mCommand) {
        OperationRecord record;
        if (driverInfoAndInput != null){
            record = OperationRecord.build(driverInfoAndInput);
        } else { // ?????????????????????????????? nfcData == null
            record = new OperationRecord();
        }
        record.setOperationName(mCommand);
        SpUtils.saveOperationRecord(ctx, record);
    }

    /**
     * ????????????id?????????
     * @param intent ?????????????????????????????????
     * @return ??????id?????????????????????????????? null
     */
    public static String getCardId(Intent intent) {
        Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        byte[] idBytes = mTag.getId();
        String id = bytesToHexString(idBytes);
        if (Validator.isNotBlank(id)) return id;
        return null;
    }

    /**
     * ????????????id?????????
     * @param tag ?????????????????????????????????
     * @return ??????id?????????????????????????????? null
     */
    public static String getCardId(Tag tag) {
        if (tag == null) return null;
        byte[] idBytes = tag.getId();
        String id = bytesToHexString(idBytes);
        if (Validator.isNotBlank(id)) return id;
        return null;
    }

    /**
     * ????????????????????????????????????
     * @param salesCardData ???????????????
     * @return ???????????????
     */
    @NonNull
    public static String getSampleCardData(String salesCardData) {
        if (!Validator.isAcceptCardData(salesCardData)) return null;
        AcceptCardData acceptCardData = AcceptCardData.build(salesCardData);
        SampleOrSampledCardData sampleOrSampledCardData = new SampleOrSampledCardData();
        sampleOrSampledCardData.setPlateNo(acceptCardData.getPlateNo());
        sampleOrSampledCardData.setGrossWeight(acceptCardData.getGrossWeight());
        sampleOrSampledCardData.setOrderCode(acceptCardData.getOrderCode());
        sampleOrSampledCardData.setMaterialName(acceptCardData.getMaterialName());
        sampleOrSampledCardData.setTransportMaterialId(acceptCardData.getTransportMaterialId());
        sampleOrSampledCardData.setDate(acceptCardData.getDate());
        sampleOrSampledCardData.setCustomerCoder(acceptCardData.getCustomerCoder());
        return sampleOrSampledCardData.getCardStr();
    }

    /**
     * ???????????????????????????
     */
    public static void playSuccessRing(Context ctx) {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        Ringtone r = RingtoneManager.getRingtone(ctx, notification);
        r.play();
    }

    /**
     * ????????????????????????????????????????????????transportId
     * @param cardData ???????????????????????????
     * @return ??????????????? null
     */
    public static String getTransportIdFromCard(String cardData) {
        if (!Validator.isNotBlank(cardData)) return null;
        if (getCardType(cardData) == Constant.CardType.unknown) return null;
        cardData = cardData.substring(1, cardData.length() - 1);
        String[] split = cardData.split(",");
        if (split.length > 0) return split[0];
        return null;
    }

    /**
     * TODO ???????????????????????????
     * ??????NFC??????????????????
     */
    public static String readNfcTag(Intent intent) {
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (parcelables == null) return null;
            NdefMessage messages = (NdefMessage) parcelables[0];
            NdefRecord record = messages.getRecords()[0];
            return parseTextRecord(record);
        }
        return null;
    }

    /**
     * ???????????????????????????
     * @param intent
     */
    public void readFromTag(Intent intent){
        Tag mTag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (mTag == null) return;
        Ndef ndef = Ndef.get(mTag);
        if (ndef == null) return;
        try{
            if (!ndef.isConnected()) ndef.connect();
            Parcelable[] messages = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (messages != null) {
                NdefMessage[] ndefMessages = new NdefMessage[messages.length];
                for (int i = 0; i < messages.length; i++) {
                    ndefMessages[i] = (NdefMessage) messages[i];
                }
                NdefRecord record = ndefMessages[0].getRecords()[0];
                byte[] payload = record.getPayload();
                String text = new String(payload);
                ndef.close();
            }
        }catch (Exception e) {
           e.printStackTrace();
        }
    }

    /**
     * ??????NDEF???????????????????????????????????????????????????????????????
     */
    private static String parseTextRecord(NdefRecord ndefRecord) {
        //?????????????????????NDEF??????
        if (ndefRecord.getTnf() != NdefRecord.TNF_WELL_KNOWN) {
            return null;
        }
        //??????????????????????????????
        if (!Arrays.equals(ndefRecord.getType(), NdefRecord.RTD_TEXT)) {
            return null;
        }
        try {
            //???????????????????????????????????????
            byte[] payload = ndefRecord.getPayload();
            //????????????NDEF??????????????????????????????????????????
            //?????????????????????UTF-8??????UTF-16????????????????????????"??????"???16?????????80???16?????????80?????????????????????1???
            //???????????????0???????????????"??????"??????????????????????????????
            String textEncoding = ((payload[0] & 0x80) == 0) ? "UTF-8" : "UTF-16";
            //3f???????????????0???????????????1???????????????"??????"????????????????????????
            int languageCodeLength = payload[0] & 0x3f;
            //????????????NDEF??????????????????????????????????????????
            //??????????????????
            String languageCode = new String(payload, 1, languageCodeLength, StandardCharsets.US_ASCII);
            //????????????NDEF?????????????????????????????????????????????
            String s = new String(payload, languageCodeLength + 1, payload.length - languageCodeLength - 1, textEncoding);
            return s;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * ????????????????????????
     * @param dataInCard ???????????????
     * @return ???????????????
     */
    public static String getAcceptCardCnt(String dataInCard) {
        if (!Validator.isNotBlank(dataInCard)) return null;
        if (!Validator.isAcceptCardData(dataInCard)) return null;
        String[] split = dataInCard.split(",");
        if (split.length > 2) return split[2];
        return null;
    }

    /**
     * ????????? NFC ????????????
     * @param ctx ?????????
     */
    public static void toNfcSetting(Context ctx) {
        Intent intent;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        } else {
            intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
        }
        ctx.startActivity(intent);
    }

    public static void initDialogButtonTextSize(AlertDialog alertDialog) {
        Button btnPositive = alertDialog.getButton(Dialog.BUTTON_POSITIVE);
        btnPositive.setTextSize(Constant.DIALOG_BUTTON_TEXT_SIZE);
        Button btnNegative = alertDialog.getButton(Dialog.BUTTON_NEGATIVE);
        btnNegative.setTextSize(Constant.DIALOG_BUTTON_TEXT_SIZE);
    }

    public static boolean eraseTag(Tag tag) {
        Ndef ndef = Ndef.get(tag);
        try {
            NdefMessage message = Utils.getNdefMessage("-");
            if (ndef != null) {
                ndef.connect();
                ndef.writeNdefMessage(message);
                return true;
            } else {
                NdefFormatable ndefFormatable = NdefFormatable.get(tag);
                if (ndefFormatable != null) {
                    try {
                        ndefFormatable.connect();
                        ndefFormatable.format(message);
                        return true;
                    } catch (Exception e2) {
                        e2.printStackTrace();
                    } finally {
                        ndefFormatable.close();
                    }
                }
            }
        } catch (FormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Utils.closeNdef(ndef);
        }
        return false;
    }

    public static void toPackage(Context ctx, String packageName) {
        PackageManager packageManager = ctx.getPackageManager();
        if (checkPackInfo(ctx, packageName)) {
            Intent intent = packageManager.getLaunchIntentForPackage(packageName);
            ctx.startActivity(intent);
        } else {
            Toast.makeText(ctx, "????????????" + packageName, Toast.LENGTH_SHORT).show();
        }
    }

    public static void readNfcA(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        NfcA nfcA = NfcA.get(tag);
        if (nfcA == null) return;
        try {
            nfcA.connect();
            byte[] result = nfcA.transceive(new byte[] {
                    (byte)0x30,  // READ
                    (byte)(4 & 0x0ff)
            });
            System.out.println("result = " + result);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (nfcA != null) nfcA.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void readM1(Intent intent) {
        MifareClassic mif = MifareClassic.get(intent.getParcelableExtra(NfcAdapter.EXTRA_TAG));
        int ttype = mif.getType();
        int tsize = mif.getSize();
        int s_len = mif.getSectorCount();
        int b_len = mif.getBlockCount();
        try {
            mif.connect();
            if (mif.isConnected()){
                for(int i=0; i< s_len; i++){
                    boolean isAuthenticated = false;

                    if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_MIFARE_APPLICATION_DIRECTORY)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i, MifareClassic.KEY_DEFAULT)) {
                        isAuthenticated = true;
                    } else if (mif.authenticateSectorWithKeyA(i,MifareClassic.KEY_NFC_FORUM)) {
                        isAuthenticated = true;
                    } else {
                        Log.d("TAG", "Authorization denied " + i);
                    }
                    if(isAuthenticated) {
                        int block_index = mif.sectorToBlock(i);
                        byte[] block = mif.readBlock(block_index);
                        String s_block = Utils.bytesToHexString(block);
                        System.out.println("s_block = " + s_block);
                    }
                }
            }
            mif.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean checkPackInfo(Context ctx, String packageName) {
        PackageInfo packageInfo = null;
        try {
            packageInfo = ctx.getPackageManager().getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packageInfo != null;
    }

    public static ScanManager getScanManger(Context ctx) {
        ScanManager mScanManager = new ScanManager();
        if (!mScanManager.getScannerState()) {
            if (!mScanManager.openScanner()) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                builder.setMessage("?????????????????????");
                builder.setPositiveButton("??????", (dialog, which) -> dialog.dismiss());
                AlertDialog mAlertDialog = builder.create();
                Utils.initDialogButtonTextSize(mAlertDialog);
                mAlertDialog.show();
                return null;
            }
        }

        // ?????????
        mScanManager.setParameterInts(new int[]{PropertyID.TRIGGERING_LOCK}, new int[]{0});
        // intent ??????
        mScanManager.switchOutputMode(0);
        // Host ??????
        mScanManager.setTriggerMode(Triggering.PULSE);
        // ?????????????????????
        int[] ids = new int[]{PropertyID.SEND_GOOD_READ_BEEP_ENABLE, PropertyID.SEND_GOOD_READ_VIBRATE_ENABLE};
        int[] values = new int[]{2, 1};
        mScanManager.setParameterInts(ids, values);

        // ????????????
        boolean triggerLockState = mScanManager.getTriggerLockState();
        System.out.println("triggerLockState = " + triggerLockState);
        int outputMode = mScanManager.getOutputMode();
        System.out.println("outputMode = " + outputMode);
        Triggering triggerMode = mScanManager.getTriggerMode();
        System.out.println("triggerMode = " + triggerMode);

        return mScanManager;
    }

    /**
     * ??????????????????
     * @param intent ?????? Intent
     * @return ???????????????List<String>?????????????????? Intent ????????? null
     */
    public static List<String> getTechList(Intent intent) {
        Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
        if (tag == null) return null;
        return Arrays.asList(tag.getTechList());
    }
}
