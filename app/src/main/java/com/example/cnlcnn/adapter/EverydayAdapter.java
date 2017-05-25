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

import com.example.cnlcnn.everyday.EverydayActivity;
import com.example.cnlcnn.model.EverydayModel;
import com.example.cnlcnn.utils.GlideUtils;
import com.example.cnlcnn.wallpaper.R;

import java.util.List;

/**
 * Created by cnlcnn on 2017/5/18.
 */

public class EverydayAdapter extends RecyclerView.Adapter<EverydayAdapter.ViewHolder> {

    private Context mContext;
    private List<EverydayModel> mList;
    private EverydayModel model;

//    private List<String> mListUrls = new ArrayList<>();
//    private List<String> mListTitles = new ArrayList<>();

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView image;
        TextView time;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView;
            image = (ImageView) itemView.findViewById(R.id.picture);
            time = (TextView)itemView.findViewById(R.id.time);
        }
    }

    public EverydayAdapter(Context context, List<EverydayModel> pictureList) {
        this.mContext = context;
        this.mList = pictureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.fragment_everyday_item, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();//获取当前点击的item值

                Intent intent = new Intent();
//                intent.putExtra("position", position);
//                intent.putExtra("url", mListUrls.get(position));
//                intent.putExtra("name", mListTitles.get(position));
//                intent.setClass(mContext, NetPictureSetActivity.class);
                intent.putExtra("time",mList.get(position).getTime());
                intent.putExtra("url",mList.get(position).getUrl());
                intent.setClass(mContext, EverydayActivity.class);
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        model = mList.get(position);
        GlideUtils.loadImageCrop(mContext, model.getImage(), holder.image);
        holder.time.setText(model.getTime());
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
