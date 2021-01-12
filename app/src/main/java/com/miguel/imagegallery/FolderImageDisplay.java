package com.miguel.imagegallery;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class FolderImageDisplay extends AppCompatActivity {
    public static final String FOLDER_PATH = "folderPath";
    public static final String FOLDER_NAME = "folderName";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_image_display);
    }
}