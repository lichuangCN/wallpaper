package com.example.cnlcnn.aboutUs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 *  项目名：  WallPaper
 *  创建者:   LiChuang
 *  创建时间： 2017/5/7.
 *  描述：    关于作者我
 */

public class AboutUsActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener{

    private CircleImageView profile_image;
    private ListView mListView;
    private List<String> mList = new ArrayList<>();
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aboutus);

        initView();
    }

    private void initView() {

        mList.add("作者: LiChuang");
        mList.add("E-mail: 3532830260@qq.com");
        mList.add("Github: https://github.com/cnlcnn/wallpaper");

        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        profile_image.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.mListView);
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_image:
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                //不需要
                break;
            case 2:
                Constants.startWebView(this, "Github", Constants.GITHUB);
                break;
        }
    }
}
