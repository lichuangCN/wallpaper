package com.example.cnlcnn.pictureFragments;
/*
 *  项目名：  WallPaper
 *  文件名:   CategoryFragment
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 *  描述：    分类
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cnlcnn.adapter.CListAdapter;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.imp.APIimp;
import com.example.cnlcnn.model.CategoryModel;
import com.example.cnlcnn.model.WallpaperApiModel;
import com.example.cnlcnn.utils.L;
import com.example.cnlcnn.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class CategoryFragment extends Fragment {

    private APIimp apiImp;

    private List<CategoryModel> mList = new ArrayList<>();
    private List<String> mListUrl = new ArrayList<>();

    private CListAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_categray_picture, container, false);

        initView(view);
        return view;
    }

    private void initView(View view) {

        mList.clear();
        mRecyclerView = (RecyclerView) view.findViewById(R.id.list);
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(layoutManager);

        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.WALLPAPER_BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()).build();
        apiImp = retrofit.create(APIimp.class);

        Call<WallpaperApiModel> call = apiImp.getWallpaperApi();
        //请求数据
        call.enqueue(new Callback<WallpaperApiModel>() {
            @Override
            public void onResponse(Call<WallpaperApiModel> call, Response<WallpaperApiModel> response) {
                if (response.isSuccessful()) {
                    parsingJson(response.body().getCategory());
                }
            }

            @Override
            public void onFailure(Call<WallpaperApiModel> call, Throwable t) {
                L.i(t.toString());
            }
        });
    }

    //解析
    private void parsingJson(List<WallpaperApiModel.CategoryBean> category) {
        for (int i = 0; i < category.size(); i++) {
            CategoryModel model = new CategoryModel();
            model.setName(category.get(i).getName());
            model.setCover(category.get(i).getCover());
            model.setUrl(category.get(i).getUrl());
            mList.add(model);
        }
        mAdapter = new CListAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);
    }
}
