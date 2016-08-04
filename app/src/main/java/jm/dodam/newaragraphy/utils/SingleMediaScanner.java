package jm.dodam.newaragraphy.utils;

import android.content.Context;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;

import java.io.File;

/**
 * Created by kim on 2016-08-03.
 */
public class SingleMediaScanner implements MediaScannerConnectionClient {

    private MediaScannerConnection mMs;
    private File mFile;

    public SingleMediaScanner(Context context, File file) {
        mFile = file;
        mMs = new MediaScannerConnection(context, this);
        mMs.connect();
    }

    @Override
    public void onMediaScannerConnected() {
        mMs.scanFile(mFile.getAbsolutePath(), null);
    }

    @Override
    public void onScanCompleted(String s, Uri uri) {
        mMs.disconnect();
    }
}
