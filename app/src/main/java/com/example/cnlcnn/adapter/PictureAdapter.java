package com.example.cnlcnn.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.cnlcnn.entity.Constants;
import com.example.cnlcnn.model.Picture;
import com.example.cnlcnn.picture.NetPictureSetActivity;
import com.example.cnlcnn.wallpaper.R;

import java.util.List;

import static com.example.cnlcnn.wallpaper.R.id.picture;

/**
 * Created by cnlcnn on 2017/5/4.
 */

public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.ViewHolder> {
    private Context mContext;
    private List<Picture> mPictureList;

    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView pictureView;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView;
            pictureView = (ImageView) itemView.findViewById(picture);
        }
    }

    public PictureAdapter(List<Picture> pictureList) {
        mPictureList = pictureList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (mContext == null) {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_picture_item, parent, false);

        final ViewHolder holder = new ViewHolder(view);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Picture picture = mPictureList.get(position);
                Intent intent = new Intent();

                    intent.setClass(mContext, NetPictureSetActivity.class);
                    intent.putExtra(Constants.PICTURE_IMAGE_ID, picture.getImageId());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Picture picture = mPictureList.get(position);
        Glide.with(mContext).load(picture.getImageId()).into(holder.pictureView);
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }
}
