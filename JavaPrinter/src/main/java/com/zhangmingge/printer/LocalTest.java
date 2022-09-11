package com.zhangmingge.printer;

import com.zhangmingge.printer.common.PrintUtil;
import com.zhangmingge.printer.common.TestData;

import javax.print.PrintService;
import java.awt.print.*;

/**
 * 通过 Java API 实现打印。需要打印机连到电脑本地（比如通过 USB 相连）
 */
public class LocalTest {
    public static void main(String[] args) {
        PrintService[] printServices = PrintUtil.getPrintServices();
        if(printServices.length > 0){
            System.out.println("================== 当前的打印设备列表为 ==================");
            for (PrintService printService : printServices) {
                System.out.println(printService);
            }
            Printable checkWeightData = TestData.getCheckWeightData();
            print("Foxit PhantomPDF Printer", checkWeightData);
        } else {
            System.out.println("***************** 没有打印设备 ****************");
        }
    }

    /**
     * 设置数据模型,设定打印模式
     * @param printerName 电脑中设备中打印机的名称，将使用此打印机打印内容
     * @param printable 可打印对象（实现了 Printable 的类的对象）
     */
    public static void print(String printerName, Printable printable){
        int lineCnt = 7;
        //行高单位为 1/72 英寸，20 单位大约为 7mm
        int lineHeight = 20;
        //定义纸张宽高（宽度应该在实际纸张宽度内，高度应该按实际内容计算）
        double width = PrintUtil.mm2mWidth(72);
        double height = lineCnt * lineHeight;
        Paper paper = getPaper(width, height);
        PageFormat pageFormat = getPageFormat(paper);
        Book book = getBook(printable, pageFormat);
        PrintUtil.print(printerName, book);
    }

    private static Book getBook(Printable printableEntity, PageFormat pageFormat) {
        //书、文档
        Book book = new Book();
        //把 PageFormat 和 Printable 添加到书中，组成一个页面
        book.append(printableEntity, pageFormat);
        return book;
    }

    private static PageFormat getPageFormat(Paper paper) {
        //打印格式
        PageFormat pageFormat = new PageFormat();
        //原点在纸张的左上方，x 指向右方，y 指向下方。
        pageFormat.setOrientation(PageFormat.PORTRAIT);
        pageFormat.setPaper(paper);
        return pageFormat;
    }

    private static Paper getPaper(double width, double height) {
        //通过Paper设置页面的空白边距和可打印区域。必须与实际打印纸张大小相符。
        Paper paper = new Paper();
        //页面宽度 页面高度
        paper.setSize(width, height);
        // 可打印区域：原点 (x,y)，宽度，高度
        paper.setImageableArea(0, 0, width, height);
        return paper;
    }
}