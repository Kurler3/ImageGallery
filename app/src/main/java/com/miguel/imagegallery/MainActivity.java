package com.miguel.imagegallery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnItemClickListener{
    private int READ_STORAGE_PERMISSION_CODE = 1;
    private int WRITE_STORAGE_PERMISSION_CODE = 2;


    TextView mEmptyText;
    RecyclerView mFolderRecycler;
    GridLayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if permission is granted
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED){
            requestStoragePermission();
        }

        // Initializing views
        mEmptyText = findViewById(R.id.no_photos_text);

        mFolderRecycler = findViewById(R.id.folder_recycler_view);

        // Shows 3 folder per row
        mLayoutManager = new GridLayoutManager(this, 3);

        ArrayList<ImageFolder> folders = getFolders();

        if(folders.isEmpty()){
           mEmptyText.setVisibility(View.VISIBLE);
        }else{
            FolderRecyclerAdapter adapter = new FolderRecyclerAdapter(this, folders, this);

            mFolderRecycler.setAdapter(adapter);
            mFolderRecycler.setLayoutManager(mLayoutManager);
        }
    }
    private void requestStoragePermission(){
        // Check if we should show the user a dialog explaining why he needs to accept this request
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Access To Storage Permission Necessary")
                    .setMessage("This app needs access to your storage to be able to show you your photos!")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            READ_STORAGE_PERMISSION_CODE))
                    .setNegativeButton("Refuse", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }else{
            // The code is passed to later identify the request
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    READ_STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == READ_STORAGE_PERMISSION_CODE){
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Storage permission was granted!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private ArrayList<ImageFolder> getFolders(){
        ArrayList<ImageFolder> folders = new ArrayList<>();
        ArrayList<String> paths = new ArrayList<>();

        Uri allImagesUri;

        // Check if there is a SD card
        if(android.os.Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED)){
            // Content provider for all images in SD card
            allImagesUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        }else{
            allImagesUri = MediaStore.Images.Media.INTERNAL_CONTENT_URI;
        }

        String[] projection = { MediaStore.Images.ImageColumns.DATA ,MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,MediaStore.Images.Media.BUCKET_ID};

        Cursor cursor = getContentResolver().query(allImagesUri,
                projection,
                null, null, null);

        try{
            if(cursor!=null){
                cursor.moveToFirst();
            }
            // Do this for each image found
            do{
                ImageFolder imageFolder = new ImageFolder();

                // Extract data from cursor
                String folder = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.BUCKET_DISPLAY_NAME));
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.ImageColumns.DATA));

                // Get the path of the folder for each image
                // imagePath is the full path of the image, folder just the name of the folder it is in, example: "Camera"

                // Gets the path until the folder, but doesn't include it so I add it next
                String folderPath = imagePath.substring(0, imagePath.lastIndexOf(folder + "/"));

                folderPath = folderPath + folder + "/";

                // If the folder is not in the paths array then its a "new" one
                if(!paths.contains(folderPath)){
                    paths.add(folderPath);

                    imageFolder.setPath(folderPath);
                    imageFolder.setName(folder);
                    imageFolder.setFirstPic(imagePath);
                    imageFolder.addPic();
                    // Add the folder
                    folders.add(imageFolder);
                }else{
                    // If the path was already in the array then find which item in the imagefolder items it belongs to and add its number of pics
                    for(int i=0;i<folders.size();i++){
                        if(folders.get(i).getPath().equals(folderPath)){
                            folders.get(i).setFirstPic(imagePath);
                            folders.get(i).addPic();
                        }
                    }
                }
            }while(cursor.moveToNext());
            cursor.close();
        }catch (Exception e){
            e.printStackTrace();
        }

        return folders;
    }

    @Override
    public void onPicClicked(PictureRecyclerAdapter.PictureHolder holder, int position, ArrayList<Picture> pics) {

    }

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        // Launches a new intent to show the images inside that folder
        Intent intent = new Intent(this, FolderImageDisplay.class);

        intent.putExtra(FolderImageDisplay.FOLDER_PATH, pictureFolderPath);
        intent.putExtra(FolderImageDisplay.FOLDER_NAME, folderName);

        startActivity(intent);
    }
}