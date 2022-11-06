package com.chalco.holder;

import static org.junit.Assert.assertEquals;

import com.chalco.holder.common.Constant;
import com.chalco.holder.common.Utils;
import com.chalco.holder.common.Validator;

import org.junit.Test;

import java.util.Date;
import java.util.Locale;

public class ExampleUnitTest {

    private String transportMaterialId = "30";
    private String oldAcceptCardStr1 = String.format(Locale.US, "{%s}", transportMaterialId);
    private String oldAcceptCardStr2 = String.format(Locale.US, "{%s,%d,0}", transportMaterialId, Constant.CardType.accept);
    private String acceptCardStr = String.format(Locale.US, "{%s,%d,0,v9,桂A66824,40.56,2022-10-19 20:48:39}", transportMaterialId, Constant.CardType.accept);
    private String oldSampleCardStr1 = String.format(Locale.US, "{%s,%d}", transportMaterialId, Constant.CardType.sample);
    private String sampleCardStr = String.format(Locale.US, "{%s,%d}[v9,桂A66824,40.56,2022-10-19 20:48:39]", transportMaterialId, Constant.CardType.sample);
    private String oldSampledCardStr1 = String.format(Locale.US, "{%s,%d}", transportMaterialId, Constant.CardType.sampled);
    private String sampledCardStr = String.format(Locale.US, "{%s,%d}[v9,桂A66824,40.56,2022-10-19 20:48:39]", transportMaterialId, Constant.CardType.sampled);
    private String oldSamplingCardStr1 = String.format(Locale.US, "{%s,%d}", transportMaterialId, Constant.CardType.sampling);
    private String samplingCardStr = String.format(Locale.US, "{%s,%d}[v9,桂A66824,40.56,2022-10-19 20:48:39]", transportMaterialId, Constant.CardType.sampling);
    private String otherCardStr = String.format(Locale.US, "{%s,%d}", transportMaterialId, 3);

    @Test
    public void test2() {
        assertEquals(false, Validator.isPassWordValid("131sadA!31231_"));
        assertEquals(false, Validator.isPassWordValid("*131sadA!31231"));
        assertEquals(false, Validator.isPassWordValid("dadasadA!a"));
        assertEquals(false, Validator.isPassWordValid("3131341214141"));
        assertEquals(false, Validator.isPassWordValid("a2!."));
        assertEquals(true, Validator.isPassWordValid("p0000000"));
        assertEquals(true, Validator.isPassWordValid("200000p00"));
        assertEquals(true, Validator.isPassWordValid("20000000P"));
        assertEquals(true, Validator.isPassWordValid("20000000.p"));
        assertEquals(true, Validator.isPassWordValid("200000p00.!"));
        assertEquals(true, Validator.isPassWordValid("liu0000p00.!"));
        assertEquals(true, Validator.isPassWordValid(Constant.INIT_PASSWORD));
    }

    @Test
    public void test3() {
        assertEquals(false, Validator.isAcceptCardData(oldAcceptCardStr2));
        assertEquals(true, Validator.isAcceptCardData(acceptCardStr));
        assertEquals(false, Validator.isAcceptCardData(String.format(Locale.US, "{%s,%d,0,v9,桂A66824,石灰石,2022-10-19 20:48:39}", transportMaterialId, Constant.CardType.accept)));
        assertEquals(false, Validator.isAcceptCardData("{}"));
        assertEquals(false, Validator.isAcceptCardData("{21,1,}"));
        assertEquals(false, Validator.isAcceptCardData("{21,1,0}"));
        assertEquals(false, Validator.isAcceptCardData("{21,3,0,v9,桂A66824,片碱,40.56,}"));
        assertEquals(false, Validator.isAcceptCardData("{21,3,0,v9,桂A66824,片碱,40.56}"));
        assertEquals(false, Validator.isAcceptCardData("{,21,3,0,v9,桂A66824,片碱,40.56}"));
        assertEquals(false, Validator.isAcceptCardData("{,3,0}"));
        assertEquals(false, Validator.isAcceptCardData(oldAcceptCardStr1));
        assertEquals(false, Validator.isAcceptCardData(oldSampleCardStr1));
        assertEquals(false, Validator.isAcceptCardData(oldSampledCardStr1));
        assertEquals(false, Validator.isAcceptCardData(oldSamplingCardStr1));
        assertEquals(false, Validator.isAcceptCardData(""));
        assertEquals(false, Validator.isAcceptCardData(null));
        assertEquals(false, Validator.isAcceptCardData("{117,3,0,v9,桂A66824,片碱,40.56}"));
        assertEquals(false, Validator.isAcceptCardData("{118,3,0,v9,桂A66824,片碱,40.56}"));
    }

    @Test
    public void test1() {
        assertEquals(true, Validator.isSampleCardData(sampleCardStr));
        assertEquals(false, Validator.isSampleCardData(oldSampleCardStr1));
        assertEquals(false, Validator.isSampleCardData(oldSampledCardStr1));
        assertEquals(false, Validator.isSampleCardData(oldSamplingCardStr1));
        assertEquals(false, Validator.isSampleCardData(otherCardStr));
        assertEquals(false, Validator.isSampleCardData(""));
        assertEquals(false, Validator.isSampleCardData(null));
    }

    @Test
    public void test4() {
        assertEquals(true, Validator.isSampledCardData(sampledCardStr));
        assertEquals(false, Validator.isSampledCardData(oldSampledCardStr1));
        assertEquals(false, Validator.isSampledCardData(oldSamplingCardStr1));
        assertEquals(false, Validator.isSampledCardData(otherCardStr));
        assertEquals(false, Validator.isSampledCardData(""));
        assertEquals(false, Validator.isSampledCardData(null));
    }

    @Test
    public void test5() {
        assertEquals(false, Validator.isSamplingCardData(oldSampleCardStr1));
        assertEquals(false, Validator.isSamplingCardData(oldSampledCardStr1));
        assertEquals(false, Validator.isSamplingCardData(sampleCardStr));
        assertEquals(false, Validator.isSamplingCardData(oldSamplingCardStr1));
        assertEquals(true, Validator.isSamplingCardData(samplingCardStr));
        assertEquals(false, Validator.isSamplingCardData(otherCardStr));
        assertEquals(false, Validator.isSamplingCardData(""));
        assertEquals(false, Validator.isSamplingCardData(null));
    }

    @Test
    public void test6() {
        assertEquals("945f9ab331d59ad7fc78ecf812e47ab1", Utils.getMD5Str("sampleCardStr"));
        assertEquals("0e6482cebb772aad7be7f303f37064f3", Utils.getMD5Str(101 + Constant.UUID));
    }

    @Test
    public void test7() {
        assertEquals(Constant.CardType.unknown, Utils.getCardType(oldSampleCardStr1));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(oldSampledCardStr1));
        assertEquals(Constant.CardType.sampled, Utils.getCardType(sampledCardStr));
        assertEquals(Constant.CardType.sample, Utils.getCardType(sampleCardStr));
        assertEquals(Constant.CardType.sampling, Utils.getCardType(samplingCardStr));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(oldSamplingCardStr1));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(oldAcceptCardStr1));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(oldAcceptCardStr2));
        assertEquals(Constant.CardType.accept, Utils.getCardType(acceptCardStr));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(otherCardStr));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(""));
        assertEquals(Constant.CardType.unknown, Utils.getCardType(null));
    }

    @Test
    public void test8() {
        assertEquals(null, Utils.getTransportIdFromCard(oldSampleCardStr1));
        assertEquals(null, Utils.getTransportIdFromCard(oldSampledCardStr1));
        assertEquals(null, Utils.getTransportIdFromCard(oldSamplingCardStr1));
        assertEquals(transportMaterialId, Utils.getTransportIdFromCard(sampleCardStr));
        assertEquals(transportMaterialId, Utils.getTransportIdFromCard(sampledCardStr));
        assertEquals(transportMaterialId, Utils.getTransportIdFromCard(samplingCardStr));
        assertEquals(transportMaterialId, Utils.getTransportIdFromCard(acceptCardStr));
        assertEquals(null, Utils.getTransportIdFromCard(oldAcceptCardStr1));
        assertEquals(null, Utils.getTransportIdFromCard(oldAcceptCardStr2));
        assertEquals(null, Utils.getTransportIdFromCard(otherCardStr));
        assertEquals(null, Utils.getTransportIdFromCard(""));
        assertEquals(null, Utils.getTransportIdFromCard(null));
    }

    @Test
    public void test9() {
        assertEquals(sampleCardStr, Utils.getSampleCardData(acceptCardStr));
    }

    @Test
    public void test10() {
        assertEquals("0", Utils.getAcceptCardCnt(acceptCardStr));
    }

    @Test
    public void test11() {
        assertEquals(false, Validator.canWriteAsAccept(acceptCardStr));
        assertEquals(true, Validator.canWriteAsAccept("{12,3,0}"));
        assertEquals(true, Validator.canWriteAsAccept("{12,3,1}"));
        assertEquals(true, Validator.canWriteAsAccept("{12,3,2}"));
        assertEquals(true, Validator.canWriteAsAccept("{12,3,3}"));
        assertEquals(true, Validator.canWriteAsAccept(""));
        assertEquals(true, Validator.canWriteAsAccept(null));
    }

    @Test
    public void test12() {
        assertEquals(false, Validator.isAcceptExhausted("{12,3,0}"));
        String[] split = acceptCardStr.split(",");
        split[2] = String.valueOf(0);
        String join = String.join(",", split);
        assertEquals(false, Validator.isAcceptExhausted(join));
        split[2] = String.valueOf(1);
        join = String.join(",", split);
        assertEquals(false, Validator.isAcceptExhausted(join));
        split[2] = String.valueOf(2);
        join = String.join(",", split);
        assertEquals(true, Validator.isAcceptExhausted(join));
        split[2] = String.valueOf(3);
        join = String.join(",", split);
        assertEquals(true, Validator.isAcceptExhausted(join));
    }

    @Test
    public void test13() {
        String date = Constant.SIMPLE_DATE_FORMAT.format(new Date());
        System.out.println("date = " + date);
        String str = String.format(Locale.US, "{%s,%d,0,v9,桂A66824,40.56,%s}",
                123, Constant.CardType.accept, Constant.SIMPLE_DATE_FORMAT.format(new Date()));
        System.out.println("str = " + str);
    }
}
