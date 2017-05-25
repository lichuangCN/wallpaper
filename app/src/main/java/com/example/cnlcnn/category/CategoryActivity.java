package com.example.cnlcnn.category;
/*
 *  项目名：  WallPaper
 *  文件名:   GalleryAdapter
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;

import com.example.cnlcnn.adapter.CommonAdapter;
import com.example.cnlcnn.model.CategoryDataModel;
import com.example.cnlcnn.model.CommonModel;
import com.example.cnlcnn.utils.L;
import com.example.cnlcnn.wallpaper.R;
import com.google.gson.Gson;
import com.kymjs.rxvolley.RxVolley;
import com.kymjs.rxvolley.client.HttpCallback;
import com.kymjs.rxvolley.http.VolleyError;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private String nextUrl;
    private String name;
    private String url;

    private Toolbar mToolbar;


    private CommonAdapter mAdapter;
    private List<CommonModel> mList = new ArrayList<>();
    private ArrayList<String> mListBigUrls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        initView();
    }

    private void initView() {

        mRecyclerView = (RecyclerView) findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(CategoryActivity.this, 2);
        mRecyclerView.setLayoutManager(layoutManager);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        url = intent.getStringExtra("url");

        if (!TextUtils.isEmpty(name)) {
            mToolbar.setTitle(name);
            setSupportActionBar(mToolbar);
            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setDisplayHomeAsUpEnabled(true);
            }
        }

        if (!TextUtils.isEmpty(url)) {
            getData(url);
        }

        //监听滑动
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                //得到当前显示的最后一个item的view
                View lastChildView = recyclerView.getLayoutManager().getChildAt(recyclerView.getLayoutManager().getChildCount() - 1);
                //得到lastChildView的bottom坐标值
                int lastChildBottom = lastChildView.getBottom();
                //得到Recyclerview的底部坐标减去底部padding值，也就是显示内容最底部的坐标
                int recyclerBottom = recyclerView.getBottom() - recyclerView.getPaddingBottom();
                //通过这个lastChildView得到这个view当前的position值
                int lastPosition = recyclerView.getLayoutManager().getPosition(lastChildView);

                //判断lastChildView的bottom值跟recyclerBottom
                //判断lastPosition是不是最后一个position
                //如果两个条件都满足则说明是真正的滑动到了底部
                if (lastChildBottom == recyclerBottom && lastPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    //加载图片
                    loadData();
                }
            }
        });
    }

    /**
     * toolbar里面菜单的点击事件
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home: {//返回上一个界面
                CategoryActivity.this.finish();
            }
            break;
            default:
        }
        return true;
    }

    //加载数据
    private void loadData() {
        RxVolley.get(nextUrl, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(VolleyError error) {
                super.onFailure(error);
            }
        });
    }

    //解析
    private void getData(String url) {
        RxVolley.get(url, new HttpCallback() {
            @Override
            public void onSuccess(String t) {
                parsingJson(t);
                mAdapter = new CommonAdapter(CategoryActivity.this, mList, mListBigUrls);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onFailure(VolleyError error) {
                L.i(error.toString());
            }
        });
    }

    private void parsingJson(String t) {
        Gson gson = new Gson();
        CategoryDataModel model = gson.fromJson(t, CategoryDataModel.class);
        nextUrl = model.getLink().getNext();
        for (int i = 0; i < model.getData().size(); i++) {
            CommonModel models = new CommonModel();
            models.setSmall(model.getData().get(i).getSmall());
            models.setDown_stat(model.getData().get(i).getDown_stat());
            models.setDown(model.getData().get(i).getDown());
            models.setBig(model.getData().get(i).getBig());
            mListBigUrls.add(model.getData().get(i).getBig());
            models.setKey(model.getData().get(i).getKey());
            mList.add(models);
        }
    }
}
