package com.chalco.holder.common;

import android.text.method.KeyListener;
import android.text.method.NumberKeyListener;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class Constant {

    public static final SimpleDateFormat SIMPLE_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);

    public static final String INIT_USER_NAME = "admin";
    public static final String INIT_PASSWORD = "a0000000";
    public static final float DIALOG_BUTTON_TEXT_SIZE = 30;

    public static final Gson GSON = new Gson();
    public static final int HISTORY_MAX = 700;

    public static final String UUID = "7f48b200-a6ff-4ac6-a01f-28ac37838212";
    public static final String WRITE_INTENT_MSG_KEY = "NFCDataStr";
    public static final String ACCEPT_SALES_CARD_DATA = "ACCEPT_SALES_CARD_DATA";
    public static final String SAMPLED_CARD_STR = "SAMPLED_CARD_STR";
    public static final String OperationRecord = "OperationRecord";
    public static final String SHARED_PREFERENCES_ACCOUNT_KEY = "account";
    public static final String CARD_ID = "CARD_ID";
    public static final String CARD_SEARCH_DATA = "CARD_SEARCH_DATA";
    public static final String HISTORY_REC_MSG = "HISTORY_REC_MSG";
    public static final String TRANSPORT_MATERIAL_ID = "TRANSPORT_MATERIAL_ID";
    public static final String SAMPLE_RECORD = "SAMPLE_RECORD";
    public static final String SAMPLING_RECORD = "SAMPLING_RECORD";

    /**
     * 常量的值影响到操作记录中的操作类型
     */
    public static class Commands {
        /**
         * 采购直接收货
         */
        public static final String ACCEPT_SALES1 = "直接收货";
        /**
         * 采购二次确认
         */
        public static final String ACCEPT_SALES2 = "二次确认";
        /**
         * 采购单独收货
         */
        public static final String ACCEPT_SALES = "收货";
        /**
         * 查收货卡
         */
        public static final String SEARCH_RECEIPT_CARD = "查收货卡";
        /**
         * 采购退货
         */
        public static final String REJECT_SALES = "采购退货";
        /**
         * 采样登记
         */
        public static final String CARD_TRANSFER = "采样登记";
        /**
         * 抽样登记
         */
        public static final String SAMPLING = "抽样登记";
        /**
         * 测试读取
         */
        public static final String TEST_READ = "测试读取";
        /**
         * 测试写入
         */
        public static final String TEST_WRITE = "测试写入";
        public static final String NEW_ACCEPT_CARD = "补收货卡";
        public static final String NEW_SAMPLE_CARD = "补正样卡";
        public static final String NEW_SAMPLING_CARD = "补抽样卡";
        public static final String NEW_ACCEPT1_CARD = "写直接收货卡";
        public static final String NEW_ACCEPT2_CARD = "写二次确认卡";
    }

    public static class QRTypeCode {
        /**
         * 采购直接收货
         */
        public static final int ACCEPT_SALES1 = 1;
        /**
         * 采购二次确认
         */
        public static final int ACCEPT_SALES2 = 2;
        /**
         * 采购单独收货
         */
        public static final int ACCEPT_SALES = 1;
        /**
         * 采购退货
         */
        public static final int REJECT_SALES = -1;
    }

    public static class CardType {
        /**
         * 正样卡
         */
        public static final int sample = 0;
        /**
         * 被抽样卡
         */
        public static final int sampled = 1;
        /**
         * 抽样卡
         */
        public static final int sampling = 2;
        /**
         * 收货卡
         */
        public static final int accept = 3;
        /**
         * 未知
         */
        public static final int unknown = -1;
    }

    /**
     * Intent 信息中的键，其对应的值为一个字符串，接收 Intent 的活动依据其值执行不同的操作
     */
    public static final String COMMAND_KEY = "command";
    /**
     * Intent 信息中的键，其对应的值为用来传递数据的字符串
     */
    public static final String INTENT_STR_KEY = "str";

    /**
     * 允许输入整数
     */
    public static final KeyListener intKeyListener = new NumberKeyListener() {
        //返回哪些希望可以被输入的字符,默认不允许输入
        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        }
        //0：无键盘,键盘弹不出来 1：英文键盘 2：模拟键盘 3：数字键盘
        @Override
        public int getInputType() {
            return 3;
        }
    };

    /**
     * 输入小数
     */
    public static final KeyListener doubleKeyListener = new NumberKeyListener() {
        //返回哪些希望可以被输入的字符,默认不允许输入
        @Override
        protected char[] getAcceptedChars() {
            return new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '.'};
        }
        //0：无键盘,键盘弹不出来 1：英文键盘 2：模拟键盘 3：数字键盘
        @Override
        public int getInputType() {
            return 3;
        }
    };

}
