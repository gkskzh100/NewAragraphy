package jm.dodam.newaragraphy.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bong on 2016-08-04.
 */
public class ImageResource {

    /*
    * TODO: 스태틱을 사용한 이유를 모르겠음
    * //실행될때 파싱한 이미지를 static으로 저장 (다른 액티비티에서 사용 유효)
    * */
    static ArrayList<String> arrayList = new ArrayList<>();

    public ImageResource() {
        Log.d("Bong : ",arrayList.size()+"");

    }

    public static void setArrayList(ArrayList<String> arrayList) {
        ImageResource.arrayList = arrayList;
    }
    public static String getImage(int position){
        return arrayList.get(position);
    }
    public static Bitmap getBitmapImage(Context context, int positon){
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(arrayList.get(positon)));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return bitmap;
    }


    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}
