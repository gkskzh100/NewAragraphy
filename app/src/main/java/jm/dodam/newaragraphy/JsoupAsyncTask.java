package jm.dodam.newaragraphy;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Bong on 2016-08-01.
 */
public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
    private String htmlPageUrl;

    public JsoupAsyncTask(String htmlPageUrl) {
        this.htmlPageUrl = htmlPageUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Void doInBackground(Void... params) {
        try {
            Document doc = Jsoup.connect(htmlPageUrl).get();
            Elements tags = doc.select(".cV68d");
            Log.d("tags","tags : "+tags);
            for (Element i : tags) {
                Log.d("images", "Image : " + i.attr("style"));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        Log.d("images", "good");
    }
}
