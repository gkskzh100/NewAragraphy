package jm.dodam.newaragraphy;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import junit.framework.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteActivity extends AppCompatActivity {

    private ImageButton writeChangeImageBtn;
    private ImageButton writeAddTextBtn;
    private ImageButton writeUploadBtn;
    private RelativeLayout writeLayout;


    private EditText textView;

    private int _xDelta = 0;
    private int _yDelta = 0;

    private String savePath = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        setCustomActionbar();
        init();
        setHideStatusBar();

        setListener();



    }

    private void init() {
        writeChangeImageBtn = (ImageButton) findViewById(R.id.writeChangeImageBtn);
        writeAddTextBtn = (ImageButton) findViewById(R.id.writeAddTextBtn);
        writeUploadBtn = (ImageButton) findViewById(R.id.writeUploadBtn);
        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
    }

    private void setListener() {
        writeAddTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView = new EditText(WriteActivity.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setBackgroundColor(Color.TRANSPARENT);
                textView.setPadding(20, 10, 10, 10);
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTextSize(13);
                textView.setHint("텍스트를 입력해주세요");
                textView.setHintTextColor(Color.parseColor("#939393"));

                Bitmap bitmap;

                bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
                Canvas canvas = new Canvas(bitmap);
                textView.layout(0,80,80,100);
                textView.draw(canvas);

                textView.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View view, MotionEvent motionEvent) {

                        final int X = (int) motionEvent.getRawX();
                        final int Y = (int) motionEvent.getRawY();
                        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_DOWN:
                                RelativeLayout.LayoutParams IParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                _xDelta = X - IParams.leftMargin;
                                _yDelta = Y - IParams.topMargin;
                                break;
                            case MotionEvent.ACTION_UP:
                                view.setFocusableInTouchMode(true);
                                break;
                            case MotionEvent.ACTION_POINTER_DOWN:
                                break;
                            case MotionEvent.ACTION_POINTER_UP:
                                break;
                            case MotionEvent.ACTION_MOVE:
                                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                                layoutParams.leftMargin = X - _xDelta;
                                layoutParams.topMargin = Y - _yDelta;
                                layoutParams.rightMargin = -250;
                                layoutParams.bottomMargin = -250;
                                view.setLayoutParams(layoutParams);
                                view.setFocusableInTouchMode(false);
                                break;
                        }
//                        ((EditText) view).setTextColor(Color.YELLOW);
                        writeLayout.invalidate();

                        return false;
                    }
                });

                writeLayout.addView(textView);
            }
        });
        writeUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String folder = "Test_Directory";
                writeLayout.setDrawingCacheEnabled(false);

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date currentTime_1 = new Date();
                    String dateString = formatter.format(currentTime_1);
                    File sdCardPath = Environment.getExternalStorageDirectory();
                    File dirs = new File(Environment.getExternalStorageDirectory(), folder);

                    if(!dirs.exists()) {
                        dirs.mkdirs();
                        Log.d("CAMERA_TEST", "Directory Created");
                    }
                    writeLayout.setDrawingCacheEnabled(true);
                    Bitmap captureView = writeLayout.getDrawingCache();
                    FileOutputStream fos;
                    String save;

                    try {
                        writeLayout.refreshDrawableState();

                        save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".jpg";
                        savePath = save;
                        fos = new FileOutputStream(save);
                        captureView.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        Log.d("capture","okok");

                        File file = new File(save);
                        SingleMediaScanner mScanner = new SingleMediaScanner(getApplicationContext(),file);

                        Log.d("Scanner","okok");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("Screen", "" + e.toString());
                }


                Intent intent = new Intent(getApplicationContext(), ShareActivity.class);
                intent.putExtra("savePath",savePath);
                startActivity(intent);

                Log.d("sendSavePath", savePath);

            }
        });
        writeChangeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SelectBackActivity.class));
            }
        });
    }


    private void setCustomActionbar() {
        ActionBar actionBar = getSupportActionBar();

        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowTitleEnabled(false);

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar,null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));


        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView,params);
    }

    private void setHideStatusBar(){
        if (Build.VERSION.SDK_INT >= 19) {      //Kitkat 이상 상태바 투명하게 만들기 //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }
}
