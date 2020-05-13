package com.wenfengtou.commonutil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

    public static void writeH264(byte[] h264, String path) {
        writeFile(h264, path);
    }

    public static void writeFile(byte[] data, String path) {
        try {
            FileOutputStream fos=new FileOutputStream(path,true);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
