package com.chalco.holder.common;

import android.app.Activity;
import android.nfc.NdefMessage;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.AsyncTask;

import com.chalco.holder.entity.NeedWriteResponse;

import java.io.IOException;

public class WriteTaskUtils {
    public static class WriteTask extends AsyncTask<Void, Void, Boolean> {
        private NeedWriteResponse delegate = null;
        private Activity host = null;
        private NdefMessage msg = null;
        private Tag tag = null;

        public WriteTask(Activity host, NdefMessage msg, Tag tag) {
            this.host = host;
            if (host instanceof NeedWriteResponse) delegate = (NeedWriteResponse) host;
            this.msg = msg;
            this.tag = tag;
        }

        public void setDelegate(NeedWriteResponse delegate) {
            this.delegate = delegate;
        }

        /**
         * 在onPreExecute()完成后立即执行，用于执行较为费时的操作，此方法将接收输入参数和返回计算结果<br>
         * 不能在doInBackground(Params... params)中更改UI组件的信息
         * @param objects 输入参数
         * @return 计算结果
         */
        @Override
        protected Boolean doInBackground(Void... objects) {
            int size = msg.toByteArray().length;
            try {
                Ndef ndef = Ndef.get(tag);
                if (ndef == null) {
                    if (formatNdef()) return true;
                } else {
                    ndef.connect();
                    try {
                        if (!ndef.isWritable()) return null;
                        if (ndef.getMaxSize() < size) return null;
                        // 先调用读取，检测是否在范围内
                        ndef.getNdefMessage();
                        ndef.writeNdefMessage(msg);
                        return true;
                    } catch (Exception e) {
                        e.printStackTrace();
                    } finally {
                        ndef.close();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        private boolean formatNdef() throws IOException {
            NdefFormatable ndefFormatable = NdefFormatable.get(tag);
            if (ndefFormatable != null) {
                ndefFormatable.connect();
                try {
                    ndefFormatable.format(msg);
                    return true;
                } catch (Exception e2) {
                    e2.printStackTrace();
                } finally {
                    ndefFormatable.close();
                }
            }
            return false;
        }

        /**
         * 当后台操作结束时，此方法将会被调用，计算结果将做为参数传递到此方法中，用于在执行完后台任务后更新UI，显示结果
         * @param result 计算结果
         */
        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            // 如果 delegate 和 host 为同一个 Activity，则需要先 processWriteResponse 再 host.finish()
            if (delegate != null) delegate.processWriteResponse(result);
            if (result != null && result == true && host != null) {
                Utils.playSuccessRing(host);
            }
        }
    }
}
