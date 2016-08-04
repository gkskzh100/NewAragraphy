package jm.dodam.newaragraphy.controller.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import jm.dodam.newaragraphy.controller.adapters.BackgroundAdapter;
import jm.dodam.newaragraphy.utils.ImageResource;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.SelectBackClickListener;

/**
 * Created by Bong on 201d6-08-02.
 */
public class SelectBackActivity extends AppCompatActivity{
    private ImageButton selectExitImageBtn;
    private RecyclerView recyclerView;
    public static SelectBackActivity mySelectBackActivity;
    ArrayList<String> imageUris = new ArrayList<>();
    ImageResource imageResource;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bg);
        setCustomActionbar();
        init();

        setListener();
        setListAdapter();
        setItemClick();

    }

    private void init() {
        mySelectBackActivity = SelectBackActivity.this;
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
        BackgroundAdapter myBackgroundAdapter = new BackgroundAdapter(new ImageResource().getArrayList(), getApplicationContext());
        recyclerView.setAdapter(myBackgroundAdapter);


    }
    private void setItemClick(){

        recyclerView.addOnItemTouchListener(new SelectBackClickListener(getApplicationContext(), recyclerView, new SelectBackClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent(getApplicationContext(),CropBgActivity.class);
                // TODO: 하드코딩
                intent.putExtra("position",position);
                startActivity(intent);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }



}
