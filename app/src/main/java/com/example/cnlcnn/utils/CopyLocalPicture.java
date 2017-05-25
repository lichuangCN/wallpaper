package com.example.cnlcnn.utils;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by cnlcnn on 2017/5/6.
 */

public class CopyLocalPicture {

    /**
     *
     * @param oldPath String 原文件路径
     * @param newPath String 复制后路径
     * @return boolean
     */
    public void copyFile(String oldPath, String newPath) {
        try {

            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                OutputStream fs = new FileOutputStream(newPath + System.currentTimeMillis()+".png");
                byte[] buffer = new byte[1024];
                int length;
                while ( (length = inStream.read(buffer)) > 0) {
                    fs.write(buffer, 0, length);
                }
                inStream.close();
                fs.close();

                Log.d("copyPicture", "successful");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
