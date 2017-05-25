package com.example.cnlcnn.localPictures;

import android.app.WallpaperManager;
import android.content.Intent;
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

import java.io.IOException;

/**
 * Created by cnlcnn on 2017/5/4.
 */

public class LocalPictureSetActivity extends AppCompatActivity {

    com.getbase.floatingactionbutton.FloatingActionButton fab_set, fab_share;
    ImageView pictureImageView;
    WallpaperManager mWallpaperManager;//定义系统的壁纸管理服务
    private Bitmap imageBitmap;//转译后的图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_local_picture);

        initViews();
        mWallpaperManager = WallpaperManager.getInstance(LocalPictureSetActivity.this);

        Intent intent = getIntent();
        String path = intent.getStringExtra(Constants.LOCAL_PICTURE_PATH);
        imageBitmap = BitmapFactory.decodeFile(path);
        Glide.with(this).load(path).into(pictureImageView);

    }

    ;

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

                        }
                        break;
                    }
                }
            };
}
