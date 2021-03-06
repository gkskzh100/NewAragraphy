package jm.dodam.newaragraphy.controller.fragment;

import android.content.Context;
import android.graphics.Typeface;
import android.nfc.Tag;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.controller.activities.WriteActivity;

/**
 * Created by Bong on 2016-08-05.
 */
public class ChangeFontFragment extends Fragment {
    private static final String TAG = "ChangeFontFragment";
    private List<Typeface> fonts = new ArrayList<>();
    private ListView listView;


    public static ChangeFontFragment newInstance() {
        ChangeFontFragment fragment = new ChangeFontFragment();
        return fragment;
    }


    public ChangeFontFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_change_font, container, false);
        init(view);
        return view;
    }

    private void init(View view) {
        listView = (ListView) view.findViewById(R.id.fragment_change_font_list);

        listView.setAdapter(new Adapter());
        setListView();
    }

    private void setListView() {
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "senal.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "darae_hand.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "han_river.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "hans_punch.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "typo_papyrus.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "jolrida.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "makgeolli.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "mortal_heart_immortal_memory.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "ojun_ohwhoo.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "sigol.ttf"));
        fonts.add(Typeface.createFromAsset(getActivity().getAssets(), "sulam.ttf"));
    }


    class Adapter extends BaseAdapter {

        private LayoutInflater inflater;

        public Adapter() {
            inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return 11;
        }

        @Override
        public Object getItem(int i) {
            if (i <= fonts.size())
                return fonts.get(i);
            return i;
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            final int position = i;
            if (view == null) {
                view = inflater.inflate(R.layout.item_change_fonts, viewGroup, false);
            }
            TextView fontText = (TextView) view.findViewById(R.id.item_change_font);
            fontText.setTypeface(fonts.get(position));

            fontText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        WriteActivity.getSelectTextView().setTypeface(fonts.get(position));
                    } catch (ClassCastException | NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            });

            return view;
        }
    }
}
