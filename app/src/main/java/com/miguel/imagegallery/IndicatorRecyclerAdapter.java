package com.miguel.imagegallery;

import android.content.Context;
import android.graphics.Color;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

public class IndicatorRecyclerAdapter extends RecyclerView.Adapter<IndicatorRecyclerAdapter.IndicatorViewHolder> {

    private Context mContext;
    private ArrayList<Picture> mPictureArray = new ArrayList<>();
    private onIndicatorClickedListener mIndicatorClickedListener;

    public IndicatorRecyclerAdapter(Context mContext, ArrayList<Picture> mPictureArray, onIndicatorClickedListener mIndicatorClickedListener) {
        this.mContext = mContext;
        this.mPictureArray = mPictureArray;
        this.mIndicatorClickedListener = mIndicatorClickedListener;
    }

    public class IndicatorViewHolder extends RecyclerView.ViewHolder{
        private CardView mCard;
        private ImageView mImage;
        View mController;

        public IndicatorViewHolder(@NonNull View itemView) {
            super(itemView);

            mCard = itemView.findViewById(R.id.indicatorCard);
            mImage = itemView.findViewById(R.id.imageIndicator);
            mController = itemView.findViewById(R.id.activeImage);
        }
    }
    @NonNull
    @Override
    public IndicatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.indicator_item, parent, false);

        return new IndicatorViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IndicatorViewHolder holder, int position) {
        final Picture picture = mPictureArray.get(position);

        /** If the picture is selected then set the controllers background to white */
        holder.mController.setBackgroundColor(picture.getSelected() ? Color.parseColor("#00000000")
                : Color.parseColor("#8c000000"));

        Glide.with(mContext)
                .load(picture.getPicturePath())
                .apply(new RequestOptions().centerCrop())
                .into(holder.mImage);

        holder.mCard.setOnClickListener(v -> {
            picture.setSelected(true);
            notifyDataSetChanged();
            mIndicatorClickedListener.onIndicatorClicked(position);
        });
    }

    @Override
    public int getItemCount() {
        return mPictureArray.size();
    }


}
