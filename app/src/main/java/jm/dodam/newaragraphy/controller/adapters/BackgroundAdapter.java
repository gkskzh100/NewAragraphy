package jm.dodam.newaragraphy.controller.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jm.dodam.newaragraphy.R;
import jm.dodam.newaragraphy.utils.DBManager;

/**
 * Created by Bong on 2016-08-02.
 */
public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {
    ArrayList<String> images = null;
    Context context = null;


    public BackgroundAdapter(ArrayList<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public BackgroundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bg, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //이미지 넣어주기
        Glide.with(context)
                .load(images.get(position))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bgImage);

    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView bgImage;

        public ViewHolder(View itemView) {
            super(itemView);
            bgImage = (ImageView) itemView.findViewById(R.id.bgImage);


        }

    }


}
