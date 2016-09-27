package jm.dodam.newaragraphy.controller.adapters;

import android.content.Context;
import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import jm.dodam.newaragraphy.R;

/**
 * Created by Bong on 2016-09-26.
 */
public class UserImageAdapter extends RecyclerView.Adapter<UserImageAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> imageUri = new ArrayList<>();

    public UserImageAdapter(Context context, ArrayList<String> imageUri) {
        this.context = context;
        this.imageUri = imageUri;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_userimage, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Glide.with(context)
                .load(imageUri.get(position))
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.userImage);
    }

    @Override
    public int getItemCount() {
        return imageUri.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView userImage;
        public ViewHolder(View itemView) {
            super(itemView);
            userImage = (ImageView) itemView.findViewById(R.id.UserIv);
        }
    }
}
