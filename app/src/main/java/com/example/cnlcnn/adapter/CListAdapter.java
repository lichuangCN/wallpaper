package com.example.cnlcnn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.cnlcnn.category.CategoryActivity;
import com.example.cnlcnn.model.CategoryModel;
import com.example.cnlcnn.utils.GlideUtils;
import com.example.cnlcnn.wallpaper.R;

import java.util.List;

/*
 *  项目名：  WallPaper
 *  文件名:   CListAdapter
 *  创建者:   LiChuang
 *  创建时间:  2017/5/15
 *  描述：    分类详情
 */

public class CListAdapter extends RecyclerView.Adapter<CListAdapter.ViewHolder> {

    private Context mContext;
    private List<CategoryModel> mList;
    private CategoryModel model;


    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView image;
        TextView name;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.image);
            name = (TextView) itemView.findViewById(R.id.title);
        }
    }

    public CListAdapter(Context context, List<CategoryModel> pictureList) {
        this.mContext = context;
        mList = pictureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.tab_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = holder.getAdapterPosition();//获取当前点击的item值
                Intent intent = new Intent();
                intent.putExtra("name",mList.get(position).getName());
                intent.putExtra("url",mList.get(position).getUrl());
                intent.setClass(mContext, CategoryActivity.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        model = mList.get(position);
        GlideUtils.loadImageCrop(mContext, model.getCover(), holder.image);
        holder.name.setText(model.getName());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
