package com.miguel.imagegallery;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import static androidx.core.view.ViewCompat.setTransitionName;

public class PictureRecyclerAdapter extends RecyclerView.Adapter<PictureRecyclerAdapter.PictureHolder> {

    Context mContext;
    ArrayList<Picture> mPictureArray;
    OnItemClickListener mOnItemClickListener;

    public PictureRecyclerAdapter(Context context, ArrayList<Picture> pictures, OnItemClickListener listener){
        this.mContext = context;
        this.mPictureArray = pictures;
        this.mOnItemClickListener = listener;
    }

    public class PictureHolder  extends RecyclerView.ViewHolder {
        ImageView mPictureImage;
        public PictureHolder(@NonNull View itemView) {
            super(itemView);

            mPictureImage = itemView.findViewById(R.id.picture_image);
        }
    }
    @NonNull
    @Override
    public PictureHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);

        return new PictureHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PictureHolder holder, int position) {
        Picture picture = mPictureArray.get(position);

        Glide.with(mContext)
                .load(picture.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.mPictureImage);

        setTransitionName(holder.mPictureImage, (position) + "_image");

        holder.mPictureImage.setOnClickListener(
                v -> mOnItemClickListener.onPicClicked(holder, position, mPictureArray));
    }

    @Override
    public int getItemCount() {
        return mPictureArray.size();
    }
}
