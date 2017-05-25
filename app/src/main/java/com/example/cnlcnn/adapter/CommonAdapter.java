package com.example.cnlcnn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.model.CommonModel;
import com.example.cnlcnn.picture.NetPictureSetActivity;
import com.example.cnlcnn.utils.GlideUtils;
import com.example.cnlcnn.wallpaper.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by cnlcnn on 2017/5/16.
 */

public class CommonAdapter extends RecyclerView.Adapter<CommonAdapter.ViewHolder> {

    private Context mContext;
    private List<CommonModel> mList;
    private CommonModel model;
    private ArrayList<String> mListBigUrl = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView image;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.picture);
        }
    }

    public CommonAdapter(Context context, List<CommonModel> pictureList, ArrayList<String> mListUrl) {
        this.mContext = context;
        this.mList = pictureList;
        this.mListBigUrl = mListUrl;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_recommend_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();//获取当前点击的item值

                Intent intent = new Intent();
                intent.putExtra("position", position);
//                intent.putExtra("url",mList.get(position).getBig());
                intent.putStringArrayListExtra(Constants.NET_PICTURE_PATH, mListBigUrl);
//                intent.putExtra("url",mList.get(position).getSmall());
                intent.setClass(mContext, NetPictureSetActivity.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(CommonAdapter.ViewHolder holder, int position) {
        model = mList.get(position);
        GlideUtils.loadImageCrop(mContext, model.getSmall(), holder.image);
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

