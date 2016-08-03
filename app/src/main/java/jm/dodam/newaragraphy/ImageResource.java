package jm.dodam.newaragraphy;

import android.util.Log;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Bong on 2016-08-04.
 */
public class ImageResource {
    ArrayList<String> arrayList = new ArrayList<>();

    public ImageResource(ArrayList<String> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<String> getArrayList() {
        return arrayList;
    }
}
