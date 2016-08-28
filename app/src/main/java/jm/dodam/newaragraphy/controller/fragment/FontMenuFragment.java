//package jm.dodam.newaragraphy.controller.fragment;
//
//import android.content.Context;
//import android.graphics.Typeface;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.design.widget.TabLayout;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.support.v4.view.ViewPager;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.MotionEvent;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.inputmethod.InputMethodManager;
//import android.widget.SlidingDrawer;
//
//import jm.dodam.newaragraphy.R;
//
///**
// * Created by IronFactory on 2016. 8. 5..
// */
//public class FontMenuFragment extends Fragment {
//
//    private ViewPager viewPager;
//    private TabLayout tabs;
//    private SlidingDrawer slidingDrawer;
//
//    private OnFontChange handler;
//
//
//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//
//        handler = (OnFontChange) context;
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_font_menu_bar, container, false);
//        init(view);
//
//        tabs.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                Log.d("dd", "gggg");
//                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
//                    Log.d("dd", "move");
//                    slidingDrawer.unlock();
//                }
//                return false;
//            }
//        });
//
//        return view;
//    }
//
//    private void init(View view) {
//        viewPager = (ViewPager) view.findViewById(R.id.view_font_menu_bar_pager);
//        tabs = (TabLayout) view.findViewById(R.id.view_font_menu_bar_tab);
//        slidingDrawer = (SlidingDrawer) view.findViewById(R.id.view_sliding_drawer);
//
//
//        slidingDrawer.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener() {
//            @Override
//            public void onDrawerOpened() {
//                slidingDrawer.lock();
//                Log.d("dd", "open");
//            }
//        });
//
//        slidingDrawer.getHandle().setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                slidingDrawer.unlock();
//                Log.d("dd", "drag");
//                return false;
//            }
//        });
//
//        viewPagerSetting();
//
//    }
//
//
//    private void viewPagerSetting() {
//        viewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
//
//
//            @Override
//            public Fragment getItem(int position) {
//                switch (position) {
//                    case 0:
////                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
////                        imm.showSoftInput(textView, InputMethodManager.SHOW_FORCED);
//                        break;
//                    case 1:
//                        return ChangeFontFragment.newInstance(handler);
//                    case 2:
//                        return ChangeSizeFragment.newInstance(handler);
//                    case 3:
//                        return ChangeColorFragment.newInstance(handler);
//                }
//
//                return null;
//            }
//
//            @Override
//            public int getCount() {
//                return 4;
//            }
//
//            @Override
//            public CharSequence getPageTitle(int position) {
////                switch (position) {
////                    case 0:
////                        return "폰트";
////                    case 1:
////                        return "크기";
////                    case 2:
////                        return "색깔";
////                }
//                return null;
//            }
//        });
//        tabs.setupWithViewPager(viewPager);
//        tabs.getTabAt(0).setIcon(R.drawable.add_text_btn);
//        tabs.getTabAt(1).setIcon(R.drawable.change_font_btn);
//        tabs.getTabAt(2).setIcon(R.drawable.change_size_btn);
//        tabs.getTabAt(3).setIcon(R.drawable.change_color_btn);
//    }
//
//
//    public interface OnFontChange {
//        void onFontChanged(Typeface font);
//
//        void onColorChanged(int color);
//
//        void onSizeChanged(String size);
//
//        void onAttach(Context context);
//
//        @Nullable
//        View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState);
//    }
//}
