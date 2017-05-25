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
import com.example.cnlcnn.localPictures.LocalPictureSetActivity;
import com.example.cnlcnn.model.LocalPictureModel;
import com.example.cnlcnn.wallpaper.R;

import java.util.List;

import static com.example.cnlcnn.wallpaper.R.id.picture;

/**
 * Created by cnlcnn on 2017/5/5.
 */

public class LocalPictureAdapter extends RecyclerView.Adapter<LocalPictureAdapter.ViewHolder> {

    private Context mContext;
    private List<LocalPictureModel> mPictureList;


    static class ViewHolder extends RecyclerView.ViewHolder {

        CardView mCardView;
        ImageView pictureView;

        public ViewHolder(View itemView) {
            super(itemView);

            mCardView = (CardView) itemView;
            pictureView = (ImageView) itemView.findViewById(picture);

        }
    }

    public LocalPictureAdapter(List<LocalPictureModel> pictureList) {
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

                LocalPictureModel localPicture = mPictureList.get(position);
                Intent intent = new Intent();

                intent.setClass(mContext, LocalPictureSetActivity.class);
                intent.putExtra(Constants.LOCAL_PICTURE_PATH, localPicture.getPath());
                mContext.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        LocalPictureModel localPicture = mPictureList.get(position);
        if (localPicture.getPath() == null) {

        } else {
            Glide.with(mContext).load(localPicture.getPath()).into(holder.pictureView);
        }
    }

    @Override
    public int getItemCount() {
        return mPictureList.size();
    }
}
