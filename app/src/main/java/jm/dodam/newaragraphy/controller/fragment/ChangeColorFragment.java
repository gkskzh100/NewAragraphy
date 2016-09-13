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
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.activities.WriteActivity;
import jm.dodam.newaragraphy.utils.Global;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChangeColorFragment extends Fragment {

    private static final String TAG = "ChangeColorFragment";
    private List<Integer> colors = new ArrayList<>();
    private GridView gridView;


    public static ChangeColorFragment newInstance() {
        ChangeColorFragment fragment = new ChangeColorFragment();
        return fragment;
    }


    public ChangeColorFragment() {
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

        // dummy data

        //투명
//        colors.add(Color.rgb(255, 255, 255));
        colors.add(Color.rgb(0, 0, 0));
        colors.add(Color.rgb(79, 132, 84));
        colors.add(Color.rgb(39, 100, 122));
        colors.add(Color.rgb(42, 67, 95));
        colors.add(Color.rgb(255, 151, 163));
        colors.add(Color.rgb(251, 41, 115));
        colors.add(Color.rgb(158, 55, 99));
        colors.add(Color.rgb(0, 118, 177));
        colors.add(Color.rgb(54, 193, 224));
        colors.add(Color.rgb(153, 154, 60));
        colors.add(Color.rgb(255, 215, 61));
        colors.add(Color.rgb(168, 23, 44));
        colors.add(Color.rgb(93, 70, 56));
        //팔레트

        gridView.setAdapter(new Adapter());
    }


    class Adapter extends BaseAdapter {

        private LayoutInflater inflater;

        public Adapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 15;
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
//            final int position = i;
            if (view == null) {
                view = inflater.inflate(R.layout.item_change_color, viewGroup, false);
            }
            ImageView colorCircle = (ImageView) view.findViewById(R.id.item_change_color_circle);
            if (i == 0) {
                // 흰색테두리
                colorCircle.setBackgroundResource(R.drawable.xml_border);
//                Drawable background = colorCircle.getBackground();
//                ((GradientDrawable) background).setColor(Color.WHITE);

                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        handler.onColorChanged(Global.INVISIBLE);
                        try {

                            WriteActivity.getSelectTextView().setTextColor(Color.WHITE);
                        } catch (ClassCastException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else
            if (position == 13) {
                // 색 조합
                // TODO 여러 색 입힐 수 있는 팔레트 이미지 입히기
                colorCircle.setBackgroundResource(R.drawable.color_palette);

                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        ColorPickerDialogBuilder.with(getActivity())
                                .setTitle(getResources().getString(R.string.color_title))
                                .initialColor(Color.WHITE)
                                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                                .density(20)
                                .setPositiveButton(getResources().getString(R.string.color_submit), new ColorPickerClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int selectColor, Integer[] integers) {
                                        WriteActivity.getSelectTextView().setTextColor(selectColor);
//                                        handler.onColorChanged(selectColor);
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
                // 일반 색상
                colorCircle.setBackgroundResource(R.drawable.circle);
                Drawable background = colorCircle.getBackground();
                ((GradientDrawable) background).setColor(colors.get(position));


                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        try{
                            WriteActivity.getSelectTextView().setTextColor(colors.get(position));
                        }catch (ClassCastException | NullPointerException e){
                            e.printStackTrace();
                        }
//                        handler.onColorChanged(colors.get(position));
                    }
                });
            }

            return view;
        }
    }
}
