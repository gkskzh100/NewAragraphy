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
    ArrayList<String> images = null;
    Context context = null;
    public BackgroundAdapter(ArrayList<String> images, Context context) {
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
                .load("https://images.unsplash.com/placeholder-avatars/extra-large.jpg?ixlib=rb-0.3.5&amp;q=80&amp;fm=jpg&amp;crop=faces&amp;fit=crop&amp;h=32&amp;w=32&amp;s=46caf91cf1f90b8b5ab6621512f102a8")
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.bgImage);

    }


    @Override
    public int getItemCount() {
        return images.size()+10;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView bgImage;
        public ViewHolder(View itemView) {
            super(itemView);
            bgImage = (ImageView)itemView.findViewById(R.id.bgImage);


        }

    }


}
