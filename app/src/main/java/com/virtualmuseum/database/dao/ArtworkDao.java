package com.virtualmuseum.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.virtualmuseum.database.entities.ArtworkEntity;

import java.util.List;

@Dao
public interface ArtworkDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<ArtworkEntity> artworks);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(ArtworkEntity artwork);

    @Update
    void update(ArtworkEntity artwork);

    // ── READ ──────────────────────────────────────────────────────────────────

    @Query("SELECT * FROM artworks ORDER BY title ASC")
    LiveData<List<ArtworkEntity>> getAllLive();

    @Query("SELECT * FROM artworks ORDER BY title ASC")
    List<ArtworkEntity> getAllSync();

    @Query("SELECT * FROM artworks WHERE id = :id")
    ArtworkEntity getByIdSync(int id);

    @Query("SELECT * FROM artworks WHERE categoryId = :catId ORDER BY title ASC")
    LiveData<List<ArtworkEntity>> getByCategoryLive(int catId);

    @Query("SELECT * FROM artworks WHERE categoryId = :catId ORDER BY title ASC")
    List<ArtworkEntity> getByCategorySync(int catId);

    @Query("SELECT * FROM artworks WHERE isFavorite = 1 ORDER BY title ASC")
    LiveData<List<ArtworkEntity>> getFavoritesLive();

    @Query("SELECT * FROM artworks WHERE isFavorite = 1 ORDER BY title ASC")
    List<ArtworkEntity> getFavoritesSync();

    @Query("SELECT * FROM artworks " +
           "WHERE title LIKE '%' || :q || '%' OR artist LIKE '%' || :q || '%' " +
           "ORDER BY title ASC")
    LiveData<List<ArtworkEntity>> searchLive(String q);

    // ── Favorite toggle ───────────────────────────────────────────────────────

    @Query("UPDATE artworks SET isFavorite = :fav WHERE id = :id")
    void setFavorite(int id, boolean fav);

    // ── Utility ───────────────────────────────────────────────────────────────

    @Query("SELECT COUNT(*) FROM artworks")
    int count();
}
