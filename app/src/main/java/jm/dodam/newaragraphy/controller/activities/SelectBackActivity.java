package jm.dodam.newaragraphy.controller.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import jm.dodam.newaragraphy.controller.adapters.BackgroundAdapter;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.SelectBackClickListener;

/**
 * Created by Bong on 201d6-08-02.
 */
public class SelectBackActivity extends AppCompatActivity{

    final DBManager dbManager = new DBManager(SelectBackActivity.this,"Image.db",null,1);
    private ImageButton selectExitImageBtn;
    private RecyclerView recyclerView;
    public static SelectBackActivity mySelectBackActivity;

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

        getSupportActionBar().setElevation(0);
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

        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        boolean isWifiAvail = ni.isAvailable();
        boolean isWifiConn = ni.isConnected();
        ni = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        boolean isMobileAvail = ni.isAvailable();
        boolean isMobileConn = ni.isConnected();

        if(isWifiConn==false && isMobileConn==false) {
            Snackbar.make(recyclerView, "인터넷에 연결할 수 없습니다.\n연결을 확인하세요.", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                        }
                    }).show();
        } else if (isWifiAvail==true || isMobileAvail==true) {
            //그리드뷰 설정하는곳
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new GridLayoutManager(this,2));
            BackgroundAdapter myBackgroundAdapter = new BackgroundAdapter(dbManager.getImageList(), getApplicationContext());
            recyclerView.setAdapter(myBackgroundAdapter);
        }


    }
    private void setItemClick(){

        recyclerView.addOnItemTouchListener(new SelectBackClickListener(getApplicationContext(), recyclerView, new SelectBackClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent itCropActivity = new Intent(getApplicationContext(),CropBgActivity.class);
                // TODO: 하드코딩
                //이미지의 포지션 값을 다음 화면에 보낸다.
                itCropActivity.putExtra("position",position);
                itCropActivity.putExtra("login",true);
                startActivity(itCropActivity);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }



}
