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
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

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




    }
    private void requestStoragePermission(){
        // Check if we should show the user a dialog explaining why he needs to accept this request
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)){

            new AlertDialog.Builder(this)
                    .setTitle("Acess To Storage Permission Necessary")
                    .setMessage("This app needs access to your storage to be able to show you your photos!")
                    .setPositiveButton("Ok", (dialog, which) ->
                            ActivityCompat.requestPermissions(MainActivity.this,
                                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            READ_STORAGE_PERMISSION_CODE))
                    .setNegativeButton("Refuse", (dialog, which) -> dialog.dismiss())
                    .create().show();
        }else{
            // The code is passed to later identify the request
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
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

    @Override
    public void onPicClicked(String pictureFolderPath, String folderName) {
        // Launches a new intent to show the images inside that folder
        Intent intent = new Intent(this, FolderImageDisplay.class);

        intent.putExtra(FolderImageDisplay.FOLDER_PATH, pictureFolderPath);
        intent.putExtra(FolderImageDisplay.FOLDER_NAME, folderName);

        startActivity(intent);
    }
}