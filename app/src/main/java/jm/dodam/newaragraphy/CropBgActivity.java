package jm.dodam.newaragraphy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by Bong on 2016-08-03.
 */
public class CropBgActivity extends AppCompatActivity {
    public static Bitmap bgBitmap;
    ImageButton SelectAcceptImageBtn, SelectExitImageBtn;
    SelectBackActivity selectBackActivity = (SelectBackActivity)SelectBackActivity.mySelectBackActivity;
    URL url;
    private ImageView resultView, freeCropImageBtn, squareCropImageBtn, hQuadrangleCropImageBtn, vQuadrangleCropImageBtn;
    CropImageView cropImageView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        setCustomActionbar();
        init();
        setListener();

        cropImage();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void cropImage() {
        //URL url = new URL("https://images.unsplash.com/photo-1445771832954-9c7fb748f214?dpr=1&amp;auto=compress,format&amp;crop=entropy&amp;fit=crop&amp;w=767&amp;h=512&amp;q=80&quot;");



        cropImageView.setImageResource(R.drawable.instagram_btn);
        cropImageView.setOnGetCroppedImageCompleteListener(new CropImageView.OnGetCroppedImageCompleteListener() {
            @Override
            public void onGetCroppedImageComplete(CropImageView view, Bitmap bitmap, Exception error) {
                if (error == null) {
                    if (bitmap != null) {
                        cropImageView.setImageBitmap(bitmap);
                    }
                }

            }
        });
        cropImageView.setAutoZoomEnabled(false);
        cropImageView.setAspectRatio(1, 1);//비율 값
        cropImageView.setFixedAspectRatio(false);//비율 Free ON/OFF
        cropImageView.setScaleType(CropImageView.ScaleType.FIT_CENTER);
//        cropImageView.getCroppedImageAsync(400, 400);


    }


    private void init() {
        SelectAcceptImageBtn = (ImageButton) findViewById(R.id.SelectAcceptImageBtn);
        SelectExitImageBtn = (ImageButton) findViewById(R.id.SelectExitImageBtn);
        cropImageView = (CropImageView) findViewById(R.id.cropImage);
        freeCropImageBtn = (ImageView) findViewById(R.id.freeCropImageBtn);
        squareCropImageBtn = (ImageView) findViewById(R.id.squareCropImageBtn);
        hQuadrangleCropImageBtn = (ImageView) findViewById(R.id.hQuadrangleCropImageBtn);
        vQuadrangleCropImageBtn = (ImageView) findViewById(R.id.vQuadrangleCropImageBtn);
    }

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_crop, null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3e3e3e")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }

    private void setListener() {
        SelectExitImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
        SelectAcceptImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bgBitmap = cropImageView.getCroppedImage();
                Intent intent = new Intent(getApplicationContext(),WriteActivity.class);
                intent.putExtra("background",bgBitmap);
                selectBackActivity.finish();
                finish();

            }
        });
        freeCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(false);
            }
        });
        squareCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(1, 1);
            }
        });
        hQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(4, 3);
            }
        });
        vQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(3, 4);
            }
        });
    }


}
