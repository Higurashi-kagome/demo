 
 
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
 
/**
 * 字符串压缩
 */
public class ZipStrUtil {
 
  /**
   * 恢复字符串
   * @param compressedStr
   * @return
   */
  public static String gunzip(String compressedStr){
    if(compressedStr==null){
      return null;
    }
 
    ByteArrayOutputStream out= new ByteArrayOutputStream();
    ByteArrayInputStream in=null;
    GZIPInputStream ginzip=null;
    byte[] compressed=null;
    String decompressed = null;
    try {
      compressed = new sun.misc.BASE64Decoder().decodeBuffer(compressedStr);
      in=new ByteArrayInputStream(compressed);
      ginzip=new GZIPInputStream(in);
 
      byte[] buffer = new byte[1024];
      int offset = -1;
      while ((offset = ginzip.read(buffer)) != -1) {
        out.write(buffer, 0, offset);
      }
      decompressed=out.toString();
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      if (ginzip != null) {
        try {
          ginzip.close();
        } catch (IOException e) {
        }
      }
      if (in != null) {
        try {
          in.close();
        } catch (IOException e) {
        }
      }
      if (out != null) {
        try {
          out.close();
        } catch (IOException e) {
        }
      }
    }
 
    return decompressed;
  }
 
  /**
   * 压缩字符串
   * @param primStr
   * @return
   */
  public static String gzip(String primStr) {
    if (primStr == null || primStr.length() == 0) {
      return primStr;
    }
 
    ByteArrayOutputStream out = new ByteArrayOutputStream();
 
    GZIPOutputStream gzip=null;
    try {
      gzip = new GZIPOutputStream(out);
      gzip.write(primStr.getBytes());
    } catch (IOException e) {
      e.printStackTrace();
    }finally{
      if(gzip!=null){
        try {
          gzip.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
 
 
    return new sun.misc.BASE64Encoder().encode(out.toByteArray());
  }

  public static void main(String[] args) {
    String s = "{\"orderId\":\"555202da153d46909b19940b5aa0f962\",\"orderType\":\"sale\",\"plateNo\":\"湘A66666\"}";
    String s1 = ZipStrUtil.gzip(s);
    System.out.println("s1 = " + s1);
    String s2 = ZipStrUtil.gunzip(s1);
    System.out.println("s2 = " + s2);
  }
}