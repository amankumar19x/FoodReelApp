package com.example.foodreelappassignment.adapter;

import android.content.Context;


import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.media3.common.AudioAttributes;
import androidx.media3.common.C;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;


import com.example.foodreelappassignment.R;
import com.example.foodreelappassignment.model.FoodData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private Context context;
    private ArrayList<FoodData> dataArrayList;

    public MyAdapter(Context context, ArrayList<FoodData> dataArrayList) {
        this.context = context;
        this.dataArrayList = dataArrayList;
        loadBookmarks(); // Load saved bookmarks
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.single_item_layout,parent, false);

        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        holder.txtFoodTitle.setText(dataArrayList.get(position).getTitle());
        holder.txtFoodLocation.setText(dataArrayList.get(position).getLocation());

        String url = dataArrayList.get(position).getUrl();

        holder.bind(url);

        // Set bookmark icon based on saved state
        holder.bookmarkIcon.setImageResource(dataArrayList.get(position).isBookmarked() ?
                R.drawable.bookmark_filled : R.drawable.bookmark_black);

        // Handle bookmark click
        holder.bookmarkIcon.setOnClickListener(v -> {
            boolean newState = !dataArrayList.get(position).isBookmarked();
            dataArrayList.get(position).setBookmarked(newState);
            holder.bookmarkIcon.setImageResource(newState ?
                    R.drawable.bookmark_filled : R.drawable.bookmark_black);
            saveBookmark(dataArrayList.get(position)); // Save bookmark state
        });

    }


    @Override
    public int getItemCount() {
        return dataArrayList.size();
    }


    // Automatically release ExoPlayer when a ViewHolder is recycled
    @Override
    public void onViewRecycled(@NonNull MyAdapter.ViewHolder holder) {
        super.onViewRecycled(holder);
        holder.releasePlayer();
    }

    // Release all ExoPlayers in the tracked ViewHolders


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txtFoodTitle, txtFoodLocation;
        Button btnOrder;
        PlayerView player_view;
        public ExoPlayer exoPlayer;
        ImageView bookmarkIcon;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtFoodTitle = itemView.findViewById(R.id.txtFoodTitle);
            txtFoodLocation = itemView.findViewById(R.id.txtFoodLocation);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            player_view = itemView.findViewById(R.id.player_view);
            bookmarkIcon = itemView.findViewById(R.id.imgBookmarkIcon);

            Log.d("VideoDebug", "Initializing ExoPlayer in ViewHolder");

            // 🔹 Initialize ExoPlayer only once
            if (exoPlayer == null) {
                exoPlayer = new ExoPlayer.Builder(itemView.getContext()).build();
                player_view.setPlayer(exoPlayer);
            }
        }

        public void bind(String videoUrl) {
            if (exoPlayer == null) {
                Log.e("VideoDebug", "ExoPlayer was null in bind()! Re-initializing.");
                exoPlayer = new ExoPlayer.Builder(itemView.getContext()).build();
                player_view.setPlayer(exoPlayer);
            }

            Log.d("VideoDebug", "Binding video: " + videoUrl);

            MediaItem mediaItem = MediaItem.fromUri(Uri.parse(videoUrl));
            exoPlayer.setMediaItem(mediaItem);
            exoPlayer.prepare();
            exoPlayer.setPlayWhenReady(true);
            exoPlayer.setRepeatMode(Player.REPEAT_MODE_ALL);
        }

        public void releasePlayer() {
            if (exoPlayer != null) {
                Log.d("VideoDebug", "Releasing ExoPlayer");
                exoPlayer.setPlayWhenReady(false);
                exoPlayer.release();
                exoPlayer = null; // 🔹 Important: Only set null if really necessary
            }
        }
    }

    // Save bookmarks in SharedPreferences
    private void saveBookmark(FoodData foodData) {
        SharedPreferences prefs = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        Set<String> bookmarkedUrls = prefs.getStringSet("savedReels", new HashSet<>());

        if (foodData.isBookmarked()) {
            bookmarkedUrls.add(foodData.getUrl()); // Add URL to bookmarks
        } else {
            bookmarkedUrls.remove(foodData.getUrl()); // Remove if unbookmarked
        }

        editor.putStringSet("savedReels", bookmarkedUrls);
        editor.apply();
    }

    // Load saved bookmarks when initializing adapter
    private void loadBookmarks() {

        if (context == null) return; // Prevents NullPointerException
        SharedPreferences prefs = context.getSharedPreferences("Bookmarks", Context.MODE_PRIVATE);
        Set<String> bookmarkedUrls = prefs.getStringSet("savedReels", new HashSet<>());

        for (FoodData food : dataArrayList) {
            if (bookmarkedUrls.contains(food.getUrl())) {
                food.setBookmarked(true);
            }
        }
    }


}
