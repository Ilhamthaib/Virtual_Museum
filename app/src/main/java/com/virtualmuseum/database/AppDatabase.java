package com.virtualmuseum.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.virtualmuseum.database.dao.ArtworkDao;
import com.virtualmuseum.database.dao.CategoryDao;
import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(
    entities = { CategoryEntity.class, ArtworkEntity.class },
    version  = 6,
    exportSchema = false
)
public abstract class AppDatabase extends RoomDatabase {

    public abstract CategoryDao categoryDao();
    public abstract ArtworkDao  artworkDao();

    // Thread pool partagé pour toutes les opérations DB
    public static final ExecutorService DB_EXECUTOR = Executors.newFixedThreadPool(4);

    // ── Singleton ─────────────────────────────────────────────────────────────
    private static volatile AppDatabase INSTANCE;

    public static AppDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(
                            context.getApplicationContext(),
                            AppDatabase.class,
                            "virtual_museum.db"
                    ).fallbackToDestructiveMigration()
                    .addCallback(SEED_CALLBACK)
                    .build();
                }
            }
        }
        return INSTANCE;
    }

    // ── Pré-population au 1er lancement ──────────────────────────────────────
    private static final RoomDatabase.Callback SEED_CALLBACK = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            DB_EXECUTOR.execute(() -> {
                if (INSTANCE != null) DatabaseSeeder.seed(INSTANCE);
            });
        }
    };
}
