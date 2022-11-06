package com.chalco.holder.common;

import android.content.Context;
import android.os.Environment;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * 全局异常处理器<br>
 * https://www.jianshu.com/p/4d18febf1c70
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    
    public static final String TAG = CrashHandler.class.getSimpleName();
    
    //系统默认的UncaughtException处理类 
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    //CrashHandler实例
    private static final CrashHandler INSTANCE = new CrashHandler();
    //程序的Context对象
    private Context mContext;
    //用来存储设备信息和异常信息
    private final Map<String, String> info = new HashMap<>();

    /** 保证只有一个CrashHandler实例 */
    private CrashHandler() {
    }

    /** 获取CrashHandler实例 ,单例模式 */
    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    /** 
     * 初始化 
     *
     * @param context 
     */
    public void init(Context context) {
        mContext = context;
        //获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        //设置该CrashHandler为程序的默认处理器
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    /** 
     * 当UncaughtException发生时会转入该函数来处理 
     */
    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean isHandled = handleException(ex);
        if (!isHandled && mDefaultHandler != null) {
            System.out.println("mDefaultHandler");
            //如果用户没有处理则让系统默认的异常处理器来处理
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /** 
     * 自定义错误处理,收集错误信息 发送错误报告等操作均在此完成. 
     *
     * @param ex 
     * @return true:如果处理了该异常信息;否则返回false. 
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) return false;
        //使用Toast来显示异常信息
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast.makeText(mContext, "抱歉，程序出现异常，即将退出", Toast.LENGTH_SHORT).show();
                ex.printStackTrace();
                Looper.loop();
            }
        }.start();
        //保存日志文件
        saveCrashInfo2File(ex);
        Utils.sleep(3000);
        //退出程序
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
        return true;
    }

    /**
     * 保存错误信息到文件中
     *
     * @param ex 
     * @return  返回文件名称,便于将文件传送到服务器 
     */
    private String saveCrashInfo2File(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        ex.printStackTrace(printWriter);
        Throwable cause = ex.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        StringBuffer sb = new StringBuffer();
        sb.append(result);
        try {
            long timestamp = System.currentTimeMillis();
            String time = Constant.SIMPLE_DATE_FORMAT.format(new Date());
            String fileName = String.format(Locale.US, "crash-%s-%d.log", time, timestamp);
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                String path = "/sdcard/crash/";
                File dir = new File(path);
                if (!dir.exists()) dir.mkdirs();
                File file = new File(path + fileName);
                file.getParentFile().mkdirs();
                file.createNewFile();
                FileOutputStream fos = new FileOutputStream(file);
                fos.write(sb.toString().getBytes());
                fos.close();
            }
            return fileName;
        } catch (Exception e) {
            Log.e(TAG, "an error occurred while writing file...", e);
        }
        return null;
    }
}