package com.jju.feng.anti_theft.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by Administrator on 2016/12/9 0009.
 */

public class Md5Utils {

    /**
     * 加密字符串采用md5算法
     *
     * @param text
     * @return
     */
    public static String encode(String text) {
        try {
            MessageDigest digest = MessageDigest.getInstance("md5");
            byte[] result = digest.digest(text.getBytes());
            StringBuffer sb = new StringBuffer();
            for (byte b : result) {
                String hex = Integer.toHexString(b & 0xff);//+ 2;//加盐
                if (hex.length() == 1) {
                    sb.append("0");
                }
                sb.append(hex);
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";//can't reach
        }
    }
}
