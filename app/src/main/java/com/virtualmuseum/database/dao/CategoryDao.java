package com.virtualmuseum.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.List;

@Dao
public interface CategoryDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<CategoryEntity> categories);

    @Query("SELECT * FROM categories ORDER BY name ASC")
    LiveData<List<CategoryEntity>> getAllLive();

    @Query("SELECT * FROM categories ORDER BY name ASC")
    List<CategoryEntity> getAllSync();

    @Query("SELECT * FROM categories WHERE id = :id")
    CategoryEntity getByIdSync(int id);

    @Query("SELECT COUNT(*) FROM categories")
    int count();
}
