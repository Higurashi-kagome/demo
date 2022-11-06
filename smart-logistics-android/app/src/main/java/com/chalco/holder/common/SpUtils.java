package com.chalco.holder.common;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.chalco.holder.entity.OperationRecord;
import com.google.gson.reflect.TypeToken;

import java.util.LinkedList;

/**
 *  安卓数据存储工具类
 */
public class SpUtils {

    private static SharedPreferences sp;

    /**
     * 清除操作记录
     * @param ctx 上下文对象
     */
    public static void clearHistoryList(Context ctx) {
        if (sp == null) sp = ctx.getSharedPreferences(Constant.OperationRecord, MODE_PRIVATE);
        LinkedList<OperationRecord> operationRecords = listOperationRecord(ctx);
        LogUtil.d("clearHistoryList - " + operationRecords.toString());
        sp.edit().clear().apply();
    }

    /**
     * 用新 List 覆盖历史数据（超过最大长度时保存失败）
     * @param cxt
     * @param list
     */
    public static void setOperationRecordList(Context cxt, LinkedList<OperationRecord> list) {
        if (list.size() > Constant.HISTORY_MAX) return;
        clearHistoryList(cxt);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.OperationRecord, Constant.GSON.toJson(list));
        editor.apply();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void removeByTransportMaterialId(Context cxt, String transportMaterialId){
        if (!Validator.isNotBlank(transportMaterialId)) return;
        LinkedList<OperationRecord> list = SpUtils.listOperationRecord(cxt);
        list.removeIf(item -> transportMaterialId.equals(item.getTransportMaterialId()));
        SpUtils.setOperationRecordList(cxt, list);
    }

    /**
     * 保存操作记录
     * @param ctx
     * @param record 操作记录
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public static void saveOperationRecord(Context ctx, OperationRecord record) {
        if (record == null) return;
        if (sp == null) sp = ctx.getSharedPreferences(Constant.OperationRecord, MODE_PRIVATE);
        LinkedList<OperationRecord> list = listOperationRecord(ctx);
        // 根据transportMaterialId去重
        String transportMaterialId = record.getTransportMaterialId();
        String operationName = record.getOperationName();
        if (Validator.isNotBlank(transportMaterialId) && Validator.isNotBlank(operationName)) {
            list.removeIf(item -> {
                return transportMaterialId.equals(item.getTransportMaterialId()) &&
                        operationName.equals(item.getOperationName());
            });
        }
        list.add(record);
        if (list.size() > Constant.HISTORY_MAX) list.removeFirst();
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.OperationRecord, Constant.GSON.toJson(list));
        editor.apply();
    }

    /**
     * 获取操作记录列表
     * @param ctx 上下文对象
     */
    public static LinkedList<OperationRecord> listOperationRecord(Context ctx) {
        if (sp == null) sp = ctx.getSharedPreferences(Constant.OperationRecord, MODE_PRIVATE);
        String dataJson = sp.getString(Constant.OperationRecord, null);
        if (dataJson == null){
            return new LinkedList<>();
        }else{
            return Constant.GSON.fromJson(dataJson, new TypeToken<LinkedList<OperationRecord>>() {}.getType());
        }
    }

    /**
     * 保存用户名
     * @param ctx 上下文对象
     * @param account 用户名
     */
    public static void putAccount(Context ctx, String account) {
        if (sp == null) sp = ctx.getSharedPreferences(Constant.SHARED_PREFERENCES_ACCOUNT_KEY, MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(Constant.SHARED_PREFERENCES_ACCOUNT_KEY, account);
        editor.apply();
    }

    /**
     * 获取用户名
     * @param ctx 上下文对象
     * @return 用户名
     */
    public static String getAccount(Context ctx) {
        if (sp == null) sp = ctx.getSharedPreferences(Constant.SHARED_PREFERENCES_ACCOUNT_KEY, MODE_PRIVATE);
        return sp.getString(Constant.SHARED_PREFERENCES_ACCOUNT_KEY, "admin");
    }

}