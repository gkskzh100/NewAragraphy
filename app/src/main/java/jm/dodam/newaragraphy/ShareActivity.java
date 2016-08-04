package jm.dodam.newaragraphy;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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

//    private void sendShare() {
//        Uri uri = Uri.fromFile(new File(savePath));
//        Intent shareIntent = new Intent();
//        shareIntent.setAction(Intent.ACTION_SEND);
//        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
//        shareIntent.setType("image/jpeg");
//        startActivity(Intent.createChooser(shareIntent,"공유하기"));
//    }

}
