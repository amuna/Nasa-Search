package com.space.search.View.Adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;

import com.space.search.Model.Response;
import com.space.search.R;


import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class SearchItemAdapter extends RecyclerView.Adapter<SearchItemAdapter.NoteHolder> {
    private List<Response.Item> allItems = new ArrayList<>();
    private RequestOptions options;
    private Context mContext;
    private OnItemClickListener listener;


    public SearchItemAdapter(OnItemClickListener listener){
        options = new RequestOptions();
        options.centerCrop();
        this.listener = listener;
    }


    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.search_item, parent, false);
        return new NoteHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder holder, int position) {
        Response.Item currentItem = allItems.get(position);

        holder.title.setText(currentItem.getData().get(0).getTitle());
        holder.desc.setText(String.valueOf(currentItem.getData().get(0).getDescription()));

        Glide.with(mContext)
                .load(currentItem.getLinks().get(0).getHref())
                .apply(options)
                //show progress bar while image is rendering
                .listener(new RequestListener<Drawable>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                        holder.progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(holder.dp);
    }

    @Override
    public int getItemCount() {
        return allItems.size();
    }

    public void setItems(List<Response.Item> items) {
        this.allItems = items;
        notifyDataSetChanged();
    }

    public Response.Item getItemAt(int position) {
        return allItems.get(position);
    }

    class NoteHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView title;
        private TextView desc;
        private ImageView dp;
        private ProgressBar progressBar;

        public NoteHolder(@NonNull final View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
            dp = itemView.findViewById(R.id.dp);
            progressBar = itemView.findViewById(R.id.progress);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (listener != null) {
                Response.Item item = getItemAt(getAdapterPosition());
                listener.onClick(item, v);
            }
        }
    }
}
