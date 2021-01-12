package com.miguel.imagegallery;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.File;
import java.util.ArrayList;

public class FolderRecyclerAdapter extends RecyclerView.Adapter<FolderRecyclerAdapter.FolderViewHolder> {

    ArrayList<ImageFolder> mFolderArray;
    Context mContext;
    OnItemClickListener mItemClickListener;

    public FolderRecyclerAdapter(Context context, ArrayList<ImageFolder> folders, OnItemClickListener listener){
        this.mContext = context;
        this.mFolderArray = folders;
        this.mItemClickListener = listener;
    }
    public class FolderViewHolder extends RecyclerView.ViewHolder{
        ImageView mFolderImage;
        TextView mFolderTitle, mFolderSize;
        CardView mCardView;
        public FolderViewHolder(@NonNull View itemView) {
            super(itemView);

            mFolderImage = itemView.findViewById(R.id.folderPic);
            mFolderTitle = itemView.findViewById(R.id.folderName);
            mFolderSize = itemView.findViewById(R.id.folderSize);
            mCardView = itemView.findViewById(R.id.folderCard);
        }
    }
    @NonNull
    @Override
    public FolderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View folder = LayoutInflater.from(parent.getContext()).inflate(R.layout.folder_item, parent, false);

        return new FolderViewHolder(folder);
    }

    @Override
    public void onBindViewHolder(@NonNull FolderViewHolder holder, int position) {
        ImageFolder imageFolder = mFolderArray.get(position);

        // Displays the first image on the folder. Glide loads a path

        Glide.with(mContext)
                .load(imageFolder.getFirstPic())
                .apply(new RequestOptions().centerCrop())
                .into(holder.mFolderImage);

        String folderName = imageFolder.getName();
        String folderSize = "" + imageFolder.getNumPics();

        holder.mFolderTitle.setText(folderName);
        holder.mFolderSize.setText(folderSize);

        holder.mCardView.setOnClickListener(v ->
                mItemClickListener.onPicClicked(imageFolder.getPath(),
                        imageFolder.getName()));
    }

    @Override
    public int getItemCount() {
        return mFolderArray.size();
    }
}