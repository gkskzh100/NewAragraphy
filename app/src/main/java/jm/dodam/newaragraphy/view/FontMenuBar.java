package jm.dodam.newaragraphy.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import jm.dodam.newaragraphy.R;

/**
 * Created by IronFactory on 2016. 8. 5..
 */
public class FontMenuBar extends LinearLayout {

    public FontMenuBar(Context context) {
        super(context);
    }

    public FontMenuBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FontMenuBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_font_menu_bar, this);

    }
}
