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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
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
        /*
        * TODO: "temp.txt" 처럼 하드코딩은 지양하는 것이 좋음
        * utils 에 Global 클래스를 만들어 둠 그곳에
        * public static final String tempPath = "temp.txt";
        * 이런식으로 선언해두고 가져다 쓰는 것이 안전함.
        * 적어도 "tepm.txt" 이런식으로 오타를 낼 일은 없음
        * 추후에 파일 경로가 바뀌어도 Global 의 변수만 바꿔주면 되므로 유지보수에 유리함.
        * */
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.txt");
        try {
            Log.d(TAG, "create new File : " + file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void parseImages(){
        // TODO: 변수명 + 하드코딩
        //사진 사이트에서 이미지 파싱중
        JsoupAsyncTask imageParsing = new JsoupAsyncTask(getApplicationContext(),"https://unsplash.com/collections/225685/surf");
        imageParsing.execute();
    }
}
