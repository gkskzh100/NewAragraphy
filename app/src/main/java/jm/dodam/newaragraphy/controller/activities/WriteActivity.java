package jm.dodam.newaragraphy.controller.activities;

import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.fragment.FontMenuFragment;
import jm.dodam.newaragraphy.utils.SingleMediaScanner;

public class WriteActivity extends AppCompatActivity implements FontMenuFragment.OnFontChange {

    private static final String TAG = "WriteActivity";
    private ImageButton writeChangeImageBtn;
    private ImageButton writeAddTextBtn;
    private ImageButton writeUploadBtn;
    private RelativeLayout writeLayout;
    private ImageView writeImageView;

    private EditText textView;

    private int _xDelta = 0;
    private int _yDelta = 0;

    private String savePath = null;

    public static WriteActivity mtWriteActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        setCustomActionbar();
        init();
        setHideStatusBar();

        setListener();

    }

    public void setWriteImageBitmap(Bitmap bitmap) {
        writeImageView.setImageBitmap(bitmap);
        writeImageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
    }


    private void init() {
        mtWriteActivity = WriteActivity.this;

        writeChangeImageBtn = (ImageButton) findViewById(R.id.writeChangeImageBtn);
        writeAddTextBtn = (ImageButton) findViewById(R.id.writeAddTextBtn);
        writeUploadBtn = (ImageButton) findViewById(R.id.writeUploadBtn);
        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
        writeImageView = (ImageView) findViewById(R.id.writeImageView);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.activity_write_fond_menu_bar, new FontMenuFragment())
                .commit();
    }

    private void setListener() {
        writeAddTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView = new EditText(WriteActivity.this);
                textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                textView.setBackgroundColor(Color.YELLOW);
                textView.setPadding(20, 10, 10, 10);
                // TODO: Color
                textView.setTextColor(Color.parseColor("#FFFFFF"));
                textView.setTextSize(13);
                // TODO: 하드코딩
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
                // TODO: 하드코딩
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


                String subject = "메시지 제목";
                String text = "메시지 내용은 \n다음줄에서..";
                File file = new File(savePath);

                List targetedShareIntents = new ArrayList<>();

                //facebook
                Intent facebookIntent = getShareIntent("com.facebook.katana", subject, text, file);
                if(facebookIntent != null)
                    targetedShareIntents.add(facebookIntent);

                //twitter
                Intent twitterIntent = getShareIntent("com.twitter.android", subject, text, file);
                if(twitterIntent != null)
                    targetedShareIntents.add(twitterIntent);

                //kakaoTalk
                Intent kakaoTalkIntent = getShareIntent("com.kakao.talk", subject, text, file);
                if(kakaoTalkIntent != null)
                    targetedShareIntents.add(kakaoTalkIntent);

                //instagram
                Intent instagramIntent = getShareIntent("com.instagram.android", subject, text, file);
                if(instagramIntent != null)
                    targetedShareIntents.add(instagramIntent);

                Intent chooser = Intent.createChooser((Intent) targetedShareIntents.remove(0),"공유하기");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS,targetedShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooser);

            }
        });
        writeChangeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SelectBackActivity.class));
            }
        });
    }

    private Intent getShareIntent(String name, String subject, String text, File file) {
        boolean found = false;

        Intent intent = new Intent(Intent.ACTION_SEND);
        MimeTypeMap type = MimeTypeMap.getSingleton();
        intent.setType(type.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(savePath)));

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        if(resInfo == null)
            return null;

        for (ResolveInfo info : resInfo) {
            if(info.activityInfo.packageName.toLowerCase().contains(name) ||
                    info.activityInfo.name.toLowerCase().contains(name)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
                intent.setPackage(info.activityInfo.packageName);
                found = true;
                break;
            }
        }
        if (found)
            return intent;

        return null;
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


    @Override
    public void onColorChanged(int color) {
        Log.d(TAG, "" + color);
    }
}
