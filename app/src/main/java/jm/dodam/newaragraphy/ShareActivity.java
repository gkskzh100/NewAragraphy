package jm.dodam.newaragraphy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Bong on 2016-08-02.
 */
public class ShareActivity extends Activity{
    private ImageButton shareGalleryBtn;
    private ImageButton shareKakaoBtn;
    private ImageButton shareFacebookBtn;
    private ImageButton shareTwitterBtn;
    private ImageButton shareInstagramBtn;
    private ImageButton shareCancelBtn;

    Image mImagePath ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        init();
    }

    private void init() {
        shareCancelBtn = (ImageButton) findViewById(R.id.shareCancelBtn);
        shareGalleryBtn = (ImageButton) findViewById(R.id.shareGalleryBtn);
        shareKakaoBtn = (ImageButton) findViewById(R.id.shareKakaoBtn);
        shareFacebookBtn = (ImageButton) findViewById(R.id.shareFacebookBtn);
        shareTwitterBtn = (ImageButton) findViewById(R.id.shareTwitterBtn);
        shareInstagramBtn = (ImageButton) findViewById(R.id.shareInstagramBtn);

        setListener();
    }

    private void setListener() {
        shareCancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void sendFacebookShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/*");

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);
        if(resInfo.isEmpty()) {
            return;
        }

        List<Intent> shareIntentList = new ArrayList<Intent>();

        for (ResolveInfo info : resInfo) {
            Intent shareIntent = (Intent) intent.clone();

            if(info.activityInfo.packageName.toLowerCase().equals("com.facebook.katana")) {
                //facebook
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "#Aragraphy");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file:///"+mImagePath));
            }
        }

//        http://gogorchg.tistory.com/entry/Android-SNS%EB%A1%9C-%EB%8D%B0%EC%9D%B4%ED%84%B0-%EA%B3%B5%EC%9C%A0%ED%95%98%EA%B8%B0
    }

}
