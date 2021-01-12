package com.miguel.imagegallery;

import java.util.ArrayList;

public interface OnItemClickListener {
    void onPicClicked(PictureRecyclerAdapter.PictureHolder holder, int position, ArrayList<Picture> pics);
    void onPicClicked(String pictureFolderPath,String folderName);
}
