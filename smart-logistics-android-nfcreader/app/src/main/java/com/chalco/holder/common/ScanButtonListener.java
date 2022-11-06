package com.chalco.holder.common;

import android.annotation.SuppressLint;
import android.device.ScanManager;
import android.device.scanner.configuration.Triggering;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;

import com.chalco.holder.R;

/**
 * 扫描按钮
 */
public class ScanButtonListener implements View.OnClickListener, View.OnTouchListener {
    private Button mScan = null;
    private ScanManager mScanManager = null;

    public ScanButtonListener(Button mScan, ScanManager mScanManager) {
        this.mScan = mScan;
        this.mScanManager = mScanManager;
    }

    private void startDecode() {
        if (mScanManager != null) {
            mScanManager.startDecode();
        }
    }

    private void stopDecode() {
        if (mScanManager != null) {
            mScanManager.stopDecode();
        }
    }

    public void onClick(View v) {}
    @SuppressLint("ClickableViewAccessibility")
    public boolean onTouch(View v, MotionEvent event) {
        if (v.getId() == R.id.scan_trigger) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                if (Triggering.HOST.equals(mScanManager.getTriggerMode())) {
                    if (mScan != null) mScan.setText(R.string.scan_trigger_text);
                    stopDecode();
                }
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (mScan != null) mScan.setText(R.string.scan_trigger_end);
                startDecode();
                if (Triggering.PULSE.equals(mScanManager.getTriggerMode())) {
                    // 五秒后超时，定时关闭
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                        stopDecode();
                        if (mScan != null) mScan.setText(R.string.scan_trigger_text);
                    }, 5000);
                }
            }
        }
        return false;
    }
}
