package com.example.cnlcnn.imp;

import com.example.cnlcnn.model.WallpaperApiModel;

import retrofit2.Call;
import retrofit2.http.GET;

/*
 *  项目名：   Wallpaper
 *  文件名:   ApiImp
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 *  描述：    api接口
 */


public interface APIimp {

    //获取爱壁纸接口
    @GET("baidu_rom.php")
    Call<WallpaperApiModel> getWallpaperApi();

}
