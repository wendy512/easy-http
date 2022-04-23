package com.github.easyhttp.common;

import java.io.*;

/**
 * io 工具类
 * @author wendy512
 * @date 2022-04-18 15:12:15:12
 * @since 1.0.0
 */
public class IOUtils {
    private static final int EOF = -1;
    public static final int BUFFER_SIZE = 8192;

    /**
     * 输入流转成字节数组
     * @param input 输入流
     * @return 字节数组
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream input) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream(BUFFER_SIZE);
        copyLarge(input, output, new byte[BUFFER_SIZE]);
        byte[] bytes = output.toByteArray();
        closeQuietly(input);
        return bytes;
    }

    public static long copyLarge(InputStream input, OutputStream output, byte[] buffer)
            throws IOException {
        long count = 0;
        int n = 0;
        while (EOF != (n = input.read(buffer))) {
            output.write(buffer, 0, n);
            count += n;
        }
        return count;
    }
    
    public static void closeQuietly(Closeable closeable) {
        try {
            if (closeable != null) {
                closeable.close();
            }
        } catch (IOException ioe) {
            // ignore
        }
    }
}
