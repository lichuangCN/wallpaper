package com.example.cnlcnn.everyday;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;

import com.example.cnlcnn.adapter.CommonAdapter;
import com.example.cnlcnn.model.CommonModel;
import com.example.cnlcnn.model.EverydayDataModel;
import com.example.cnlcnn.utils.L;
import com.example.cnlcnn.wallpaper.R;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnlcnn on 2017/5/18.
 */

public class EverydayActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String nextUrl;
    private String name;
    private String url;

    private Toolbar mToolbar;

    private ArrayList<String> mListBigUrls = new ArrayList<>();

    private List<CommonModel> mList = new ArrayList<>();
    private CommonAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_everyday);

        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(EverydayActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);

        Intent intent = getIntent();
        name = intent.getStringExtra("time");
        url = intent.getStringExtra("url");

        if (!TextUtils.isEmpty(name)) {
            mToolbar.setTitle(name);
            setSupportActionBar(mToolbar);

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
                actionBar.setHomeAsUpIndicator(R.drawable.ic_back);
            }
        }

        if (!TextUtils.isEmpty(url)) {
            RxVolley.get(url, new HttpCallback() {
                @Override
                public void onSuccess(String t) {
                    parsingJson(t);
                }

                @Override
                public void onFailure(VolleyError error) {
                    L.i(error.toString());
                }
            });
        }
    }

    //解析
    private void parsingJson(String t) {
        Gson gson = new Gson();
        EverydayDataModel model = gson.fromJson(t, EverydayDataModel.class);
        for (int i = 0; i < model.getData().size(); i++) {
            CommonModel models = new CommonModel();
            models.setKey(model.getData().get(i).getKey());
            models.setBig(model.getData().get(i).getBig());
            mListBigUrls.add(model.getData().get(i).getBig());
            models.setDown(model.getData().get(i).getDown());
            models.setDown_stat(model.getData().get(i).getDown_stat());
            models.setSmall(model.getData().get(i).getSmall());
            mList.add(models);
        }
        mAdapter = new CommonAdapter(EverydayActivity.this, mList, mListBigUrls);
        mRecyclerView.setAdapter(mAdapter);
    }

    /**
     * toolbar里面菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
               EverydayActivity.this.finish();
            }
            break;
            default:
        }
        return true;
    }
}
