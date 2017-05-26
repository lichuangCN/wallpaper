package com.example.cnlcnn.app;

import android.app.Application;

import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.common.QueuedWork;

/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间： 2017/5/22.
 *  描述：    分享平台的接口初始化
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
        UMShareAPI.get(this);
        Config.isUmengWx = true;
        Config.isUmengQQ = true;
        Config.isUmengSina = true;
        Config.isJumptoAppStore = true;//在没有安装的情况下跳转到应用商店下载

    }

    //各个平台的配置，建议放在全局Application或者程序入口
    {
        //QQ和Qzone appid appkey
//        PlatformConfig.setQQZone("100424468", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setQQZone("1106112473", "fzdtgokc7yupyput");

        PlatformConfig.setWeixin("wxdc1e388c3822c80b", "3baf1193c85774b3fd9d18447d76cab0");

        //自己申请sina appId, appkey可以用，Demo上的不能使用
        PlatformConfig.setSinaWeibo("1242761452", "5be0c9455d75a92492481a58b69d90a2","http://sns.whalecloud.com");

    }
}
