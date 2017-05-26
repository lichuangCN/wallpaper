package com.example.cnlcnn.model;
/*
 *  项目名：  WallPaper
 *  文件名:   EverydayModel
 *  创建者:   LiChuang
 *  创建时间:  2017/5/18
 *  描述： 首页每日图片model
 */

public class EverydayModel {

    private String time;
    private String image;
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    @Override
    public String toString() {
        return "EverydayModel{" +
                "time='" + time + '\'' +
                ", image='" + image + '\'' +
                '}';
    }
}
