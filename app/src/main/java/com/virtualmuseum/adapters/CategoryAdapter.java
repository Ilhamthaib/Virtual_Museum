package com.virtualmuseum.adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualmuseum.R;
import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.ArrayList;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    public interface OnCategoryClickListener {
        void onCategoryClick(CategoryEntity category);
    }

    private List<CategoryEntity>          categories;
    private final OnCategoryClickListener listener;

    public CategoryAdapter(List<CategoryEntity> categories, OnCategoryClickListener listener) {
        this.categories = new ArrayList<>(categories);
        this.listener   = listener;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView card;
        TextView tvIcon;
        TextView tvName;
        TextView tvDesc;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card   = itemView.findViewById(R.id.card_category);
            tvIcon = itemView.findViewById(R.id.tv_category_icon);
            tvName = itemView.findViewById(R.id.tv_category_name);
            tvDesc = itemView.findViewById(R.id.tv_category_desc);
        }
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryEntity category = categories.get(position);
        holder.tvIcon.setText(category.getIconEmoji());
        holder.tvName.setText(category.getName());
        holder.tvDesc.setText(category.getDescription());
        try {
            holder.card.setCardBackgroundColor(Color.parseColor(category.getColorHex()));
        } catch (IllegalArgumentException e) {
            holder.card.setCardBackgroundColor(Color.WHITE);
        }
        holder.card.setOnClickListener(v -> listener.onCategoryClick(category));
    }

    @Override
    public int getItemCount() { return categories.size(); }

    public void updateList(List<CategoryEntity> newList) {
        this.categories = new ArrayList<>(newList);
        notifyDataSetChanged();
    }
}
