package jm.dodam.newaragraphy;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

/**
 * Created by Bong on 2016-08-26.
 */
public class ChoiceDialog extends Dialog{
    Context context;
    private LinearLayout dialogLayout_unsplash, dialogLayout_gallery, dialogLayout_cancle;
    private View.OnClickListener unsplashClikListener, galleryClickListener, cancleClickLiistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        WindowManager.LayoutParams lpWindow = new WindowManager.LayoutParams();
        lpWindow.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        lpWindow.dimAmount = 0.8f;
        getWindow().setAttributes(lpWindow);

        setContentView(R.layout.dialog_choice_getimage);

        setLayout();
        setClickListener();
    }

    private void setClickListener() {
        dialogLayout_unsplash.setOnClickListener(unsplashClikListener);
        dialogLayout_gallery.setOnClickListener(galleryClickListener);
        dialogLayout_cancle.setOnClickListener(cancleClickLiistener);
        
    }


    private void setLayout() {
        dialogLayout_unsplash = (LinearLayout)findViewById(R.id.dialogLayout_unsplash);
        dialogLayout_gallery = (LinearLayout)findViewById(R.id.dialogLayout_gallery);
        dialogLayout_cancle = (LinearLayout)findViewById(R.id.dialogLayout_cancle);
    }


    public ChoiceDialog(Context context) {
        super(context,android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
    }

    public ChoiceDialog(Context context, int themeResId) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
    }

    public ChoiceDialog(Context context, View.OnClickListener unsplashClickListener,View.OnClickListener galleryClickListener , View.OnClickListener cancleClickListener) {
        super(context, android.R.style.Theme_Translucent_NoTitleBar);
        this.context = context;
        this.unsplashClikListener = unsplashClickListener;
        this.galleryClickListener = galleryClickListener;
        this.cancleClickLiistener = cancleClickListener;
    }


}
