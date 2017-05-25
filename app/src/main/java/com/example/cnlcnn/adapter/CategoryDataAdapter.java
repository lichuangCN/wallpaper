package com.example.cnlcnn.adapter;
/*
 *  项目名：  WallPaper
 *  文件名:   CategoryDataAdapter
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 *  描述：    分类详情
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.cnlcnn.model.CategoryGridModel;
import com.example.cnlcnn.utils.GlideUtils;
import com.example.cnlcnn.wallpaper.R;

import java.util.List;

public class CategoryDataAdapter extends BaseAdapter {

    public Context mContext;
    private LayoutInflater inflater;
    private List<CategoryGridModel> mList;
    private CategoryGridModel model;

    public CategoryDataAdapter(Context mContext, List<CategoryGridModel> mList) {
        this.mContext = mContext;
        this.mList = mList;
        inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder = null;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = inflater.inflate(R.layout.activity_special_grid_item, null);
            viewHolder.iv_main_grid_icon = (ImageView) view.findViewById(R.id.iv_main_grid_icon);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        model = mList.get(i);
        GlideUtils.loadImageCrop(mContext, model.getSmall(), viewHolder.iv_main_grid_icon);
        return view;
    }

    class ViewHolder {
        private ImageView iv_main_grid_icon;
    }

}
