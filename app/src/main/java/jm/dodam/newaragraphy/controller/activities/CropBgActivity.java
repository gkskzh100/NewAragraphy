package jm.dodam.newaragraphy.controller.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.utils.ImageResource;
import jm.dodam.newaragraphy.R;

/**
 * Created by Bong on 2016-08-03.
 */
public class CropBgActivity extends AppCompatActivity {
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
    /*
    * TODO: Activity 를 멤버변수로 선언하는 것은 객체지향을 해치는 행위임으로 최대한 기피 해야함
    * Activity Stack 하단의 Activity 를 종료하려 한다면 Flag 를 사용하는 것을 권장함
    * */

    private ImageView freeCropImageBtn, squareCropImageBtn, hQuadrangleCropImageBtn, vQuadrangleCropImageBtn;

    /*
    * TODO: 특별한 이유가 없다면 private 을 의무화하는게 좋음
    * 미연의 사고를 방지하기 위함
    * */
    private CropImageView cropImageView;
    private Bitmap cropped = null;
    private Bitmap bitmap = null;
    back task;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        getImageResource();
        setCustomActionbar();
        init();
        cropImage();
        setListener();

    }


    private void getImageResource(){
        int position = getIntent().getIntExtra("position",0);
        task = new back();
        task.execute(dbManager.getImageList().get(position));



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

        getSupportActionBar().setElevation(0);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_crop, null);
        actionBar.setCustomView(mCustomView);

        /*
        * TODO Color 의 경우 res폴더의 colors.xml 을 생성하여 다루는 것을 권장
        * */
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


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

                cropped = cropImageView.getCroppedImage();
                Log.d("asdfgn",cropped.toString());
                Intent intentWrite = new Intent(getApplicationContext(),WriteActivity.class);
                intentWrite.putExtra("bgImage",cropped);

               // writeActivity.setWriteImageBitmap(cropped);
                startActivity(new Intent(getApplicationContext(),WriteActivity.class));
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
