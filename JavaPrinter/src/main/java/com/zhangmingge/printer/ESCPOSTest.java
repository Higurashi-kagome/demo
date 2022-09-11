package com.zhangmingge.printer;

import com.github.anastaciocintra.escpos.EscPos;
import com.github.anastaciocintra.escpos.EscPosConst;
import com.github.anastaciocintra.escpos.Style;
import com.github.anastaciocintra.escpos.barcode.QRCode;
import com.zhangmingge.printer.common.Constants;
import com.zhangmingge.printer.common.TestData;
import com.zhangmingge.printer.entity.CheckWeightData;
import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * 通过 escpos-coffee 库来操作打印机。是相对来说最简洁方便的操作方式。<br>
 * 更多实例和使用方法可查看官网：https://anastaciocintra.github.io/escpos-coffee/<br>
 * 该库的底层，就是类似于 {@link RemoteTest} 的代码。
 */
public class ESCPOSTest {

    @Test
    public void print() throws IOException {
        CheckWeightData weightData = TestData.getCheckWeightData();
        String[] lines = new String[]{
                "检斤时间：" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(weightData.getWeightDate()),
                "检斤重量（吨）：" + weightData.getAccordWeight(),
                "检斤点：" + weightData.getDeviceName(),
                "车牌号：" + weightData.getPlateNo(),
                "订单类别：" + weightData.getOrderType()
        };
        Socket socket =  new Socket(Constants.IP, Constants.PORT);
        OutputStream out = socket.getOutputStream();
        Style title = new Style()
                .setFontSize(Style.FontSize._2, Style.FontSize._2)
                .setLineSpacing(2);
        EscPos escPos = new EscPos(out);
        // 不设置中文会乱码
        escPos.setCharsetName("GB18030");
        for (String line : lines) {
            escPos.writeLF(title, line);
        }
        escPos.feed(4);
        QRCode qrcode = new QRCode();
        qrcode.setSize(7);
        qrcode.setJustification(EscPosConst.Justification.Center);
        escPos.write(qrcode, "hello qrcode");
        escPos.feed(4);
        escPos.cut(EscPos.CutMode.FULL);
        escPos.close();
    }
}
