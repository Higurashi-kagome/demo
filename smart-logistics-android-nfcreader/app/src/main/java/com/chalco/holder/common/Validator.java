package com.chalco.holder.common;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.widget.EditText;

import com.chalco.holder.entity.DriverInfo;
import com.chalco.holder.entity.DriverInfoAndInput;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 数据校验函数
 */
public class Validator {
    /**
     * 校验密码
     * @param pass 密码
     * @return 通过返回 true，否则返回 false
     */
    public static boolean isPassWordValid(String pass) {
        if (!Validator.isNotBlank(pass)) return false;
        // 仅限数字、字母、!、.
        Pattern p1 = Pattern.compile("([^\\d!a-zA-Z\\.])");
        Matcher m1 = p1.matcher(pass);
        if (m1.find()) return false;
        // 至少一个数字
        Pattern p2 = Pattern.compile("(\\d)");
        Matcher m2 = p2.matcher(pass);
        if (!m2.find()) return false;
        // 至少一个字母
        Pattern p3 = Pattern.compile("([a-zA-Z])");
        Matcher m3 = p3.matcher(pass);
        if (!m3.find()) return false;
        // 至少 8 个字符
        Pattern p4 = Pattern.compile("^.{8,}$");
        Matcher m4 = p4.matcher(pass);
        return m4.find();
    }

    /**
     * 输入框中的内容是否为空
     * @param editText 输入框
     * @return 为空返回 true，否则返回 false
     */
    public static boolean isEditTextEmpty(EditText editText) {
        return "".equals(editText.getText().toString().trim());
    }

    /**
     * 字符串是否不为空
     * @param str 待判断的字符串
     * @return 不为空返回 true
     */
    public static boolean isNotBlank(String str){
        return str != null && !"".equals(str.trim());
    }

    public static boolean isBlank(String str){
        return !isNotBlank(str);
    }

    /**
     * 是否为收货卡数据
     * @param cardData 卡中数据
     * @return 是则返回 true
     */
    public static boolean isAcceptCardData(String cardData) {
        if (!isNotBlank(cardData)) return false;
        String re = String.format(Locale.US, "\\{([\\w]+),%d,(\\d)+(,[^,\\}]*){4}\\}", Constant.CardType.accept);
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(cardData);
        if (!matcher.find()) return false;
        try {
            cardData = cardData.substring(1, cardData.length() - 1);
        } catch (Exception e) {
            return false;
        }
        String[] split = cardData.split(",");
        if (split.length != 7) return false;
        try {
            // 毛重
            Double.parseDouble(split[5]);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    /**
     * 是否为正样卡数据
     * @param cardData 卡中数据
     * @return 是则返回 true
     */
    public static boolean isSampleCardData(String cardData) {
        if (!isNotBlank(cardData)) return false;
        String re = String.format(Locale.US, "\\{[\\w]+,%d\\}\\[[^,\\]]*(,[^,\\]]*){3}\\]", Constant.CardType.sample);
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(cardData);
        return matcher.find();
    }

    /**
     * 是否为被抽样卡
     * @param cardData
     * @return
     */
    public static boolean isSampledCardData(String cardData) {
        if (!isNotBlank(cardData)) return false;
        String re = String.format(Locale.US, "\\{[\\w]+,%d\\}\\[[^,\\]]*(,[^,\\]]*){3}\\]", Constant.CardType.sampled);
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(cardData);
        return matcher.find();
    }

    /**
     * 是否为抽样卡
     * @param cardData
     * @return
     */
    public static boolean isSamplingCardData(String cardData) {
        if (!isNotBlank(cardData)) return false;
        String re = String.format(Locale.US, "\\{[\\w]+,%d\\}\\[[^,\\]]*(,[^,\\]]*){3}\\]", Constant.CardType.sampling);
        Pattern pattern = Pattern.compile(re);
        Matcher matcher = pattern.matcher(cardData);
        return matcher.find();
    }

    /**
     * 是否为 NFC 标签（卡片）靠近产生的 Intent
     * @param intent 待判断的 Intent
     * @return 是则返回 true
     */
    public static boolean isNFCIntent(Intent intent) {
        String action = intent.getAction();
        return NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action);
    }

    public static boolean isTrue(Boolean b){
        return b != null && b == true;
    }

    /**
     * 是否能被写为收货卡
     * @param dataInCard 卡中数据
     * @return 能写返回 true
     */
    public static boolean canWriteAsAccept(String dataInCard) {
        // 不是收货卡时，可写为收货卡
        if (!isAcceptCardData(dataInCard)) return true;
        // 是收货卡时，检查是否采样过
        String cnt = Utils.getAcceptCardCnt(dataInCard);
        int i = Integer.parseInt(cnt);
        return i >= 1;
    }

    /**
     * 收货卡是否可被继续采样（一张收货卡最多采样两次）
     * @param dataInCard 收货卡数据
     * @return 不能继续采样返回 false
     */
    public static boolean isAcceptExhausted(String dataInCard) {
        if (!isAcceptCardData(dataInCard)) return false;
        String cnt = Utils.getAcceptCardCnt(dataInCard);
        int i = Integer.parseInt(cnt);
        return i > 1;
    }

    /**
     * 是否为收货（质检）二维码
     * @param codeStr
     * @return
     */
    public static boolean isAcceptQrCode(String codeStr) {
        if (!Validator.isNotBlank(codeStr)) return false;
        DriverInfo driverInfo = null;
        try{
            DriverInfoAndInput driverInfoAndInput = Constant.GSON.fromJson(codeStr, DriverInfoAndInput.class);
            driverInfo = driverInfoAndInput.getDriverInfo();
        } catch (Exception e) { }
        if (driverInfo == null) return false;
        if (!Validator.isNotBlank(driverInfo.getTransportMaterialId())) return false;
        if (!Validator.isNotBlank(driverInfo.getOrderCode())) return false;
        if (!Validator.isNotBlank(driverInfo.getPlateNo())) return false;
        if (!Validator.isNotBlank(driverInfo.getMaterialName())) return false;
        return Validator.isNotBlank(driverInfo.getCustomerCode());
    }
}
