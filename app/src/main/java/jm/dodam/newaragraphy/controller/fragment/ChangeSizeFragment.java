package jm.dodam.newaragraphy.controller.fragment;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import jm.dodam.newaragraphy.R;

public class ChangeSizeFragment extends Fragment {

    private EditText editText;
    private String str = null;

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
        editText = (EditText) view.findViewById(R.id.fragment_change_size_edit);

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //입력 전
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                //변화

                str = charSequence.toString();
                Log.d("ChangeSizeFragment", ""+str);
                handler.onSizeChanged(str);

            }

            @Override
            public void afterTextChanged(Editable editable) {
                //입력 끝
            }
       });



    }
}
