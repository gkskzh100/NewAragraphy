package jm.dodam.newaragraphy.controller.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.utils.CustomLoading;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.utils.Global;
import jm.dodam.newaragraphy.utils.SingleMediaScanner;

/**
 * Created by Bong on 2016-09-27.
 */
public class ChoosedUserImageActivity extends Activity {

    final DBManager userImageDB = new DBManager(ChoosedUserImageActivity.this, "UserImage.db", null, Global.DB_VERSION);
    private ImageView imageView;
    private RelativeLayout userCaptureLayout;
    private ImageButton choosedExitBtn, choosedDownloadBtn;
    private LinearLayout topLayout, bottomLayout;
    private Boolean imageVisible = false;
    private String imageUri = null, savePath = null;
    private int imageWidth = 0, imageHeight = 0;
    private Bitmap bm = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosedimage);
        init();
        setChooseImage();
    }

    private void init() {
        userCaptureLayout = (RelativeLayout)findViewById(R.id.UserCaptureLayout);
        choosedDownloadBtn = (ImageButton)findViewById(R.id.choosedDownloadBtn);
        choosedExitBtn = (ImageButton)findViewById(R.id.choosedExitBtn);
        imageView = (ImageView)findViewById(R.id.choosedImage);
        topLayout = (LinearLayout)findViewById(R.id.topLayout);
        bottomLayout = (LinearLayout)findViewById(R.id.bottomLayout);

        topLayout.setVisibility(View.GONE);
        bottomLayout.setVisibility(View.GONE);
        imageUri = userImageDB.getImageList().get(getIntent().getIntExtra("position",0));
        setListener();
    }

    private void setListener() {
        choosedDownloadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                captureImage();
            }
        });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!imageVisible){
                    imageVisible = true;

                    topLayout.setVisibility(View.VISIBLE);
                    bottomLayout.setVisibility(View.VISIBLE);
                }else{
                    imageVisible = false;

                    topLayout.setVisibility(View.GONE);
                    bottomLayout.setVisibility(View.GONE);
                }
            }
        });
        choosedExitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void setChooseImage(){
        BitmapFactory.Options bfo = new BitmapFactory.Options();
        bfo.inSampleSize = 2;
        bm = BitmapFactory.decodeFile(imageUri,bfo);

        imageWidth = bm.getWidth();
        imageHeight = bm.getHeight();
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels;

        float ratio = screenWidth / imageWidth;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) userCaptureLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (imageHeight * ratio);
        userCaptureLayout.setLayoutParams(layoutParams);

        imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        imageView.setImageBitmap(bm);
    }

    private void captureImage(){

        String folder = "Aragraphy";


        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date currentTime_1 = new Date();
            String dateString = formatter.format(currentTime_1);
            File sdCardPath = Environment.getExternalStorageDirectory();
            File dirs = new File(Environment.getExternalStorageDirectory(), folder);

            if (!dirs.exists()) {
                dirs.mkdirs();
                Log.d("CAMERA_TEST", "Directory Created");
            }
            userCaptureLayout.setDrawingCacheEnabled(true);

            Bitmap captureView = userCaptureLayout.getDrawingCache();
            FileOutputStream fos;
            String save;

            try {
                userCaptureLayout.refreshDrawableState();

                save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".png";
                savePath = save;
                fos = new FileOutputStream(save);
                captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);

                File file = new File(save);
                SingleMediaScanner mScanner = new SingleMediaScanner(getApplicationContext(), file);
                Toast.makeText(ChoosedUserImageActivity.this, "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e("Screen", "" + e.toString());
        }


        String text = "#Aragraphy";
        File file = new File(savePath);
    }

}

