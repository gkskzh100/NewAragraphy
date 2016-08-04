package jm.dodam.newaragraphy.controller.fragment;


import android.content.Context;
import android.content.DialogInterface;
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

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.utils.Global;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeColorFragment extends Fragment {

    private static final String TAG = "ChangeColorFragment";
    private List<Integer> colors = new ArrayList<>();
    private GridView gridView;
    private static FontMenuFragment.OnFontChange handler;


    public static ChangeColorFragment newInstance(FontMenuFragment.OnFontChange handler) {
        ChangeColorFragment fragment = new ChangeColorFragment();
        ChangeColorFragment.handler = handler;
        return fragment;
    }


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
            return colors.size() + 2;
        }

        @Override
        public Object getItem(int i) {
            if (i < colors.size())
                return colors.get(i);
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final int position = i - 1;
            if (view == null) {
                view = inflater.inflate(R.layout.item_change_color, viewGroup, false);
            }
            ImageView colorCircle = (ImageView) view.findViewById(R.id.item_change_color_circle);
            Drawable background;
            if (i == 0) {
                // TODO 투명 이미지 입히기
                colorCircle.setBackgroundResource(R.drawable.circle);
                background = colorCircle.getBackground();
                ((GradientDrawable) background).setColor(Color.WHITE);

                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handler.onColorChanged(Global.INVISIBLE);
                    }
                });

            } else if (position == colors.size()) {
                // 색 조합
                // TODO 여러 색 입힐 수 있는 팔레트 이미지 입히기
                colorCircle.setBackgroundResource(R.drawable.circle);
                background = colorCircle.getBackground();
                ((GradientDrawable) background).setColor(Color.WHITE);

                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ColorPickerDialogBuilder.with(getActivity())
                                .setTitle(getResources().getString(R.string.color_title))
                                .initialColor(Color.YELLOW)
                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                .density(20)
                                .setPositiveButton(getResources().getString(R.string.color_submit), new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int selectColor, Integer[] integers) {
                                        handler.onColorChanged(selectColor);
                                    }
                                })
                                .setNegativeButton(getResources().getString(R.string.color_cancel), new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                    }
                                }).build().show();
                    }
                });

            } else {
                colorCircle.setBackgroundResource(R.drawable.circle);
                background = colorCircle.getBackground();
                ((GradientDrawable) background).setColor(colors.get(position));


                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        handler.onColorChanged(colors.get(position));
                    }
                });
            }
            return view;
        }
    }
}
