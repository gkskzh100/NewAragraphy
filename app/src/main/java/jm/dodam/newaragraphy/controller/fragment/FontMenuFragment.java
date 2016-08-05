package jm.dodam.newaragraphy.controller.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;

import com.astuetz.PagerSlidingTabStrip;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.activities.WriteActivity;

/**
 * Created by IronFactory on 2016. 8. 5..
 */
public class FontMenuFragment extends Fragment {

    private int icons[] = {R.drawable.text_btn, R.drawable.enlarge_btn, R.drawable.color_palette_btn};

    private ViewPager viewPager;
    private ImageButton controllerBtn;
    private PagerSlidingTabStrip tabs;
    private LinearLayout layout;

    private int _yDelta = 0;



    private OnFontChange handler;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        handler = (OnFontChange) context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_font_menu_bar, container, false);
        init(view);

        controllerBtn.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                final int Y = (int) motionEvent.getRawY();

                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_DOWN :
                        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                        _yDelta = Y - layoutParams.topMargin;
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                        break;
                    case MotionEvent.ACTION_POINTER_UP:
                        break;
                    case MotionEvent.ACTION_MOVE:
                        LinearLayout.LayoutParams lParams = (LinearLayout.LayoutParams) layout.getLayoutParams();
                        float dimen = getResources().getDimension(R.dimen.write_fragment_max_height);
                        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
                        int height = dm.heightPixels;

                        if(Y < (height-100) && (height - dimen +50) < Y) {
                            lParams.topMargin = Y - _yDelta;
                            layout.setLayoutParams(lParams);
                        }


                        break;


                }

                return false;
            }
        });

        return view;
    }

    private void init(View view) {
        viewPager = (ViewPager) view.findViewById(R.id.view_font_menu_bar_pager);
        controllerBtn = (ImageButton) view.findViewById(R.id.view_font_menu_bar_controller);
        tabs = (PagerSlidingTabStrip) view.findViewById(R.id.view_font_menu_bar_tab);
        layout = (LinearLayout) view.findViewById(R.id.view_font_menu_bar_layout);

        viewPagerSetting();

    }

    private void viewPagerSetting() {
        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {


            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return ChangeFontFragment.newInstance(handler);
                    case 1:
                        return ChangeSizeFragment.newInstance(handler);
                    case 2:
                        return ChangeColorFragment.newInstance(handler);
                }

                return null;
            }

            @Override
            public int getCount() {
                return 3;
            }

            public int getPageIconResId(int position) {
                return icons[position];
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "폰트";
                    case 1:
                        return "크기";
                    case 2:
                        return "색깔";
                }
                return null;
//                Drawable image = getContext().getResources().getDrawable(icons[position]);
//                image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//                SpannableString sb = new SpannableString("   ");
//                ImageSpan imageSpan = new ImageSpan(image,ImageSpan.ALIGN_BOTTOM);
//                sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                return sb;
            }
        });
        tabs.setViewPager(viewPager);

    }


    public interface OnFontChange {
        void onFontChanged(Typeface font);

        void onColorChanged(int color);

        void onSizeChanged(String size);
    }
}
