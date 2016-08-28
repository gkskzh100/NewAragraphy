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

    // WriteActivity 로 데이터 전송
    private static WriteActivity.OnFontChange handler;


    public static ChangeColorFragment newInstance(WriteActivity.OnFontChange handler) {
        ChangeColorFragment fragment = new ChangeColorFragment();
        ChangeColorFragment.handler = handler;
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
        colors.add(Color.rgb(255, 255, 255));//흰색
        colors.add(Color.rgb(109, 110, 114));//회색
        colors.add(Color.rgb(0, 0, 0));     //검정
        colors.add(Color.rgb(29, 32, 136));//남색

        colors.add(Color.rgb(222, 127, 169));//핑크
        colors.add(Color.rgb(230, 0, 18)); //빨강
        colors.add(Color.rgb(235, 97, 0)); //주황
        colors.add(Color.rgb(245, 158, 3));//귤색
        colors.add(Color.rgb(255, 241, 0));//노랑

        colors.add(Color.rgb(187, 213, 80));//라임
        colors.add(Color.rgb(0, 153, 68)); //초록
        colors.add(Color.rgb(0, 104, 183));//파랑
        colors.add(Color.rgb(96, 25, 134));//보라
        //팔레트

//        투명 흰색 회색 검정 남색
//        핑크 빨강 주황 귤색 노랑
//        라임 초록 파랑 보라 팔레트

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
            if (i == 0) {
                // 투명
                // TODO 투명 이미지 입히기
                colorCircle.setBackgroundResource(R.drawable.alpha_btn);
//                Drawable background = colorCircle.getBackground();
//                ((GradientDrawable) background).setColor(Color.WHITE);

                colorCircle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        handler.onColorChanged(Global.INVISIBLE);
                        try {

                            WriteActivity.getSelectTextView().setTextColor(Global.INVISIBLE);
                        } catch (ClassCastException | NullPointerException e) {
                            e.printStackTrace();
                        }
                    }
                });

            } else if (position == colors.size()) {
                // 색 조합
                // TODO 여러 색 입힐 수 있는 팔레트 이미지 입히기
                colorCircle.setBackgroundResource(R.drawable.pallet_btn);

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
