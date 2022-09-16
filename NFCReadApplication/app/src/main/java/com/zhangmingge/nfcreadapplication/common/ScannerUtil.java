package com.zhangmingge.nfcreadapplication.common;

import android.device.ScanManager;
import android.device.scanner.configuration.PropertyID;
import android.device.scanner.configuration.Triggering;

/**
 * 手持机扫描器工具类
 */
public class ScannerUtil {
    private static ScanManager mScanManager = new ScanManager();

    private ScannerUtil() {}

    public static ScanManager getScanManager() {
        return mScanManager;
    }

    /**
     * 创建 ScanManager 对象，尝试开启 Scanner
     * @return 失败 false，成功 true
     */
    public static boolean initScan() {
        mScanManager = new ScanManager();
        boolean powerOn = mScanManager.getScannerState();
        if (!powerOn) {
            powerOn = mScanManager.openScanner();
            mScanManager.setTriggerMode(Triggering.PULSE);
            int[] key = new int[]{ PropertyID.WEDGE_INTENT_ACTION_NAME };
            String[] action = { ScanManager.ACTION_DECODE };
            boolean ret = mScanManager.setParameterString(key, action);
            if (!powerOn) {
                return false;
            }
        }
        return true;
    }

    /**
     * ScanManager.startDecode
     */
    public static void startDecode() {
        if (mScanManager != null) {
            mScanManager.startDecode();
        }
    }


    /**
     * ScanManager.stopDecode
     */
    public static void stopDecode() {
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
    }


    /**
     * ScanManager.closeScanner
     *
     * @return false 失败，true 成功
     */
    public static boolean closeScanner() {
        boolean state = false;
        if (mScanManager != null) {
            mScanManager.stopDecode();
            state = mScanManager.closeScanner();
        }
        return state;
    }
}
