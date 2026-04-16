package com.virtualmuseum.database.repository;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.List;

/**
 * MuseumViewModel — utilisé depuis toutes les Activities.
 *
 * FIX : artworksByCategory initialisé dans le constructeur APRÈS repo
 *       pour éviter "variable repo might not have been initialized".
 */
public class MuseumViewModel extends AndroidViewModel {

    private final MuseumRepository repo;

    // filtre catégorie actif (-1 = toutes)
    private final MutableLiveData<Integer> categoryFilter = new MutableLiveData<>(-1);

    // Déclaré final, initialisé dans le constructeur après repo
    public final LiveData<List<ArtworkEntity>> artworksByCategory;

    public MuseumViewModel(@NonNull Application application) {
        super(application);

        // 1️⃣ repo initialisé EN PREMIER
        repo = new MuseumRepository(application);

        // 2️⃣ artworksByCategory initialisé APRÈS repo
        artworksByCategory = Transformations.switchMap(categoryFilter, catId ->
            catId == -1
                ? repo.getAllArtworksLive()
                : repo.getArtworksByCategoryLive(catId)
        );
    }

    // ── Categories ────────────────────────────────────────────────────────────

    public LiveData<List<CategoryEntity>> getAllCategories() {
        return repo.getAllCategoriesLive();
    }

    // ── Artworks ──────────────────────────────────────────────────────────────

    public LiveData<List<ArtworkEntity>> getAllArtworks() {
        return repo.getAllArtworksLive();
    }

    public void setCategoryFilter(int categoryId) {
        categoryFilter.setValue(categoryId);
    }

    public LiveData<List<ArtworkEntity>> searchArtworks(String query) {
        return repo.searchArtworksLive(query);
    }

    public LiveData<List<ArtworkEntity>> getFavorites() {
        return repo.getFavoritesLive();
    }

    // ── Favorites ─────────────────────────────────────────────────────────────

    public void toggleFavorite(ArtworkEntity artwork) {
        repo.toggleFavorite(artwork);
    }

    public void setFavorite(int artworkId, boolean fav) {
        repo.setFavorite(artworkId, fav);
    }
}
