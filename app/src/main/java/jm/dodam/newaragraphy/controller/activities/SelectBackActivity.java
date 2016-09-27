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
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import jm.dodam.newaragraphy.controller.adapters.BackgroundAdapter;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.RecyclerVIewClickListener;
import jm.dodam.newaragraphy.utils.Global;

/**
 * Created by Bong on 201d6-08-02.
 */
public class SelectBackActivity extends AppCompatActivity{

    private ImageButton selectExitImageBtn;
    private RecyclerView recyclerView;
    public static SelectBackActivity mySelectBackActivity;
    private Context context;
    private ArrayList<String> imageUri = new ArrayList<>();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_bg);
        setCustomActionbar();
        init();

        context = this;
        setListener();
        setListAdapter();
        setItemClick();

    }
    private void setImageData(){
        for (int i=1;i<=54;i++){
            imageUri.add("http://bhy98528.cafe24.com/"+"unsplash/"+i+".jpg");
        }
    }
    private void init() {
        setImageData();
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
            BackgroundAdapter myBackgroundAdapter = new BackgroundAdapter(imageUri, getApplicationContext());
            recyclerView.setAdapter(myBackgroundAdapter);
            alertDialogCreate();
        }


    }
    private void setItemClick(){
        recyclerView.addOnItemTouchListener(new RecyclerVIewClickListener(getApplicationContext(), recyclerView, new RecyclerVIewClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent itCropActivity = new Intent(getApplicationContext(),CropBgActivity.class);

                //이미지의 포지션 값을 다음 화면에 보낸다.
                itCropActivity.putExtra("SelectImage",imageUri.get(position));
                itCropActivity.putExtra("login",true);
                startActivity(itCropActivity);
                finish();
            }

            @Override
            public void onLongItemClick(View view, int position) {

            }
        }));
    }

    private void alertDialogCreate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("Free 이미지로 제작할 시 메인화면에 올라가게 됩니다.작업을 계속하시겠습니까?")
                .setCancelable(false)
                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        SelectBackActivity.this.finish();
                    }
                }).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }



}