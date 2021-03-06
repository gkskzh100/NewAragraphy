package jm.dodam.newaragraphy;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import jm.dodam.newaragraphy.controller.activities.WriteActivity;

public class CustomTextView extends LinearLayout {

    private TextView textView;
    private LinearLayout layoutMenu;
    private Button btnDel, btnModify;
    private WriteActivity context;

    private CharSequence text;

    public CustomTextView(WriteActivity context) {
        super(context);
        this.context = context;
        init();
    }

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    private void init() {
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View v = li.inflate(R.layout.custom_text_view, this, false);

        textView = (TextView) v.findViewById(R.id.custom_text_view);
        layoutMenu = (LinearLayout) v.findViewById(R.id.layout_custom_menu);
        btnDel = (Button) v.findViewById(R.id.btn_del);
        btnModify = (Button) v.findViewById(R.id.btn_modify);

        btnDel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                removeAllViews();
                context.viewPager.setCurrentItem(1);
            }
        });
        btnModify.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                context.setModify(view);
            }
        });

        addView(v);
    }


    public TextView getTextView() {
        return textView;
    }

    public LinearLayout getLayoutMenu() {
        return layoutMenu;
    }

    public Button getBtnDel() {
        return btnDel;
    }

    public Button getBtnModify() {
        return btnModify;
    }

    public CharSequence getText() {
        text = textView.getText();
        return text;
    }
}
