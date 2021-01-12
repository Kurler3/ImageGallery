package com.miguel.imagegallery;

public interface OnItemClickListener {
    //void onPicClicked(PicHolder holder, int position, ArrayList<pictureFacer> pics);
    void onPicClicked(String pictureFolderPath,String folderName);
}
