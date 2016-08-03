package jm.dodam.newaragraphy;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bong on 2016-08-01.
 */

public class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {

    private String htmlPageUrl;
    int count = 0;
    Context context;
    ImageResource DB;
    public ArrayList<String> imageUris = new ArrayList<String>();
    ArrayList<String> test = new ArrayList<String>();
    public JsoupAsyncTask(Context context, String htmlPageUrl) {
        this.context = context;
        this.htmlPageUrl = htmlPageUrl;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }



    protected Void doInBackground(Void... params) {
        try {

            Document doc = Jsoup.connect(htmlPageUrl).get();
            Elements tags = doc.select(".cV68d");
            imageUris = new ArrayList<String>();
            Log.d("tags","tags : "+tags);
            for (Element i : tags) {
                imageUris.add(i.attr("style").toString().substring(i.attr("style").toString().indexOf('"')+1,i.attr("style").toString().lastIndexOf('"')));
//                Log.d("images", "Image : " + imageUris.get(count));
                count++;
            }
            DB = new ImageResource(imageUris);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {


    }
    public int getImageUrisLength(){
        return imageUris.size();
    }
    public ArrayList<String> getImageUris() {

        return imageUris;
    }
}
