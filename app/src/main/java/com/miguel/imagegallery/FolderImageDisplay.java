package com.miguel.imagegallery;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.transition.Fade;
import android.view.View;
import android.widget.ProgressBar;

import java.util.ArrayList;
import java.util.Collections;

public class FolderImageDisplay extends AppCompatActivity implements OnItemClickListener{
    public static final String FOLDER_PATH = "folderPath";
    public static final String FOLDER_NAME = "folderName";

    RecyclerView mPictureRecycler;
    String mFolderName;
    String mFolderPath;
    ProgressBar mProgressBar;
    ArrayList<Picture> mPictureArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_image_display);

        mPictureRecycler = findViewById(R.id.pictures_recycler_view);
        mProgressBar = findViewById(R.id.progress_bar);

        mFolderName = getIntent().getStringExtra(FOLDER_NAME);
        mFolderPath = getIntent().getStringExtra(FOLDER_PATH);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 4);
        mPictureRecycler.setLayoutManager(gridLayoutManager);

        mPictureArray = new ArrayList<>();

        if(mPictureArray.isEmpty()){
            mProgressBar.setVisibility(View.VISIBLE);
            mPictureArray = getPicturesByFolder(mFolderPath);
            mPictureRecycler.setAdapter(new PictureRecyclerAdapter(this, mPictureArray, this));
            mProgressBar.setVisibility(View.INVISIBLE);
        }

        setTitle(mFolderName);
    }
    private ArrayList<Picture> getPicturesByFolder(String folderName){
        ArrayList<Picture> pictures = new ArrayList<>();

        Uri allPicturesInFolder;

        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            allPicturesInFolder = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }else{
            allPicturesInFolder = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        String[] projection = {MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.SIZE};

        Cursor cursor = FolderImageDisplay.this.getContentResolver().query(allPicturesInFolder, projection,
                MediaStore.Images.Media.DATA + " like ? ",
                new String[] {"%"+folderName+"%"}, null);

        try{
            if(cursor!=null){
                cursor.moveToFirst();
            }
            do{
                Picture picture = new Picture();

                picture.setPictureName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DISPLAY_NAME)));
                picture.setPicturePath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA)));
                picture.setPictureSize(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE)));

                pictures.add(picture);

            }while(cursor.moveToNext());
            cursor.close();
            // Reverse the array list
            Collections.reverse(pictures);
        }catch (Exception e){
            e.printStackTrace();
        }
        return pictures;
    }

    @Override
    public void onPicClicked(PictureRecyclerAdapter.PictureHolder holder, int position, ArrayList<Picture> pics) {
        PictureBrowser pictureBrowser = PictureBrowser.newInstance(this, pics, position);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            pictureBrowser.setEnterTransition(new Fade());
            pictureBrowser.setExitTransition(new Fade());
        }

        getSupportFragmentManager().beginTransaction()
                .addSharedElement(holder.mPictureImage, position + "picture")
                .add(R.id.browser_container, pictureBrowser)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {

    }
}