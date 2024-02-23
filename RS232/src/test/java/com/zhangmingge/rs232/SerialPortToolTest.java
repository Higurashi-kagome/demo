package com.zhangmingge.rs232;

import gnu.io.SerialPort;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import static org.junit.Assert.*;

@Slf4j
public class SerialPortToolTest {

    @Test
    public void findSystemAllComPort() {
        log.info("串口列表：{}", SerialPortTool.findSystemAllComPort());
    }

    @Test
    public void openComPort() {
        SerialPort com7 = SerialPortTool.openComPort("COM7", 9600, 8, 1, 0);
        assert com7 != null;
        com7.close();
    }
}