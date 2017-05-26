package com.example.cnlcnn.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/*
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间:  2017/5/6
 *  描述： 封装扫描本地文件夹中的图片，用于获取下载的图片和添加的本地图片
 */

public class ScanLocalPicture {

    //根据自己的需求读取SDCard中的资源图片的路径
    private String picturePath;

    public ScanLocalPicture(String path) {
        this.picturePath = path;
    }

    /**
     * 从SD卡中获取资源图片的路径,存放在list数组中
     */
    public List<String> getPicturePathFromSD() {

        /* 设定目前所在路径 */
        List<String> it = new ArrayList<String>();

        File mFile = new File(picturePath);
        //该目录下所有文件目录，将所有文件存入ArrayList中 */
        File[] files = mFile.listFiles();

//        Log.d("localPicturefiles", files.length+"");

        for (int i = 0; i < files.length; i++) {
            File file = files[i];
            if (checkIsImageFile(file.getPath()))
                it.add(file.getPath());
        }
        return it;
    }

    /**
     * 判断是否相应的图片格式
     */
    private boolean checkIsImageFile(String fName) {

        boolean isImageFormat;

        /* 取得扩展名 */
        String end = fName
                .substring(fName.lastIndexOf(".") + 1, fName.length())
                .toLowerCase();

        /* 按扩展名的类型决定MimeType */
        if (end.equals("jpg") || end.equals("gif") || end.equals("png")
                || end.equals("jpeg") || end.equals("bmp")) {
            isImageFormat = true;
        } else {
            isImageFormat = false;
        }
        return isImageFormat;
    }
}
