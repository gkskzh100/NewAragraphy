package jm.dodam.newaragraphy.controller.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.SeekBar;

import jm.dodam.newaragraphy.R;

public class ChangeSizeFragment extends Fragment {

    private SeekBar seekBar;
    private int brightnes;
    private static FontMenuFragment.OnFontChange handler;


    public static ChangeSizeFragment newInstance(FontMenuFragment.OnFontChange handler){
        ChangeSizeFragment fragment = new ChangeSizeFragment();
        ChangeSizeFragment.handler = handler;
        return fragment;
    }

    public ChangeSizeFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_size, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        seekBar = (SeekBar) view.findViewById(R.id.fragment_change_size_edit);

        seekBar.setProgress(50);

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

                try{
                    ((EditText)getActivity().getCurrentFocus()).setTextSize(i);
                }catch (ClassCastException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
/*        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //변화

                str = charSequence.toString();
                Log.d("ChangeSizeFragment", ""+str);
                if(charSequence == null || TextUtils.isEmpty(charSequence)) {
                    str="0";
                }
                try{
                    ((EditText)getActivity().getCurrentFocus()).setTextSize(Integer.valueOf(str));
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }
//                handler.onSizeChanged(str);


            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 끝
            }
       });*/



    }
}
