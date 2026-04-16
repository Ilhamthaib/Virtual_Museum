package com.virtualmuseum.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualmuseum.R;
import com.virtualmuseum.database.entities.ArtworkEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class ArtworkAdapter extends RecyclerView.Adapter<ArtworkAdapter.ViewHolder> {

    public interface OnArtworkClickListener {
        void onArtworkClick(ArtworkEntity artwork);
    }

    private List<ArtworkEntity>          artworks;
    private final OnArtworkClickListener listener;

    private static final int[] PLACEHOLDER_COLORS = {
        R.color.cat1, R.color.cat2, R.color.cat3, R.color.cat4, R.color.cat5
    };

    public ArtworkAdapter(List<ArtworkEntity> artworks, OnArtworkClickListener listener) {
        this.artworks = new ArrayList<>(artworks);
        this.listener = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivThumb;
        TextView  tvTitle;
        TextView  tvArtist;
        TextView  tvYear;
        ImageView ivFavoriteBadge;
        TextView  tv3DBadge;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivThumb         = itemView.findViewById(R.id.iv_artwork_thumb);
            tvTitle         = itemView.findViewById(R.id.tv_artwork_title);
            tvArtist        = itemView.findViewById(R.id.tv_artwork_artist);
            tvYear          = itemView.findViewById(R.id.tv_artwork_year);
            ivFavoriteBadge = itemView.findViewById(R.id.iv_favorite_badge);
            tv3DBadge       = itemView.findViewById(R.id.tv_has_3d);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_artwork, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ArtworkEntity artwork = artworks.get(position);

        // Charger l'image depuis assets si disponible, sinon couleur de placeholder
        boolean imageLoaded = false;
        String imageUri = artwork.getImageUri();
        if (imageUri != null && imageUri.startsWith("file:///android_asset/")) {
            String assetPath = imageUri.replace("file:///android_asset/", "");
            try {
                InputStream is = holder.itemView.getContext().getAssets().open(assetPath);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                if (bmp != null) {
                    holder.ivThumb.setImageBitmap(bmp);
                    holder.ivThumb.setBackgroundColor(0);
                    imageLoaded = true;
                }
            } catch (IOException ignored) {}
        }

        if (!imageLoaded) {
            holder.ivThumb.setImageDrawable(null);
            int colorIdx = Math.max(0, Math.min(artwork.getCategoryId() - 1,
                                                PLACEHOLDER_COLORS.length - 1));
            holder.ivThumb.setBackgroundColor(
                ContextCompat.getColor(holder.itemView.getContext(), PLACEHOLDER_COLORS[colorIdx]));
        }

        holder.tvTitle.setText(artwork.getTitle());
        holder.tvArtist.setText(artwork.getArtist());
        holder.tvYear.setText(artwork.getYearLabel());

        holder.ivFavoriteBadge.setVisibility(artwork.isFavorite() ? View.VISIBLE : View.GONE);
        holder.tv3DBadge.setVisibility(artwork.has3DModel() ? View.VISIBLE : View.GONE);

        holder.itemView.setOnClickListener(v -> listener.onArtworkClick(artwork));
    }

    @Override
    public int getItemCount() { return artworks.size(); }

    public void updateList(List<ArtworkEntity> newList) {
        this.artworks = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
