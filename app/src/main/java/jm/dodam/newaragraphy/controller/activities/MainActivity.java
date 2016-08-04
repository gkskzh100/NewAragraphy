package jm.dodam.newaragraphy.controller.activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import jm.dodam.newaragraphy.JsoupAsyncTask;
import jm.dodam.newaragraphy.R;

public class MainActivity extends Activity {

    private static final int MY_PERMISSION_REQUEST_STORAGE = 0;
    private static final String TAG = "checkPermission";
    private ImageView mainExImageView;
    private ImageButton mainWriteImgBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashActivity.class));
        init();
        checkPermission();
        parseImages();

    }

    private void init() {
        mainExImageView = (ImageView)findViewById(R.id.mainExImageView);
        mainWriteImgBtn = (ImageButton) findViewById(R.id.mainWriteImgBtn);

        setListener();
    }

    private void setListener() {
        mainWriteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), WriteActivity.class));
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED) {

            if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                Toast.makeText(this,"Read/WRite external storage", Toast.LENGTH_SHORT).show();
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    MY_PERMISSION_REQUEST_STORAGE);
        } else {
            writeFile();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE :
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    writeFile();
                } else {
                    Log.d(TAG, "Permission always deny");
                }
                break;
        }
    }

    private void writeFile() {
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.txt");
        try {
            Log.d(TAG, "create new File : " + file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseImages(){

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(getApplicationContext(),"https://unsplash.com/collections/225685/surf");
        jsoupAsyncTask.execute();
    }
}
