package jm.dodam.newaragraphy.controller.activities;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.utils.Global;

/**
 * Created by Bong on 2016-09-27.
 */
public class ChoosedUserImageActivity extends Activity {

    final DBManager userImageDB = new DBManager(ChoosedUserImageActivity.this, "UserImage.db", null, Global.DB_VERSION);
    private ImageView imageView;
    private ImageButton choosedExitBtn, choosedDownloadBtn;
    private LinearLayout topLayout, bottomLayout;
    private Boolean imageVisible = false;
    private String imageUri;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choosedimage);
        init();
        setChooseImage();
    }

    private void init() {
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

        Glide.with(getApplicationContext())
                .load(imageUri)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

}

