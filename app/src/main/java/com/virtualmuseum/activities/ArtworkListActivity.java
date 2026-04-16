package com.virtualmuseum.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.virtualmuseum.R;
import com.virtualmuseum.adapters.ArtworkAdapter;
import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.repository.MuseumViewModel;

import java.util.ArrayList;

public class ArtworkListActivity extends AppCompatActivity {

    public static final String EXTRA_CATEGORY_ID   = "extra_category_id";
    public static final String EXTRA_CATEGORY_NAME = "extra_category_name";

    private RecyclerView   recyclerView;
    private EditText       etSearch;
    private ArtworkAdapter adapter;
    private MuseumViewModel viewModel;
    private int categoryId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_list);

        categoryId = getIntent().getIntExtra(EXTRA_CATEGORY_ID, -1);
        String categoryName = getIntent().getStringExtra(EXTRA_CATEGORY_NAME);
        if (categoryName == null) categoryName = "Œuvres";

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(categoryName);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        recyclerView = findViewById(R.id.rv_artworks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ArtworkAdapter(new ArrayList<>(), this::openDetail);
        recyclerView.setAdapter(adapter);

        viewModel = new ViewModelProvider(this).get(MuseumViewModel.class);

        // Charger par catégorie ou tout
        if (categoryId == -1) {
            viewModel.getAllArtworks().observe(this, list -> {
                if (list != null) adapter.updateList(list);
            });
        } else {
            viewModel.setCategoryFilter(categoryId);
            viewModel.artworksByCategory.observe(this, list -> {
                if (list != null) adapter.updateList(list);
            });
        }

        // Recherche live
        etSearch = findViewById(R.id.et_search);
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c)     {}
            @Override
            public void afterTextChanged(Editable s) {
                String query = s.toString().trim();
                if (query.isEmpty()) {
                    // Revenir à la liste filtrée par catégorie
                    if (categoryId == -1) {
                        viewModel.getAllArtworks().observe(ArtworkListActivity.this,
                            list -> { if (list != null) adapter.updateList(list); });
                    } else {
                        viewModel.artworksByCategory.observe(ArtworkListActivity.this,
                            list -> { if (list != null) adapter.updateList(list); });
                    }
                } else {
                    viewModel.searchArtworks(query).observe(ArtworkListActivity.this,
                        list -> { if (list != null) adapter.updateList(list); });
                }
            }
        });
    }

    private void openDetail(ArtworkEntity artwork) {
        Intent intent = new Intent(this, ArtworkDetailsActivity.class);
        intent.putExtra(ArtworkDetailsActivity.EXTRA_ARTWORK_ID, artwork.getId());
        startActivity(intent);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}
