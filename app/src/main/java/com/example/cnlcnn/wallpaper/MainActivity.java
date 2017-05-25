package com.example.cnlcnn.wallpaper;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;

import com.example.cnlcnn.aboutUs.AboutUsActivity;
import com.example.cnlcnn.adapter.HomePagerAdapter;
import com.example.cnlcnn.download.LocalDownloadActivity;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.localPictures.LocalPicturesActivity;
import com.example.cnlcnn.pictureFragments.CategoryFragment;
import com.example.cnlcnn.pictureFragments.EverydayFragment;
import com.example.cnlcnn.pictureFragments.RecommendFragment;
import com.example.cnlcnn.utils.DeleteCache;

import java.io.File;


public class MainActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private DrawerLayout mDrawerLayout;
    NavigationView navigationView;
    private TabLayout mTabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        createSDCardCache();//创建本地cache目录
        createSDCardDownload();//创建下载图片保存文件夹
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
        }

        //彩色的图标，而不是统一的图标颜色
        navigationView.setItemIconTintList(null);
        /*侧滑栏点击事件的方法*/
        setupDrawerContent(navigationView);
        /* 顶部可滑动tabs*/
        setTabs();

    }

    /**
     * 界面组组件的声明
     */
    private void initView() {
        mTabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return true;
    }

    /*
    * toolbar里面菜单的点击事件
    * */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//侧滑栏导航图标点击事件，侧滑栏弹出
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
            break;
            default:
        }
        return true;
    }

    /**
     * 设置侧滑菜单的点击事件
     */
    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.nav_download: {
                                Intent intent = new Intent(MainActivity.this, LocalDownloadActivity.class);
                                intent.putExtra(Constants.TYPE_NAME, (menuItem.getTitle().toString()));
                                startActivity(intent);
                            }
                            break;
                            case R.id.nav_location: {
                                Intent intent = new Intent(MainActivity.this, LocalPicturesActivity.class);
                                intent.putExtra(Constants.TYPE_NAME, (menuItem.getTitle().toString()));
                                startActivity(intent);
                            }
                            break;
                            case R.id.nav_set: {
                                mToolbar.setTitle(menuItem.getTitle().toString());
                            }
                            break;
                            case R.id.nav_clear: {
                                cleanCache();
                            }
                            break;
                            case R.id.nav_suggestion: {
                                mToolbar.setTitle(menuItem.getTitle().toString());
                            }
                            break;
                            case R.id.nav_about: {
                                Intent intent = new Intent(MainActivity.this, AboutUsActivity.class);
                                intent.putExtra(Constants.TYPE_NAME, (menuItem.getTitle().toString()));
                                startActivity(intent);
                            }
                            break;
                            case R.id.nav_exit: {
                                new AlertDialog.Builder(MainActivity.this)
                                        .setIcon(R.drawable.ic_launcher)
                                        .setTitle("退出")
                                        .setMessage("您确定要退出？")
                                        .setNegativeButton("取消", null)
                                        .setPositiveButton("确定",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog,
                                                                        int which) {
                                                        finish();
                                                    }
                                                }).show();
                            }
                            break;
                        }
                        mDrawerLayout.closeDrawers();
                        return false;
                    }
                });
    }

    /**
     * 主页添加可滑动fragment页（最新，最热，分类）
     */
    private void setTabs() {
        HomePagerAdapter homePagerAdapter = new HomePagerAdapter(getSupportFragmentManager());
        homePagerAdapter.addTab(new EverydayFragment(), "每日");
        homePagerAdapter.addTab(new RecommendFragment(), "推荐");
        homePagerAdapter.addTab(new CategoryFragment(), "分类");
        viewPager.setAdapter(homePagerAdapter);
        //把tabLayout和Viewpager关联起来
        mTabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 按返回键弹出对话框确定退出
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {

            new AlertDialog.Builder(this)
                    .setIcon(R.drawable.ic_launcher)
                    .setTitle("退出")
                    .setMessage("您确定要退出？")
                    .setNegativeButton("取消", null)
                    .setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            }).show();
        }
        return super.onKeyDown(keyCode, event);
    }

    /*
    *在SD卡上创建一个本地图片文件夹
    */
    public void createSDCardCache() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + Constants.WALLPAPER_CACHE;
            File filePath = new File(path);
            if (!filePath.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                filePath.mkdirs();
            }
        } else {
            return;
        }
    }

    /*
     *在SD卡上创建一个下载图片文件夹
     */
    public void createSDCardDownload() {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            // 创建一个文件夹对象，赋值为外部存储器的目录
            File sdcardDir = Environment.getExternalStorageDirectory();
            //得到一个路径，内容是sdcard的文件夹路径和名字
            String path = sdcardDir.getPath() + Constants.WALLPAPER_DOWNLOAD;
            File filePath = new File(path);
            if (!filePath.exists()) {
                //若不存在，创建目录，可以在应用启动的时候创建
                filePath.mkdirs();
            }
        } else {
            return;
        }
    }

    /**
     * 清除缓存
     */
    public void cleanCache() {
        AlertDialog.Builder cleanDia = new AlertDialog.Builder(MainActivity.this);
        cleanDia.setTitle("清除")
                .setMessage("确定清除数据缓存")
                .setCancelable(false)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        File sdcardDir = Environment.getExternalStorageDirectory();
                        //得到一个路径，内容是添加本地图片的存放目录
                        String path = sdcardDir.getPath() + Constants.WALLPAPER_CACHE;
                        File file = new File(path);
                        DeleteCache deleteCache = new DeleteCache();
                        deleteCache.DeleteCache(file);
                        createSDCardCache();
                    }
                });
        cleanDia.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        cleanDia.show();
    }
}
