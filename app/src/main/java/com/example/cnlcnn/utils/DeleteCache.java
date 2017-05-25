package com.example.cnlcnn.utils;

import java.io.File;

/**
 * Created by cnlcnn on 2017/5/6.
 */

public class DeleteCache {

    /**
     * 递归删除文件和文件夹
     *
     * @param file
     *            要删除的根目录
     */
    public void DeleteCache(File file) {
        if (file.exists() == false) {

            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteCache(f);
                }
                file.delete();
            }
        }


    }
}
