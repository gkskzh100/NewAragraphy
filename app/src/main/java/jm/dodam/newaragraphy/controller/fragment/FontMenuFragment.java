package jm.dodam.newaragraphy.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.astuetz.PagerSlidingTabStrip;

import jm.dodam.newaragraphy.R;

/**
 * Created by IronFactory on 2016. 8. 5..
 */
public class FontMenuFragment extends Fragment {

    private class ViewHolder {
        private ViewPager viewPager;
        private Button controllerBtn;
        private PagerSlidingTabStrip tabs;

        public ViewHolder(View view) {
            this.viewPager = (ViewPager) view.findViewById(R.id.view_font_menu_bar_pager);
            this.controllerBtn = (Button) view.findViewById(R.id.view_font_menu_bar_controller);
            this.tabs = (PagerSlidingTabStrip) view.findViewById(R.id.view_font_menu_bar_tab);
        }
    }

    private ViewHolder viewHolder;
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
        return view;
    }

    private void init(View view) {
        viewHolder = new ViewHolder(view);
        viewPagerSetting();
    }

    private void viewPagerSetting() {
        viewHolder.viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0 :
                        return ChangeColorFragment.newInstance(handler);
                    case 1 :
                        return ChangeSizeFragment.newInstance(handler);
                }

                return null;
            }


            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return "색깔";
                    case 1:
                        return "크기";
                }
                return null;
            }
        });
        viewHolder.tabs.setViewPager(viewHolder.viewPager);

    }

    public interface OnFontChange {
        void onColorChanged(int color);
        void onSizeChanged(String size);
    }
}
