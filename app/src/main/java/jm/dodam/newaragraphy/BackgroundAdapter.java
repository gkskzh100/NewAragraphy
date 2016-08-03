package jm.dodam.newaragraphy;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Bong on 2016-08-02.
 */
public class BackgroundAdapter extends RecyclerView.Adapter<BackgroundAdapter.ViewHolder> {
    List<String> images = null;
    Context context = null;
    public BackgroundAdapter(List<String> images, Context context) {
        this.images = images;
        this.context = context;
    }

    @Override
    public BackgroundAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_bg,parent,false);
        for (int i=0;i<images.size();i++){
            Log.d("image","URI : "+images.get(i));
        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        Glide.with(context)
//                .load(images.get(position))
                .load(R.drawable.facebook_btn)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bgImage);

    }


    @Override
    public int getItemCount() {
        return images.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView bgImage;
        public ViewHolder(View itemView) {
            super(itemView);
            bgImage = (ImageView)itemView.findViewById(R.id.bgImage);


        }

    }


}
