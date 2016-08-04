package jm.dodam.newaragraphy.controller.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import jm.dodam.newaragraphy.R;

/**
 * Created by IronFactory on 2016. 8. 5..
 */
public class FontMenuFragment extends Fragment {

    private class ViewHolder {
        private ViewPager viewPager;
        private Button controllerBtn;

        public ViewHolder(View view) {
            this.viewPager = (ViewPager) view.findViewById(R.id.view_font_menu_bar_pager);
            this.controllerBtn = (Button) view.findViewById(R.id.view_font_menu_bar_controller);
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
                return ChangeColorFragment.newInstance(handler);
            }

            @Override
            public int getCount() {
                return 1;
            }
        });
    }

    public interface OnFontChange {
        void onColorChanged(int color);
    }
}
