package com.example.cnlcnn.pictureFragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cnlcnn.adapter.EverydayAdapter;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.imp.APIimp;
import com.example.cnlcnn.model.EverydayModel;
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

/*
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间:  2017/5/3
 *  描述： 首页每日图片
 */


public class EverydayFragment extends Fragment {

    private APIimp apiImp;

    private RecyclerView mRecyclerView;
    private EverydayAdapter mAdapter;

    private List<EverydayModel> mList = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_everyday, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mRecyclerView = (RecyclerView)view.findViewById(R.id.list);
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
                    parsingEveryDay(response.body().getEveryday());
                }
            }

            @Override
            public void onFailure(Call<WallpaperApiModel> call, Throwable t) {
                L.i(t.toString());
            }
        });
    }

    //解析每天
    private void parsingEveryDay(List<WallpaperApiModel.EverydayBean> everyday) {

        for (int i = 0; i < everyday.size(); i++) {
            EverydayModel model = new EverydayModel();
            model.setTime(everyday.get(i).getDate());
            model.setImage(everyday.get(i).getImage());
            model.setUrl(everyday.get(i).getUrl());
            mList.add(model);
        }

        mAdapter = new EverydayAdapter(getActivity(), mList);
        mRecyclerView.setAdapter(mAdapter);
    }

}
