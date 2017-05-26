package com.example.cnlcnn.model;

/**
 * 项目名：  WallPaper
 * 创建者:   LiChuang
 * 创建时间:  2017/5/18
 * 描述： 本地图片对象model
 */

public class LocalPictureModel {

    private String path;//图片的本机存储路径

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public LocalPictureModel() {
        super();
    }
}
