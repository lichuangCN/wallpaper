package com.example.cnlcnn.download;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.cnlcnn.adapter.LocalPictureAdapter;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.model.LocalPictureModel;
import com.example.cnlcnn.utils.ScanLocalPicture;
import com.example.cnlcnn.wallpaper.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnlcnn on 2017/5/6.
 */

public class LocalDownloadActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView localPictures;
    private List<LocalPictureModel> mPictureList = new ArrayList<>();//存放本地图片对象的集合
    private LocalPictureAdapter mAdapter;//本地图片适配器


    private String path;          //下载文件夹路径
    private List<String> pathList = new ArrayList<>();//存放添加后本地图片路径

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_download);

        Intent intent = getIntent();
        String typeName = intent.getStringExtra(Constants.TYPE_NAME);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        //设置标题，即侧滑栏的选项
        mToolbar.setTitle(typeName);
        setSupportActionBar(mToolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
        }

        localPictures = (RecyclerView) findViewById(R.id.type_picture);
        GridLayoutManager layoutManager = new GridLayoutManager(LocalDownloadActivity.this, 2);
        localPictures.setLayoutManager(layoutManager);

        File sdcardDir = Environment.getExternalStorageDirectory();
        //得到一个路径，内容是添加本地图片的存放目录
        path = sdcardDir.getPath() + Constants.WALLPAPER_DOWNLOAD;
        initPictures();
    }

    /*
     * toolbar里面菜单的点击事件
     **/
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
                LocalDownloadActivity.this.finish();
            }
            break;
            default:
        }
        return true;
    }

    /**
     * 显示从本地加载的图片
     */
    private void initPictures() {

        mPictureList.clear();

        ScanLocalPicture scan = new ScanLocalPicture(path);
        pathList = scan.getPicturePathFromSD();
        //声明一个本地图片实例
        LocalPictureModel picture;
        for (int i = 0; i < pathList.size(); i++) {
            picture = new LocalPictureModel();             //创建实例
            picture.setPath((String) pathList.get(i));//循环将路径添加到实例对象中
//            Log.d("picturePath", (String) pathList.get(i));
            mPictureList.add(picture);//将每一个图片实例添加到集合中
        }

        mAdapter = new LocalPictureAdapter(mPictureList);
        localPictures.setAdapter(mAdapter);
    }
}
