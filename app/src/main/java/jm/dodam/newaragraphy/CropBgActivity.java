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
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageActivity;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * Created by Bong on 2016-08-03.
 */
public class CropBgActivity extends AppCompatActivity {
    private TextView cropFreeTv, cropSquareTv, cropHsquareTv, cropVsquareTv;
    private ImageButton SelectAcceptImageBtn, SelectExitImageBtn;
    SelectBackActivity selectBackActivity = (SelectBackActivity)SelectBackActivity.mySelectBackActivity;
    WriteActivity writeActivity = (WriteActivity)WriteActivity.mtWriteActivity;
    private ImageView resultView, freeCropImageBtn, squareCropImageBtn, hQuadrangleCropImageBtn, vQuadrangleCropImageBtn;
    CropImageView cropImageView;
    String uri = null;
    Bitmap bitmap = null;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        setCustomActionbar();
        init();
        setListener();
        getImageResource();
        cropImage();


    }
    private void getImageResource(){
        int position = getIntent().getIntExtra("position",0);
        ImageResource imageResource = new ImageResource();
        uri = imageResource.getImage(position);
        bitmap = imageResource.getBitmapImage(getApplicationContext(),position);


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



        cropImageView.setImageResource(R.drawable.exam1);

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
        cropFreeTv = (TextView) findViewById(R.id.cropFreeTv);
        cropSquareTv = (TextView) findViewById(R.id.cropSquareTv);
        cropHsquareTv = (TextView) findViewById(R.id.cropHsquareTv);
        cropVsquareTv = (TextView) findViewById(R.id.cropVsquareTv);

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

                Bitmap cropped = cropImageView.getCroppedImage();
                Log.d("BBB", cropped.toString());
                writeActivity.setWriteImageBitmab(cropped);
                selectBackActivity.finish();
                finish();

            }
        });
        freeCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(false);
                buttonWhiteChange();
                cropFreeTv.setTextColor(Color.parseColor("#fc7374"));
                freeCropImageBtn.setImageResource(R.drawable.cropfield_press);
            }
        });
        squareCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(1, 1);
                buttonWhiteChange();
                cropSquareTv.setTextColor(Color.parseColor("#fc7374"));
                squareCropImageBtn.setImageResource(R.drawable.square_press);
            }
        });
        hQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(4, 3);
                buttonWhiteChange();
                cropHsquareTv.setTextColor(Color.parseColor("#fc7374"));
                hQuadrangleCropImageBtn.setImageResource(R.drawable.quadrangle_horizontal_press);
            }
        });
        vQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(3, 4);
                buttonWhiteChange();
                cropVsquareTv.setTextColor(Color.parseColor("#fc7374"));
                vQuadrangleCropImageBtn.setImageResource(R.drawable.quadrangle_vertical_press);
            }
        });
    }
    private void buttonWhiteChange(){
        cropSquareTv.setTextColor(Color.parseColor("#ffffff"));
        cropVsquareTv.setTextColor(Color.parseColor("#ffffff"));
        cropHsquareTv.setTextColor(Color.parseColor("#ffffff"));
        cropFreeTv.setTextColor(Color.parseColor("#ffffff"));

        freeCropImageBtn.setImageResource(R.drawable.cropfield);
        squareCropImageBtn.setImageResource(R.drawable.square);
        hQuadrangleCropImageBtn.setImageResource(R.drawable.quadrangle_horizontal);
        vQuadrangleCropImageBtn.setImageResource(R.drawable.quadrangle_vertical);
    }



}
