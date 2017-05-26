package com.example.cnlcnn.picture;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Gallery;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.example.cnlcnn.adapter.GalleryAdapter;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.wallpaper.R;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;


/*
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间:  2017/5/4
 *  描述： 网络单张设置，分享
 */

public class NetPictureSetActivity extends AppCompatActivity {

    private final static String DOWNLOAD_PATH
            = Environment.getExternalStorageDirectory() + "/WPDownload/";//设置图片保存路径

    com.getbase.floatingactionbutton.FloatingActionButton fab_set, fab_download, fab_share;


    private Handler mHandler = new Handler();

    private Gallery mGallery;
    private int position;

    private String picturePath;//图片的网络路径
    private ArrayList<String> mListBigUrl;

    private WallpaperManager wpManager;

    private UMImage imageurl;

    private ShareAction mShareAction;//底部分享面板需要
    private UMShareListener mShareListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_picture_set_activity);

        initViews();
        initShareAction();
    }

    /**
     * 初始化底部分享的平台
     */
    public void initShareAction() {
        mShareListener = new CustomShareListener(this);

        mShareAction = new ShareAction(NetPictureSetActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA
        )
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        new ShareAction(NetPictureSetActivity.this).withMedia(imageurl)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
    }

    public void initViews() {
        mGallery = (Gallery) findViewById(R.id.mGallery);

        fab_set = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_set);
        fab_download = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_download);
        fab_share = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_share);
        fab_set.setOnClickListener(fabListener);
        fab_download.setOnClickListener(fabListener);
        fab_share.setOnClickListener(fabListener);

        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        mListBigUrl = intent.getStringArrayListExtra(Constants.NET_PICTURE_PATH);

        //壁纸管理器
        wpManager = WallpaperManager.getInstance(this);

        if (mListBigUrl.size() > 0) {
            mGallery.setAdapter(new GalleryAdapter(this, mListBigUrl));
            mGallery.setSelection(position);
            picturePath = mListBigUrl.get(position);
            imageurl = new UMImage(this, picturePath);
        }
    }

    /**
     * 浮动按钮点击事件
     */
    FloatingActionButton.OnClickListener fabListener =
            new FloatingActionButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    switch (v.getId()) {
                        case R.id.fab_set: {
                            //设为桌面壁纸
                            setDesktopWallpaper();
                        }
                        break;
                        case R.id.fab_download: {
                            //点击图片后将图片保存到SD卡跟目录下的WPDownload文件夹内
                            new Task().execute(mListBigUrl.get(position));
                        }
                        break;
                        case R.id.fab_share: {
                            ShareBoardConfig config = new ShareBoardConfig();
                            config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_NONE);
                            mShareAction.open(config);
                        }
                        break;
                    }
                }
            };


    /**
     * 获取网络图片
     *
     * @param imageurl 图片网络地址
     * @return Bitmap 返回位图
     */
    public Bitmap GetImageInputStream(String imageurl) {
        URL url;
        HttpURLConnection connection = null;
        Bitmap bitmap = null;
        try {
            url = new URL(imageurl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5000); //超时设置
            connection.setDoInput(true);
            connection.setUseCaches(true); //设置使用缓存
            InputStream inputStream = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 异步线程下载图片
     */
    class Task extends AsyncTask<String, Void, Bitmap> {
        /**
         * 表示任务执行之前的操作
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * 主要是完成耗时的操作
         */
        @Override
        protected Bitmap doInBackground(String... params) {
            mHandler.post(showInfo);
            Bitmap bitmap = GetImageInputStream((String) params[0]);
            return bitmap;
        }

        /**
         * 主要是更新UI的操作
         */
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            SavaImage(result, DOWNLOAD_PATH);
        }
    }

    private Runnable showInfo = new Runnable() {
        @Override
        public void run() {
            Toast.makeText(getBaseContext(), "图片正在保存", Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * 保存位图到本地
     *
     * @param bitmap
     * @param path   本地路径
     * @return void
     */
    public void SavaImage(Bitmap bitmap, String path) {
        File file = new File(path);
        FileOutputStream fileOutputStream = null;
        //文件夹不存在，则创建它
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            fileOutputStream = new FileOutputStream(path + System.currentTimeMillis() + ".png");
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);

            fileOutputStream.flush();
            fileOutputStream.close();
            Toast.makeText(getBaseContext(), "图片保存成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置桌面壁纸
     */
    private void setDesktopWallpaper() {
        Glide.with(this).load(mListBigUrl.get(mGallery.getSelectedItemPosition())).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    wpManager.setBitmap(resource);
                    Toast.makeText(NetPictureSetActivity.this, "桌面壁纸设置成功", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    Toast.makeText(NetPictureSetActivity.this, "桌面壁纸设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 设置锁屏壁纸
     */
    private void setLockScreenWallpaper() {
        Glide.with(this).load(mListBigUrl.get(mGallery.getSelectedItemPosition())).asBitmap().into(new SimpleTarget<Bitmap>() {
            @Override
            public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                try {
                    //获取类名
                    Class class1 = wpManager.getClass();
                    //获取设置锁屏壁纸的函数
                    Method setWallPaperMethod = class1.getMethod("setBitmapToLockWallpaper", Bitmap.class);
                    //调用锁屏壁纸的函数，并指定壁纸的路径imageFilesPath
                    setWallPaperMethod.invoke(wpManager, resource);
                    Toast.makeText(NetPictureSetActivity.this, "锁屏壁纸设置成功", Toast.LENGTH_SHORT).show();
                } catch (Throwable e) {
                    Toast.makeText(NetPictureSetActivity.this, "锁屏壁纸设置失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //关于分享的监听事件
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<NetPictureSetActivity> mActivity;

        private CustomShareListener(NetPictureSetActivity activity) {
            mActivity = new WeakReference(activity);
        }

        @Override
        public void onStart(SHARE_MEDIA platform) {

        }

        @Override
        public void onResult(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享成功啦", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SHARE_MEDIA platform, Throwable t) {

            Toast.makeText(mActivity.get(), platform + " 分享失败啦", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel(SHARE_MEDIA platform) {

            Toast.makeText(mActivity.get(), platform + " 分享取消了", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 屏幕横竖屏切换时避免出现window leak的问题
     */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mShareAction.close();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
