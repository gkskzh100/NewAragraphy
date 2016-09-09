package jm.dodam.newaragraphy.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jm.dodam.newaragraphy.utils.CustomLoading;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.R;

/**
 * Created by Bong on 2016-08-03.
 */
public class CropBgActivity extends AppCompatActivity{
    /*
    * TODO 변수명에 약어를 쓰는것은 좋지 않음
    * 예 ViewController
    * BadCase: vc
    * BestCase: viewController
    * Tip: Android Studio 는 안드로이드 표준을 이식하여 만든 것이므로 경고등이나 밑줄이 보이면 가급적 수정하는 것이 바람직함.
    * */
    private TextView cropFreeTv, cropSquareTv, cropHsquareTv, cropVsquareTv;
    private ImageButton SelectAcceptImageBtn, SelectExitImageBtn;
    final DBManager dbManager = new DBManager(CropBgActivity.this,"Image.db",null,1);
    private byte[] imageArr;
    private ImageView freeCropImageBtn, squareCropImageBtn, hQuadrangleCropImageBtn, vQuadrangleCropImageBtn;

    /*
    * TODO: 특별한 이유가 없다면 private 을 의무화하는게 좋음
    * 미연의 사고를 방지하기 위함
    * */
    private CropImageView cropImageView;
    private Bitmap cropped = null;
    private Bitmap bitmap = null;
    private Uri resultUri = null;
    private back task;
    private Context context;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        context = this;
        setCustomActionbar();
        init();
        cropImage();
        getImageResource();
        setListener();
    }



    private void getImageResource(){
        CustomLoading.showLoading(context);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(getIntent().getBooleanExtra("login", true)){
                    int position = getIntent().getIntExtra("position",0);
                    task = new back();
                    task.execute(dbManager.getImageList().get(position));
                }else{
                    cropImageView.setImageUriAsync((Uri)getIntent().getExtras().get("galleryImage"));
                }

                CustomLoading.hideLoading();
            }
        },1000);
    }

    private void cropImage() {
        cropImageView.setOnSetImageUriCompleteListener(new CropImageView.OnSetImageUriCompleteListener() {
            @Override
            public void onSetImageUriComplete(CropImageView view, Uri uri, Exception error) {
                SelectAcceptImageBtn.setEnabled(true);
            }
        });
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

        getSupportActionBar().setElevation(0);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_crop, null);
        actionBar.setCustomView(mCustomView);

        /*
        * TODO Color 의 경우 res폴더의 colors.xml 을 생성하여 다루는 것을 권장
        *
        * android
        *
        */
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);

    }
    private byte[] resizeBitmap(Bitmap bm){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 100,stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
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
                cropped =  cropImageView.getCroppedImage();
                if (cropped!=null) {
                    byte[] byteArray = resizeBitmap(cropped);
                    Intent itWriteActivity = new Intent(getApplicationContext(), WriteActivity.class);
                    itWriteActivity.putExtra("bgImage", byteArray);
                    startActivity(itWriteActivity);
                    finish();
                }

            }
        });
        freeCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(false);
                buttonWhiteChange();
                cropFreeTv.setTextColor(Color.parseColor("#1B8CFF"));
                freeCropImageBtn.setImageResource(R.drawable.cropfield_free_select);
            }
        });
        squareCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(1, 1);
                buttonWhiteChange();
                cropSquareTv.setTextColor(Color.parseColor("#1B8CFF"));
                squareCropImageBtn.setImageResource(R.drawable.cropfield_1_1_select);
            }
        });
        hQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(4, 3);
                buttonWhiteChange();
                cropHsquareTv.setTextColor(Color.parseColor("#1B8CFF"));
                hQuadrangleCropImageBtn.setImageResource(R.drawable.cropfield_4_3_select);
            }
        });
        vQuadrangleCropImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cropImageView.setFixedAspectRatio(true);
                cropImageView.setAspectRatio(3, 4);
                buttonWhiteChange();
                cropVsquareTv.setTextColor(Color.parseColor("#1B8CFF"));
                vQuadrangleCropImageBtn.setImageResource(R.drawable.cropfield_3_4_select);
            }
        });
    }
    private void buttonWhiteChange(){
        cropSquareTv.setTextColor(Color.parseColor("#000000"));
        cropVsquareTv.setTextColor(Color.parseColor("#000000"));
        cropHsquareTv.setTextColor(Color.parseColor("#000000"));
        cropFreeTv.setTextColor(Color.parseColor("#000000"));

        freeCropImageBtn.setImageResource(R.drawable.cropfield_free);
        squareCropImageBtn.setImageResource(R.drawable.cropfield_1_1);
        hQuadrangleCropImageBtn.setImageResource(R.drawable.cropfield_4_3);
        vQuadrangleCropImageBtn.setImageResource(R.drawable.cropfield_3_4);
    }



    private class back extends AsyncTask<String, Integer,Bitmap> {

        @Override
        protected Bitmap doInBackground(String... urls) {

            try{

                URL myFileUrl = new URL(urls[0]);

                HttpURLConnection conn = (HttpURLConnection)myFileUrl.openConnection();

                conn.setDoInput(true);

                conn.connect();



                InputStream is = conn.getInputStream();



                bitmap = BitmapFactory.decodeStream(is);




            }catch(IOException e){

                e.printStackTrace();

            }

            return bitmap;

        }



        protected void onPostExecute(Bitmap img){


            cropImageView.setImageBitmap(img);
        }

    }


}
