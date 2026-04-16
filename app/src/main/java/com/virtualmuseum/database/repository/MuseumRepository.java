package com.virtualmuseum.database.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.virtualmuseum.database.AppDatabase;
import com.virtualmuseum.database.dao.ArtworkDao;
import com.virtualmuseum.database.dao.CategoryDao;
import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.List;

/**
 * MuseumRepository — source unique de données pour toute l'application.
 * Remplace l'ancien DataManager.
 */
public class MuseumRepository {

    private final ArtworkDao  artworkDao;
    private final CategoryDao categoryDao;

    public MuseumRepository(Application app) {
        AppDatabase db = AppDatabase.getInstance(app);
        artworkDao  = db.artworkDao();
        categoryDao = db.categoryDao();
    }

    // ── Categories ────────────────────────────────────────────────────────────

    public LiveData<List<CategoryEntity>> getAllCategoriesLive() {
        return categoryDao.getAllLive();
    }

    public List<CategoryEntity> getAllCategoriesSync() {
        return categoryDao.getAllSync();
    }

    // ── Artworks ──────────────────────────────────────────────────────────────

    public LiveData<List<ArtworkEntity>> getAllArtworksLive() {
        return artworkDao.getAllLive();
    }

    public LiveData<List<ArtworkEntity>> getArtworksByCategoryLive(int categoryId) {
        return artworkDao.getByCategoryLive(categoryId);
    }

    public List<ArtworkEntity> getArtworksByCategorySync(int categoryId) {
        return artworkDao.getByCategorySync(categoryId);
    }

    public LiveData<List<ArtworkEntity>> searchArtworksLive(String query) {
        return artworkDao.searchLive(query);
    }

    public LiveData<List<ArtworkEntity>> getFavoritesLive() {
        return artworkDao.getFavoritesLive();
    }

    public List<ArtworkEntity> getFavoritesSync() {
        return artworkDao.getFavoritesSync();
    }

    /** Synchronous — appeler depuis un background thread. */
    public ArtworkEntity getArtworkByIdSync(int id) {
        return artworkDao.getByIdSync(id);
    }

    // ── Favorites ─────────────────────────────────────────────────────────────

    public void toggleFavorite(ArtworkEntity artwork) {
        AppDatabase.DB_EXECUTOR.execute(() -> {
            boolean newState = !artwork.isFavorite();
            artworkDao.setFavorite(artwork.getId(), newState);
            artwork.setFavorite(newState);
        });
    }

    public void setFavorite(int artworkId, boolean fav) {
        AppDatabase.DB_EXECUTOR.execute(() -> artworkDao.setFavorite(artworkId, fav));
    }
}
