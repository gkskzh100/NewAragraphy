package jm.dodam.newaragraphy.controller.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jm.dodam.newaragraphy.CustomTextView;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.fragment.ChangeColorFragment;
import jm.dodam.newaragraphy.controller.fragment.ChangeFontFragment;
import jm.dodam.newaragraphy.controller.fragment.ChangeSizeFragment;
import jm.dodam.newaragraphy.utils.CustomLoading;
import jm.dodam.newaragraphy.utils.SingleMediaScanner;

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "WriteActivity";
    private TextView editText;

    private ImageButton writeUploadBtn;
    private RelativeLayout writeLayout;
    private ImageView writeImageView;
    private RelativeLayout writeCaptureLayout;

    private CustomTextView customTextView;

    private int _xDelta = 0;
    private int _yDelta = 0;

    private String savePath = null;

    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabs;
    public static WriteActivity mtWriteActivity;

    private LinearLayout view_sliding_content;
    private boolean isMenuShowing = false;
    private static TextView selectTextView;

    //TODO: Fragment 변수
    public static ViewPager viewPager;

    private FragmentManager supportFragmentManager;

    private Context context;
    private boolean saveBool = false;
    private int bitmapHeight = 0;
    private int bitmapWidth = 0;

    private final long FINISH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    private boolean keyboardCheck = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);
        context = this;
        setCustomActionbar();
        init();
        setHideStatusBar();
        setListener();

        writeLayout.getViewTreeObserver()
                .addOnGlobalLayoutListener(
                        new ViewTreeObserver.OnGlobalLayoutListener() {

                            @Override
                            public void onGlobalLayout() {

                                Rect rect = new Rect();
                                writeLayout.getWindowVisibleDisplayFrame(rect);
                                int screenHeight = writeLayout.getRootView().getHeight();

                                int keypadHeight = screenHeight - rect.bottom;

                                Log.d(TAG, "keypadHeight = " + keypadHeight);

                                if (keypadHeight > screenHeight * 0.15) {
                                    // keyboard is opened
                                    keyboardCheck = true;
                                    Log.d(TAG, keyboardCheck+"");
                                    if(viewPager.getCurrentItem()==0){
                                        view_sliding_content.setVisibility(View.VISIBLE);
                                        editText.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    // keyboard is closed
                                    keyboardCheck = false;
                                    Log.d(TAG, keyboardCheck+"");
                                    if(viewPager.getCurrentItem()==0){
                                        view_sliding_content.setVisibility(View.GONE);
                                        editText.setVisibility(View.GONE);
                                        isMenuShowing = false;
                                    }
                                }
                            }
                        });
    }

    @Override
    public void onBackPressed() {
        long tempTime = System.currentTimeMillis();
        long intervalTime = tempTime - backPressedTime;
        if (isMenuShowing) {
            view_sliding_content.setVisibility(View.GONE);
            editText.setVisibility(View.GONE);
            viewPager.setCurrentItem(1);
            isMenuShowing = false;
        }
        if(0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
            alertDialogCreate();
        } else if (view_sliding_content.getVisibility()==View.GONE){
            backPressedTime = tempTime;
        }
    }


    private void setWriteImage() {
        byte[] arr = getIntent().getByteArrayExtra("bgImage");
        Bitmap bm = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        bitmapWidth = bm.getWidth();
        bitmapHeight = bm.getHeight();

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        float screenWidth = dm.widthPixels;

        float ratio = screenWidth/bitmapWidth;

        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) writeCaptureLayout.getLayoutParams();
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
        layoutParams.height = (int) (bitmapHeight*ratio);
        writeCaptureLayout.setLayoutParams(layoutParams);

        writeImageView.setImageBitmap(bm);
    }


    private void init() {
        mtWriteActivity = WriteActivity.this;
        writeUploadBtn = (ImageButton) findViewById(R.id.writeUploadBtn);
        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
        writeImageView = (ImageView) findViewById(R.id.writeImageView);
        writeCaptureLayout = (RelativeLayout) findViewById(R.id.writeCaptureLayout);

        editText = (EditText) findViewById(R.id.edit_text);
        supportFragmentManager = getSupportFragmentManager();

        writeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                findSelectView();
            }
        });
        setWriteImage();
        fragmentInit();
    }

    private void setListener() {
        writeUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                findSelectView();
                // TODO: 하드코딩
                if (!saveBool) {
                    CustomLoading.showLoading(context);
                    Log.d("loading","loading...");
                }

                String folder = "Test_Directory";
                writeCaptureLayout.setDrawingCacheEnabled(false);
                if (customTextView != null) {
                    customTextView.setFocusable(false);
                }

                try {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
                    Date currentTime_1 = new Date();
                    String dateString = formatter.format(currentTime_1);
                    File sdCardPath = Environment.getExternalStorageDirectory();
                    File dirs = new File(Environment.getExternalStorageDirectory(), folder);

                    if (!dirs.exists()) {
                        dirs.mkdirs();
                        Log.d("CAMERA_TEST", "Directory Created");
                    }
                    writeCaptureLayout.setDrawingCacheEnabled(true);

                    Bitmap captureView = writeCaptureLayout.getDrawingCache();
                    FileOutputStream fos;
                    String save;

                    try {
                        writeCaptureLayout.refreshDrawableState();

                        save = sdCardPath.getPath() + "/" + folder + "/" + dateString + ".png";
                        savePath = save;
                        fos = new FileOutputStream(save);
                        captureView.compress(Bitmap.CompressFormat.PNG, 100, fos);

                        File file = new File(save);
                        SingleMediaScanner mScanner = new SingleMediaScanner(getApplicationContext(), file);

                        saveBool = true;
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("Screen", "" + e.toString());
                }


                String text = "글씨가 아름다워지는 어플 #Aragraphy";
                File file = new File(savePath);

                final List targetedShareIntents = new ArrayList<>();

                //facebook
                Intent facebookIntent = getShareIntent("com.facebook.katana", text, file);
                if (facebookIntent != null)
                    targetedShareIntents.add(facebookIntent);

                //twitter
                Intent twitterIntent = getShareIntent("com.twitter.android", text, file);
                if (twitterIntent != null)
                    targetedShareIntents.add(twitterIntent);

                //kakaoTalk
                Intent kakaoTalkIntent = getShareIntent("com.kakao.talk", text, file);
                if (kakaoTalkIntent != null)
                    targetedShareIntents.add(kakaoTalkIntent);

                //instagram
                Intent instagramIntent = getShareIntent("com.instagram.android", text, file);
                if (instagramIntent != null)
                    targetedShareIntents.add(instagramIntent);

                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (saveBool) {
                            Intent chooser = Intent.createChooser((Intent) targetedShareIntents.remove(0), "공유하기");
                            chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                            startActivity(chooser);
                            CustomLoading.hideLoading();
                            saveBool = false;
                            Toast.makeText(getApplicationContext(), "저장이 되었습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },1000);
            }
        });
    }

    private void addNewText() {
        customTextView = new CustomTextView(WriteActivity.this);
        customTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        customTextView.setX((float) 100.0);
        customTextView.setY((float) 100.0);

        TextView textview = customTextView.getTextView();
        textview.setBackgroundColor(Color.TRANSPARENT);
        textview.setPadding(20, 10, 10, 10);
        // TODO: Color
        textview.setTextColor(Color.parseColor("#000000"));
        textview.setTextSize(33);
        // TODO: 하드코딩
        textview.setHint("TEXT");
        textview.setHintTextColor(Color.parseColor("#939393"));

        findSelectView();

        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        selectTextView = customTextView.getTextView();
        customTextView.layout(0, 80, 80, 100);
        customTextView.draw(canvas);
        customTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int X = (int) motionEvent.getRawX();
                final int Y = (int) motionEvent.getRawY();
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN:
                        RelativeLayout.LayoutParams IParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
                        _xDelta = X - IParams.leftMargin;
                        _yDelta = Y - IParams.topMargin;

                        findSelectView();

                        CustomTextView customTextView = (CustomTextView) view;
                        selectTextView = customTextView.getTextView();
                        customTextView.getTextView().setBackgroundResource(R.drawable.style_textview_background);
                        customTextView.getLayoutMenu().setVisibility(View.VISIBLE);
                        editText.setText(customTextView.getTextView().getText().toString());
                        setTextChangedListener();
                        break;
                    case MotionEvent.ACTION_UP:
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
                        break;
                }

                writeCaptureLayout.invalidate();

                return true;
            }
        });


    }

    private Intent getShareIntent(String name, String text, File file) {
        boolean found = false;

        Intent intent = new Intent(Intent.ACTION_SEND);
        MimeTypeMap type = MimeTypeMap.getSingleton();
        intent.setType(type.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(savePath)));

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        if (resInfo == null)
            return null;

        for (ResolveInfo info : resInfo) {
            if (info.activityInfo.packageName.toLowerCase().contains(name) ||
                    info.activityInfo.name.toLowerCase().contains(name)) {
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

        View mCustomView = LayoutInflater.from(this).inflate(R.layout.layout_actionbar, null);
        actionBar.setCustomView(mCustomView);

        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#00000000")));

        ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT);
        actionBar.setCustomView(mCustomView, params);
    }

    private void setHideStatusBar() {
        if (Build.VERSION.SDK_INT >= 19) {      //Kitkat 이상 상태바 투명하게 만들기 //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    private void fragmentInit() {
        viewPager = (ViewPager) findViewById(R.id.view_font_menu_bar_pager);
        tabs = (TabLayout) findViewById(R.id.view_font_menu_bar_tab);
        view_sliding_content = (LinearLayout) findViewById(R.id.view_sliding_content);
        viewPagerSetting();

    }

    private void viewPagerSetting() {
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isMenuShowing) {
                    view_sliding_content.setVisibility(View.VISIBLE);
                    isMenuShowing = true;
                }
                if (position == 0) {
                    if (editText.getVisibility() == View.GONE) {
                        addNewText();
                        editText.setVisibility(View.VISIBLE);
                        editText.setText("");
                        writeCaptureLayout.addView(customTextView);
                        editText.setFocusable(true);
                        editText.setPressed(true);
                        editText.setSelected(true);
                        editText.setEnabled(true);
                        editText.invalidate();
                        editText.requestFocus();
                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                        setTextChangedListener();
                    } else {
                        Log.d(TAG, "asdfasdfasdfasdf");
                    }
                } else {
                    editText.setVisibility(View.GONE);
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        fragmentPagerAdapter = new FragmentPagerAdapter(supportFragmentManager) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new Fragment();
                    case 1:
                        return new ChangeFontFragment();
                    case 2:
                        return new ChangeSizeFragment().newInstance(mtWriteActivity);
                    case 3:
                        return new ChangeColorFragment();
                }

                return null;
            }

            @Override
            public int getCount() {
                return 4;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return null;
            }

        }

        ;
        viewPager.setAdapter(fragmentPagerAdapter);
        viewPager.setCurrentItem(1);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(0).setCustomView(R.layout.add_text_btn);

        tabs.getTabAt(1).setCustomView(R.layout.change_font_btn);

        tabs.getTabAt(2).setCustomView(R.layout.change_size_btn);

        tabs.getTabAt(3).setCustomView(R.layout.change_color_btn);

    }

    public static TextView getSelectTextView() {
        return selectTextView;
    }

    public void setModify(View view) {

        if (editText.getVisibility() == View.GONE | view_sliding_content.getVisibility() == View.GONE) {
            editText.setVisibility(View.VISIBLE);
            view_sliding_content.setVisibility(View.VISIBLE);
            InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
            editText.setText(selectTextView.getText().toString());
            setTextChangedListener();
        } else if (editText.getVisibility() == View.VISIBLE) {
            editText.setText(selectTextView.getText().toString());
            setTextChangedListener();
        }
        viewPager.setCurrentItem(0);
    }

    private void findSelectView() {
        //현재 클릭된 view 체크
        for (int i = 0; i < writeCaptureLayout.getChildCount(); i++) {
            if (writeCaptureLayout.getChildAt(i).getClass().getSimpleName().equals("CustomTextView")) {
                CustomTextView customTextView = (CustomTextView) writeCaptureLayout.getChildAt(i);
                customTextView.getTextView().setBackgroundColor(Color.TRANSPARENT);
                customTextView.getLayoutMenu().setVisibility(View.GONE);
            }
        }
    }

    private void setTextChangedListener() {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectTextView.setText(charSequence + "");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                selectTextView.setText(charSequence + "");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void alertDialogCreate() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage("작업실을 나가겠습니까?")
                .setCancelable(false)
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        WriteActivity.this.finish();
                    }
                }) .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
}