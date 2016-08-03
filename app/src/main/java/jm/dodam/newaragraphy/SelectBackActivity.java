package jm.dodam.newaragraphy;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bong on 201d6-08-02.
 */
public class SelectBackActivity extends AppCompatActivity{
    private ImageButton selectExitImageBtn;
    private RecyclerView recyclerView;
    List<String> imageUris;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bg);

        setCustomActionbar();
        init();
//        parseImages();
        setListener();
        setListAdapter();
        setItemClick();

    }

    private void init() {
        recyclerView = (RecyclerView)findViewById(R.id.SelectRecyclerView);
        selectExitImageBtn = (ImageButton)findViewById(R.id.SelectExitImageBtn);
    }

    private void setListener() {
        selectExitImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_selectbg,null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#ffffff")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView,params);

    }
    private void setListAdapter(){

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        BackgroundAdapter myBackgroundAdapter = new BackgroundAdapter(imageUris, getApplicationContext());
        recyclerView.setAdapter(myBackgroundAdapter);


    }
    private void setItemClick(){

        recyclerView.addOnItemTouchListener(new SelectBackClickListener(getApplicationContext(), recyclerView, new SelectBackClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                startActivity(new Intent(getApplicationContext(),ChooseImageActivity.class));
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }
    private void parseImages(){

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask(getApplicationContext(),"https://unsplash.com/collections/225685/surf");
        imageUris = jsoupAsyncTask.getImageUris();
        for (int i=0;i<imageUris.size();i++){
            Log.d("image","URI : "+imageUris.get(i));
        }
        jsoupAsyncTask.execute();
    }


}
