package jm.dodam.newaragraphy.controller.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.renderscript.Allocation;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.theartofdev.edmodo.cropper.CropImage;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

import jm.dodam.newaragraphy.ChoiceDialog;
import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.utils.DBManager;

public class MainActivity extends Activity {

    final DBManager dbManager = new DBManager(MainActivity.this, "Image.db", null, 1);
    private static final int MY_PERMISSION_REQUEST_STORAGE = 0;
    private static final String TAG = "checkPermission";

    private ImageView mainExImageView;
    private ImageButton mainWriteImgBtn;
    private ChoiceDialog mDialog;
    private Uri mCropImageUri;
    private Bitmap bitmap;
    private ImageView mainChangeImage;
    private ImageButton mainRightButton;
    private boolean bitmapRatio = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        startActivity(new Intent(this, SplashActivity.class));

        init();
        checkPermission();
        parseImage();
        loadHtml();


    }


    private void init() {
        mainExImageView = (ImageView) findViewById(R.id.mainExImageView);
        mainWriteImgBtn = (ImageButton) findViewById(R.id.mainWriteImgBtn);
        mainChangeImage = (ImageView) findViewById(R.id.mainChangeImage);

        // TODO: 2016. 9. 18. 하영, 이 버튼은 사진 랜덤으로 바꾸는거임
        mainRightButton = (ImageButton) findViewById(R.id.mainRightButton);

        //사진 받아오는 부분
        bitmap = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.test3);
        int height = bitmap.getHeight();
        int width = bitmap.getWidth();
        Log.d(TAG, "height : " + height + ", width : " + width);
        if(height == width) {
            bitmapRatio = true;
        }

        setListener();
    }

    private void setListener() {
        mainWriteImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDialog = new ChoiceDialog(MainActivity.this, unsplashClickListener, galleryClickListener, cancleClickListener);
                mDialog.show();
            }
        });

        Bitmap changeBitmap = blur(getApplicationContext(), bitmap, 25);

        mainExImageView.setColorFilter(Color.argb(30,0,0,0));
        mainExImageView.setImageBitmap(changeBitmap);

        if(bitmapRatio){
            Bitmap changeRoundBitmap = setRoundCorner(bitmap, 70);
            mainChangeImage.setImageBitmap(changeRoundBitmap);
        } else if(!bitmapRatio){
            mainChangeImage.setImageBitmap(bitmap);
        }



    }

    public static Bitmap blur(Context ct, Bitmap sentBitmap, int radius) {

        if (Build.VERSION.SDK_INT > 16) {
            Bitmap bitmap = sentBitmap.copy(sentBitmap.getConfig(), true);

            final RenderScript rs = RenderScript.create(ct);
            final Allocation input = Allocation.createFromBitmap(rs, sentBitmap, Allocation.MipmapControl.MIPMAP_NONE,
                    Allocation.USAGE_SCRIPT);
            final Allocation output = Allocation.createTyped(rs, input.getType());
            final ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, android.renderscript.Element.U8_4(rs));
            script.setRadius(radius);
            script.setInput(input);
            script.forEach(output);
            output.copyTo(bitmap);
            return bitmap;
        }
        return sentBitmap;
    }

    public static Bitmap setRoundCorner(Bitmap bitmap, int pixel) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        paint.setColor(color);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, pixel, pixel, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    private View.OnClickListener unsplashClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            startActivity(new Intent(MainActivity.this, SelectBackActivity.class));
            mDialog.onBackPressed();
        }
    };
    private View.OnClickListener galleryClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View view) {

            CropImage.startPickImageActivity(MainActivity.this);

            mDialog.onBackPressed();

        }
    };
    private View.OnClickListener cancleClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mDialog.onBackPressed();
        }
    };

    @Override
    @SuppressLint("NewApi")
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // handle result of pick image chooser
        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri imageUri = CropImage.getPickImageResultUri(this, data);

            // For API >= 23 we need to check specifically that we have permissions to read external storage.
            if (CropImage.isReadExternalStoragePermissionsRequired(this, imageUri)) {
                // request permissions and handle the result in onRequestPermissionsResult()
                mCropImageUri = imageUri;
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},   CropImage.PICK_IMAGE_PERMISSIONS_REQUEST_CODE);
            } else {
                // no permissions required or already grunted, can start crop image activity
                Log.d("abccd",imageUri.toString());
                Intent itCropActivity = new Intent(getApplicationContext(),CropBgActivity.class);
                itCropActivity.putExtra("galleryImage",imageUri);
                itCropActivity.putExtra("login",false);
                startActivity(itCropActivity);
            }
        }
    }
    

    @TargetApi(Build.VERSION_CODES.M)
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    Toast.makeText(this, "Read/WRite external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);
            } else {
                writeFile();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    writeFile();
                } else {
                    Log.d(TAG, "Permission always deny");
                }
                break;
        }
    }

    private void writeFile() {
        /*
        * TODO: "temp.txt" 처럼 하드코딩은 지양하는 것이 좋음
        * utils 에 Global 클래스를 만들어 둠 그곳에
        * public static final String tempPath = "temp.txt";
        * 이런식으로 선언해두고 가져다 쓰는 것이 안전함.
        * 적어도 "tepm.txt" 이런식으로 오타를 낼 일은 없음
        * 추후에 파일 경로가 바뀌어도 Global 의 변수만 바꿔주면 되므로 유지보수에 유리함.
        * */
        File file = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + "temp.txt");
        try {
            Log.d(TAG, "create new File : " + file.createNewFile());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void parseImage() {

        if (dbManager.PrintData() == "") {
            JsoupParseAsyncTask jsoupAsyncTask;
            jsoupAsyncTask = new JsoupParseAsyncTask(getApplicationContext());
            jsoupAsyncTask.execute();
        }
    }

    public class JsoupParseAsyncTask extends AsyncTask<Void, Void, Void> {

        private String htmlPageUrl = "https://unsplash.com/collections/225685/surf";
        int count = 0;
        Context context;
        ArrayList<String> imageUris = new ArrayList<String>();

        public JsoupParseAsyncTask(Context context) {
            this.context = context;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... params) {
            try {

                Document doc = Jsoup.connect(htmlPageUrl).timeout(0).get();
                Elements tags = doc.select(".cV68d");
                imageUris = new ArrayList<String>();

                for (Element i : tags) {
                    imageUris.add(i.attr("style").toString().substring(i.attr("style").toString()
                            .indexOf('"') + 1, i.attr("style").toString().lastIndexOf('"')));
                    dbManager.insert("insert into IMAGES values(null, '" + imageUris.get(count) + "'" + ");");
                    count++;

                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


        }

    }
    Handler handler = new Handler();
    private void loadHtml(){
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuffer sb = new StringBuffer();
                try{
                    URL url = new URL("http://bhy98528.cafe24.com/img/imgview.php");
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    if (conn != null){
                        conn.setConnectTimeout(2000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode()==HttpURLConnection.HTTP_OK){
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(),"euc-kr"));
                            while (true){
                                String line = br.readLine();
                                if (line==null)break;
                                sb.append(line+"\n");
                            }
                            br.close();
                        }
                        conn.disconnect();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                String str = "http://bhy98528.cafe24.com/"+sb.toString().substring(sb.toString().indexOf("e")+4,sb.toString().lastIndexOf("}")-1);
                                str = str.replaceAll("\\\\","");
                                Log.d("bbb",str);
                                Glide.with(getApplicationContext())
                                        .load(Uri.parse(str))
                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                                        .into(mainChangeImage);


                            }
                        });
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        t.start();
    }


}
