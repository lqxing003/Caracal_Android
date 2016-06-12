package com.patrick.caracal.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;

/**
 * Created by Patrick on 16/6/11.
 */

public class Encrypt {

    /**
     * 电商Sign签名生成
     * @param content 内容
     * @param keyValue Appkey
     * @param charset 编码方式
     * @throws UnsupportedEncodingException ,Exception
     * @return DataSign签名
     */
    public static String encrypt (String content, String keyValue, String charset) throws UnsupportedEncodingException, Exception
    {
        if (keyValue != null)
        {
            return Base64.base64(md5(content + keyValue, charset), charset);
        }
        return Base64.base64(md5(content, charset), charset);
    }

    public static String urlEncoder(String str, String charset) throws UnsupportedEncodingException{
        String result = URLEncoder.encode(str, charset);
        return result;
    }

    /**
     * MD5加密
     * @param str 内容
     * @param charset 编码方式
     * @throws Exception
     */
    public static String md5(String str, String charset) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(str.getBytes(charset));
        byte[] result = md.digest();
        StringBuilder sb = new StringBuilder(32);
        for (byte aResult : result) {
            int val = aResult & 0xff;
            if (val <= 0xf) {
                sb.append("0");
            }
            sb.append(Integer.toHexString(val));
        }
        return sb.toString().toLowerCase();
    }

}
