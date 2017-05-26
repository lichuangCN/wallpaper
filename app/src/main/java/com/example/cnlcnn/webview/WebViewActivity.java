package com.example.cnlcnn.webview;
/*
 *  项目名：  Wallpaper
 *  文件名:   WebViewActivity
 *  创建者:   LiChuang
 *  创建时间:  2017/5/26
 *  描述：    浏览器
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.example.cnlcnn.utils.L;
import com.example.cnlcnn.wallpaper.R;


public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private String title, url;
    private ProgressBar progressBar;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        initView();
    }

    private void initView() {

        mWebView = (WebView) findViewById(R.id.mWebView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        mToolbar = (Toolbar)findViewById(R.id.toolbar);

        Intent intent = getIntent();
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");

        if (!TextUtils.isEmpty(title)) {
            mToolbar.setTitle(title);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }else {
            getSupportActionBar().setTitle("加载失败");
        }

        mWebView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        WebSettings seting = mWebView.getSettings();
        //设置webview支持javascript脚本
        seting.setJavaScriptEnabled(true);
        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    //加载完网页进度条消失
                    progressBar.setVisibility(View.GONE);
                } else {
                    //开始加载网页时显示进度条
                    progressBar.setVisibility(View.VISIBLE);
                    //设置进度值
                    progressBar.setProgress(newProgress);
                }

            }
        });

        if (!TextUtils.isEmpty(url)) {
            mWebView.loadUrl(url);
        } else {
            L.e("url error");
        }
    }

    /**
     * toolbar里面菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
                WebViewActivity.this.finish();
            }
            break;
            default:
        }
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //当webview不是处于第一页面时，返回上一个页面
            if (mWebView.canGoBack()) {
                mWebView.goBack();
                return true;
            } else {
                //当webview处于第一页面时,直接退出程序
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}
