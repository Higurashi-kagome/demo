package com.chalco.holder.common;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Locale;

public class Logger {
    public static final String version = "v2022-10-27-night";
    public static void v(String text){
        appendLog("VERBOSE: " + text);
    }
    public static void v(String tag, String text){
        appendLog( "V/" + tag +": " + text);
    }
    public static void d(String text){
        appendLog("DEBUG: " + text);
    }
    public static void d(String tag, String msg) {
        appendLog( "D/" + tag +": " + msg);
        Log.d(tag, msg);
    }
    public static void i(String text){
        appendLog("INFO: " + text);
    }
    public static void w(String text){
        appendLog("WARN: " + text);
    }
    public static void e(String text){
        appendLog("ERROR: " + text);
    }
    public static void appendLog(String text)
    {
        String path = "/sdcard/holder/" + version;
        File dir = new File(path);
        try {
            if (!dir.exists()) dir.mkdirs();
        } catch (Exception e) {
            e.printStackTrace();
        }
        File logFile = new File(String.format(Locale.US, "%s/log.txt", dir));
        if (!logFile.exists())
        {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try
        {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
