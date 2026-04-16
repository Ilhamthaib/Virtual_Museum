package com.virtualmuseum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualmuseum.R;
import com.virtualmuseum.adapters.ArtworkAdapter;
import com.virtualmuseum.database.repository.MuseumViewModel;

import java.util.ArrayList;

public class FavoritesActivity extends AppCompatActivity {

    private RecyclerView   recyclerView;
    private LinearLayout   layoutEmpty;
    private ArtworkAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Mes Favoris ❤");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_favorites);
        layoutEmpty  = findViewById(R.id.tv_empty);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ArtworkAdapter(new ArrayList<>(), artwork -> {
            Intent intent = new Intent(this, ArtworkDetailsActivity.class);
            intent.putExtra(ArtworkDetailsActivity.EXTRA_ARTWORK_ID, artwork.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        MuseumViewModel vm = new ViewModelProvider(this).get(MuseumViewModel.class);
        vm.getFavorites().observe(this, favorites -> {
            adapter.updateList(favorites != null ? favorites : new ArrayList<>());
            boolean empty = favorites == null || favorites.isEmpty();
            layoutEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            recyclerView.setVisibility(empty ? View.GONE   : View.VISIBLE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
