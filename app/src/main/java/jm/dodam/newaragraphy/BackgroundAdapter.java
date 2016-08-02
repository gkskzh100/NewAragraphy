package jm.dodam.newaragraphy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URL;
import java.util.ArrayList;

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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bg,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        Glide.with(context)
                .load(R.drawable.instagram_btn)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.rightImage);
        Glide.with(context)
                .load(R.drawable.instagram_btn)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.leftImage);
    }


    @Override
    public int getItemCount() {
        return images.size()+10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView rightImage, leftImage;
        public ViewHolder(View itemView) {
            super(itemView);
            rightImage = (ImageView) itemView.findViewById(R.id.rightImage);
            leftImage = (ImageView)itemView.findViewById(R.id.leftImage);


        }

    }


}
