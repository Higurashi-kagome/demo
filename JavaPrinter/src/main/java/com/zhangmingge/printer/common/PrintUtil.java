package com.zhangmingge.printer.common;

import javax.print.DocFlavor;
import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import javax.print.attribute.HashAttributeSet;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.standard.PrinterName;
import java.awt.print.Book;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

public class PrintUtil {
    private static PrintService[] printServices = null;

    static {
        DocFlavor psInFormat = DocFlavor.INPUT_STREAM.AUTOSENSE;
        HashPrintRequestAttributeSet hashPrintRequestAttributeSet = new HashPrintRequestAttributeSet();
        printServices = PrintServiceLookup.lookupPrintServices(psInFormat, hashPrintRequestAttributeSet);
    }

    /**
     * 获取所有打印服务
     */
    public static PrintService[] getPrintServices(){
        return printServices;
    }

    /**
     * 获取打印服务（遍历 printServices 获取），如果找不到，则使用默认打印机
     * @param printerName 打印机名称
     * @return PrintService
     */
    public static PrintService getPrintService(String printerName) {
        for (PrintService ps : printServices) {
            String serviceName = ps.toString();
            if (serviceName.contains(printerName)) {
                return ps;
            }
        }
        return PrintServiceLookup.lookupDefaultPrintService();
    }

    /**
     * 获取打印服务（使用 API 获取）
     * @param printerName 打印机名称
     * @return PrintService
     */
    public static PrintService getPrintServiceDirectly(String printerName) {
        //指定打印机打印（printerName打印机名称）
        HashAttributeSet hs = new HashAttributeSet();
        hs.add(new PrinterName(printerName, null));
        PrintService[] pss = PrintServiceLookup.lookupPrintServices(null, hs);
        if (pss.length > 0) return pss[0];
        return null;
    }

    /**
     * 打印文档
     * @param printerName 打印机名称
     * @param book 文档
     */
    public static void print(String printerName, Book book) {
        //获取打印服务对象
        PrinterJob job = PrinterJob.getPrinterJob();
        //写入书
        job.setPageable(book);
        try {
            //添加指定的打印机
            job.setPrintService(getPrintService(printerName));
            //打印执行
            job.print();
        } catch (PrinterException e) {
            System.err.println("打印出现异常:" +e);
        }
    }

    /**
     * 纸张单位为 1/72 英寸。该方法用于将毫米转换为纸张长度单位
     * @param mm 毫米值
     * @return 转换后的纸张宽度值
     */
    public static double mm2mWidth(double mm) {
        double mWidth = mm * 72 / 25.4;
        return mWidth;
    }
}
