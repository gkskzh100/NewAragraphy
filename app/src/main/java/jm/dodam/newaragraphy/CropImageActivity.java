package jm.dodam.newaragraphy;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by Bong on 2016-08-03.
 */
public class CropImageActivity extends AppCompatActivity {
    ImageButton exitImageBtn, acceptImageBtn;
    private ImageView imageView, freeCropImageBtn, squareCropImageBtn, hQuadrangleCropImageBtn, vQuadrangleCropImageBtn;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cropimage);
        init();
        setListener();
        setCustomActionbar();

        Glide.with(getApplicationContext())
//                .load(images.get(position))
                .load("https://images.unsplash.com/photo-1445771832954-9c7fb748f214?dpr=1&amp;auto=compress,format&amp;crop=entropy&amp;fit=crop&amp;w=767&amp;h=512&amp;q=80&quot;")
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imageView);
    }

    private void setListener() {
    }

    private void init() {
        imageView = (ImageView)findViewById(R.id.image);
        freeCropImageBtn = (ImageView)findViewById(R.id.freeCropImageBtn);
        squareCropImageBtn = (ImageView)findViewById(R.id.squareCropImageBtn);
        hQuadrangleCropImageBtn = (ImageView)findViewById(R.id.hQuadrangleCropImageBtn);
        vQuadrangleCropImageBtn = (ImageView)findViewById(R.id.vQuadrangleCropImageBtn);
    }

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_crop,null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#3e3e3e")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView,params);

    }
}
