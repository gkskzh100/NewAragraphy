package jm.dodam.newaragraphy;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by Bong on 201d6-08-02.
 */
public class SelectBackActivity extends AppCompatActivity{
    private ImageButton selectExitImageBtn;
    private RecyclerView recyclerView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bg);

        setCustomActionbar();
        init();
        setListener();
        setListAdapter();

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
        ArrayList<String> uri = new ArrayList<String>();
        for (int i= 0 ;i<=10;i++) {
            uri.add("https://images.unsplash.com/photo-1470145318698-cb03732f5ddf?dpr=1.1500049829483032&auto=format&crop=entropy&fit=crop&w=1500&h=2250&q=80");
        }
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        BackgroundAdapter myBackgroundAdapter = new BackgroundAdapter(uri, getApplicationContext());
        recyclerView.setAdapter(myBackgroundAdapter);


    }



}
