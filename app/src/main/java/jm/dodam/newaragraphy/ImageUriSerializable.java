package jm.dodam.newaragraphy;

import java.io.Serializable;

/**
 * Created by Bong on 2016-08-04.
 */
public class ImageUriSerializable implements Serializable {

    public String imageUri;

    public ImageUriSerializable(String imageUri) {
        this.imageUri = imageUri;
    }
}
