package com.jju.feng.anti_theft.utils;


import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class StreamUtil {
    /**
     * 将流文件读取出来转化成字符串
     *
     * @param is
     * @return 解析成功返回字符串，解析失败返回null
     */
    public static String readStream(InputStream is) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        try {
            while ((len = is.read(buffer)) != -1) {
                baos.write(buffer, 0, len);
            }
            is.close();
            return baos.toString();

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
