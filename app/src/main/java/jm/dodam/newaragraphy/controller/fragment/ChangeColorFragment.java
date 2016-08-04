package jm.dodam.newaragraphy.controller.fragment;


import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import jm.dodam.newaragraphy.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeColorFragment extends Fragment {

    private static final String TAG = "ChangeColorFragment";
    private List<Integer> colors = new ArrayList<>();
    private GridView gridView;


    public ChangeColorFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_color, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        gridView = (GridView) view.findViewById(R.id.fragment_change_color_grid);

        colors.add(Color.rgb(255, 255, 255));
        colors.add(Color.rgb(100, 210, 230));
        colors.add(Color.rgb(110, 200, 20));
        colors.add(Color.rgb(120, 190, 100));
        colors.add(Color.rgb(130, 180, 190));
        colors.add(Color.rgb(140, 170, 180));
        colors.add(Color.rgb(150, 160, 170));
        colors.add(Color.rgb(160, 150, 160));
        colors.add(Color.rgb(170, 140, 150));
        colors.add(Color.rgb(180, 130, 140));
        colors.add(Color.rgb(190, 120, 130));
        colors.add(Color.rgb(0, 0, 0));

        gridView.setAdapter(new Adapter());
    }


    class Adapter extends BaseAdapter {

        private LayoutInflater inflater;

        public Adapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return colors.size();
        }

        @Override
        public Object getItem(int i) {
            return colors.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            if (view == null) {
                view = inflater.inflate(R.layout.item_change_color, viewGroup, false);
            }
            ImageView colorCircle = (ImageView) view.findViewById(R.id.item_change_color_circle);
            Drawable background = colorCircle.getBackground();
            if (background instanceof GradientDrawable) {
                // 일반 색상일 때
                ((GradientDrawable) background).setColor(colors.get(i));
            } else {
                // 투명 or 상세 색상일 때
                
            }
            return view;
        }
    }
}
