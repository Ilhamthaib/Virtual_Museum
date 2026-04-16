package com.virtualmuseum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualmuseum.R;
import com.virtualmuseum.adapters.CategoryAdapter;
import com.virtualmuseum.database.entities.CategoryEntity;
import com.virtualmuseum.database.repository.MuseumViewModel;

import java.util.ArrayList;

public class CategoriesActivity extends AppCompatActivity {

    private RecyclerView    recyclerView;
    private CategoryAdapter adapter;
    private MuseumViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Virtual Museum");

        recyclerView = findViewById(R.id.rv_categories);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new CategoryAdapter(new ArrayList<>(), this::openArtworkList);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MuseumViewModel.class);
        viewModel.getAllCategories().observe(this, categories -> {
            if (categories != null) adapter.updateList(categories);
        });
    }

    private void openArtworkList(CategoryEntity category) {
        Intent intent = new Intent(this, ArtworkListActivity.class);
        intent.putExtra(ArtworkListActivity.EXTRA_CATEGORY_ID,   category.getId());
        intent.putExtra(ArtworkListActivity.EXTRA_CATEGORY_NAME, category.getName());
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_favorites) {
            startActivity(new Intent(this, FavoritesActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
