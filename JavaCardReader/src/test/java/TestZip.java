import cn.hutool.core.util.HexUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TestZip {
    private static String rawData = "{\"orderId\":\"555202da153d46909b19940b5aa0f962\",\"orderType\":\"sale\",\"plateNo\":\"湘A66666\"}";
    private static String zipData1 = "{555202da153d46909b19940b5aa0f962,sale,湘A66666,1}";
    private static String zipData2 = "{555202da153d46909b19940b5aa0f962,s,湘A66666,1}";
    private static String zipData3 = "{555,s,湘A66666,1}";
    private static String zipData4 = "{555,s,1A66666,1}";
    private static String zipData5 = "{555,s,1A66666,1,100,1111}";

    public static void main(String[] args) {
        String[] testArr = new String[]{rawData, zipData1, zipData2, zipData3, zipData4, zipData5};
        for (int i = 0; i < testArr.length; i++) {
            char[] chars = HexUtil.encodeHex(testArr[i].getBytes());
            String hexStr = String.valueOf(chars);
            Pattern pattern = Pattern.compile("(.{32}|.*)");
            Matcher matcher = pattern.matcher(hexStr);
            while (matcher.find()) {
                System.out.println(matcher.group());
            }
            System.out.println();
        }
    }
}
