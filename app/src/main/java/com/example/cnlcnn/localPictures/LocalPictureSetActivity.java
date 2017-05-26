package com.example.cnlcnn.localPictures;

import android.app.WallpaperManager;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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

import java.io.IOException;
import java.lang.ref.WeakReference;

/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间： 2017/5/6.
 *  描述：    本地图片设置，分享界面
 */

public class LocalPictureSetActivity extends AppCompatActivity {

    com.getbase.floatingactionbutton.FloatingActionButton fab_set, fab_share;
    ImageView pictureImageView;
    WallpaperManager mWallpaperManager;//定义系统的壁纸管理服务
    private Bitmap imageBitmap;//转译后的图片

    private UMImage imageurl;

    private ShareAction mShareAction;//底部分享面板需要
    private UMShareListener mShareListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_local_picture);

        initViews();
        initShareAction();

        mWallpaperManager = WallpaperManager.getInstance(LocalPictureSetActivity.this);

        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.LOCAL_PICTURE_PATH);
        imageBitmap = BitmapFactory.decodeFile(path);
        Glide.with(this).load(path).into(pictureImageView);

        imageurl = new UMImage(this, path);//对网络图片路径进行转换为umeng图片路径

    }

    /**
     * 初始化底部分享的平台
     */
    public void initShareAction() {
        mShareListener = new CustomShareListener(LocalPictureSetActivity.this);

        mShareAction = new ShareAction(LocalPictureSetActivity.this).setDisplayList(
                SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE, SHARE_MEDIA.SINA
        )
                .setShareboardclickCallback(new ShareBoardlistener() {
                    @Override
                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {

                        new ShareAction(LocalPictureSetActivity.this).withMedia(imageurl)
                                .setPlatform(share_media)
                                .setCallback(mShareListener)
                                .share();
                    }
                });
    }

    /**
     * 注册界面控件
     */
    private void initViews() {
        pictureImageView = (ImageView) findViewById(R.id.picture_view);
        fab_set = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_set);
        fab_share = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab_share);
        fab_set.setOnClickListener(fabListener);
        fab_share.setOnClickListener(fabListener);
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

                            try {
                                mWallpaperManager.setBitmap(imageBitmap);
                                Toast.makeText( LocalPictureSetActivity.this,  "壁纸设置成功！", Toast.LENGTH_SHORT).show();
                                LocalPictureSetActivity.this.finish();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

    //关于分享的监听事件
    private static class CustomShareListener implements UMShareListener {

        private WeakReference<LocalPictureSetActivity> mActivity;

        private CustomShareListener(LocalPictureSetActivity activity) {
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
