package jm.dodam.newaragraphy.controller.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.RecyclerVIewClickListener;
import jm.dodam.newaragraphy.controller.adapters.BackgroundAdapter;
import jm.dodam.newaragraphy.controller.adapters.UserImageAdapter;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.utils.Global;

/**
 * Created by Bong on 2016-09-26.
 */
public class UserImageActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private ImageButton userImageBtn;
    final DBManager userImageDB = new DBManager(UserImageActivity.this, "UserImage.db", null, Global.DB_VERSION);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userimage);
        setCustomActionbar();
        init();
        //setListAdapter();
    }

    private void init() {
        recyclerView = (RecyclerView) findViewById(R.id.UserRecyclerView);
        userImageBtn = (ImageButton) findViewById(R.id.UserExitImageBtn);



        setListener();
        setListAdapter();
    }

    private void setListener() {
        userImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar_myimage, null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);
    }

    private void setListAdapter() {
        //그리드뷰 설정하는곳
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        UserImageAdapter userImageAdapter = new UserImageAdapter(getApplicationContext(),userImageDB.getImageList());
        recyclerView.setAdapter(userImageAdapter);
        setItemClick();

    }
    private void setItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerVIewClickListener(getApplicationContext(), recyclerView, new RecyclerVIewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent chImageActivity = new Intent(getApplicationContext(),ChoosedUserImageActivity.class);

                //이미지의 포지션 값을 다음 화면에 보낸다.
                chImageActivity.putExtra("position",position);
                startActivity(chImageActivity);
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));

    }
}
