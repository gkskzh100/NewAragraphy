package jm.dodam.newaragraphy.controller.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;

import java.io.File;
import java.util.List;

import jm.dodam.newaragraphy.R;


/**
 * Created by Bong on 2016-08-02.
 */
public class ShareActivity extends Activity {
    private ImageButton shareGalleryBtn;
    private ImageButton shareKakaoBtn;
    private ImageButton shareFacebookBtn;
    private ImageButton shareTwitterBtn;
    private ImageButton shareInstagramBtn;
    private ImageButton shareCancelBtn;

    private String savePath = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);

        Intent intent = getIntent();
        savePath = intent.getStringExtra("savePath");
        Log.d("getSavePath", savePath);

        init();

//        sendShare();
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
        shareFacebookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                File file =new File(savePath);
                MimeTypeMap type = MimeTypeMap.getSingleton();
                intent.setType(type.getMimeTypeFromExtension(MimeTypeMap.getFileExtensionFromUrl(savePath)));

                intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));

                Log.d("shareFaceBook","success!");
                startActivity(intent);
            }
        });
    }

    private Intent getShareIntent(String name, String subject, String text) {
        boolean found = false;

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");

        List<ResolveInfo> resInfo = getPackageManager().queryIntentActivities(intent, 0);

        if(resInfo == null)
            return null;

        for (ResolveInfo info : resInfo) {
            if(info.activityInfo.packageName.toLowerCase().contains(name) ||
                    info.activityInfo.name.toLowerCase().contains(name)) {
                intent.putExtra(Intent.EXTRA_SUBJECT, subject);
                intent.putExtra(Intent.EXTRA_TEXT, text);
                intent.setPackage(info.activityInfo.packageName);
                found = true;
                break;
            }
        }
        if (found)
            return intent;

        return null;
    }
}
