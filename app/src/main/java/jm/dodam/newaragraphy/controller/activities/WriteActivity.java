package jm.dodam.newaragraphy.controller.activities;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.support.annotation.RequiresPermission;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import jm.dodam.newaragraphy.CustomTextView;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.fragment.ChangeColorFragment;
import jm.dodam.newaragraphy.controller.fragment.ChangeFontFragment;
import jm.dodam.newaragraphy.controller.fragment.ChangeSizeFragment;
import jm.dodam.newaragraphy.utils.DBManager;
import jm.dodam.newaragraphy.utils.ImageResource;
import jm.dodam.newaragraphy.utils.SingleMediaScanner;

public class WriteActivity extends AppCompatActivity {

    private static final String TAG = "WriteActivity";
    private EditText editText;

    private ImageButton writeUploadBtn;
    private RelativeLayout writeLayout;
    private ImageView writeImageView;

    private CustomTextView customTextView;

    private int _xDelta = 0;
    private int _yDelta = 0;

    private String savePath = null;

    private int fontSize = 13;
    private int fontColor = -1;
    private Typeface fontStyle;
    private FragmentPagerAdapter fragmentPagerAdapter;
    private TabLayout tabs;
    public static WriteActivity mtWriteActivity;

    private LinearLayout view_sliding_content;
    private boolean isMenuShowing = false;
    private static TextView selectTextView;


    //TODO: Fragment 변수
    private ViewPager viewPager;

    private FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        setCustomActionbar();
        init();
        setHideStatusBar();

        setListener();

    }

    @Override
    public void onBackPressed() {
        if (isMenuShowing) {
            view_sliding_content.setVisibility(View.GONE);
            isMenuShowing = false;
        } else {
            super.onBackPressed();
        }
    }


    private void setWriteImage() {
        byte[] arr = getIntent().getByteArrayExtra("bgImage");
        Bitmap bm = BitmapFactory.decodeByteArray(arr, 0, arr.length);
        writeImageView.setImageBitmap(bm);

    }


    private void init() {
        mtWriteActivity = WriteActivity.this;
        writeUploadBtn = (ImageButton) findViewById(R.id.writeUploadBtn);
        writeLayout = (RelativeLayout) findViewById(R.id.writeLayout);
        writeImageView = (ImageView) findViewById(R.id.writeImageView);

        editText = (EditText) findViewById(R.id.edit_text);
        supportFragmentManager = getSupportFragmentManager();

        setWriteImage();
        fragmentInit();


    }

    private void setListener() {
        writeUploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO: 하드코딩
                String folder = "Test_Directory";
                writeLayout.setDrawingCacheEnabled(false);
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

                        Log.d("capture", "okok");

                        File file = new File(save);
                        SingleMediaScanner mScanner = new SingleMediaScanner(getApplicationContext(), file);

                        Log.d("Scanner", "okok");
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Log.e("Screen", "" + e.toString());
                }


                String text = "글씨가 아름다워지는 어플 #Aragraphy";
                File file = new File(savePath);

                List targetedShareIntents = new ArrayList<>();

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

                Intent chooser = Intent.createChooser((Intent) targetedShareIntents.remove(0), "공유하기");
                chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetedShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooser);

            }
        });
    }

    private void addNewText() {
        customTextView = new CustomTextView(WriteActivity.this);
        customTextView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        customTextView.setX((float) 100.0);
        customTextView.setY((float) 500.0);

        TextView textview = customTextView.getTextView();
        textview.setBackgroundColor(Color.TRANSPARENT);
        textview.setPadding(20, 10, 10, 10);
        // TODO: Color
        textview.setTextColor(Color.parseColor("#000000"));
        textview.setTextSize(33);
        // TODO: 하드코딩
        textview.setHint("TEXT");
        textview.setHintTextColor(Color.parseColor("#939393"));


        Bitmap bitmap;
        bitmap = Bitmap.createBitmap(80, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
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

                        //현재 클릭된 view 체크
                        for (int i = 0; i < writeLayout.getChildCount(); i++) {
                            if (writeLayout.getChildAt(i).getClass().getSimpleName().equals("CustomTextView")) {
                                CustomTextView customTextView = (CustomTextView) writeLayout.getChildAt(i);
                                customTextView.getTextView().setBackgroundColor(Color.TRANSPARENT);
                                customTextView.getLayoutMenu().setVisibility(View.GONE);
                            }
                        }
                        CustomTextView customTextView = (CustomTextView) view;
                        selectTextView = customTextView.getTextView();
                        customTextView.getTextView().setBackgroundResource(R.drawable.style_textview_background);
                        customTextView.getLayoutMenu().setVisibility(View.VISIBLE);
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

                writeLayout.invalidate();

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
                    addNewText();
                    editText.setVisibility(View.VISIBLE);
                    editText.setText("");
                    writeLayout.addView(customTextView);
                    editText.setFocusable(true);
                    editText.setPressed(true);
                    editText.setSelected(true);
                    editText.setEnabled(true);
                    editText.invalidate();
                    editText.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);

                    selectTextView = customTextView.getTextView();
                    editText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            customTextView.getTextView().setText(charSequence + " ");
                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                            customTextView.getTextView().setText(charSequence + " ");
                        }

                        @Override
                        public void afterTextChanged(Editable editable) {

                        }
                    });
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
//                        InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
//                        imm.showSoftInput(customTextView, InputMethodManager.SHOW_FORCED);
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

        };
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
}
