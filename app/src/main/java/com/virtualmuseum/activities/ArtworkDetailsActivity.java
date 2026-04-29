package com.virtualmuseum.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;

import com.virtualmuseum.R;
import com.virtualmuseum.database.AppDatabase;
import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.repository.MuseumRepository;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class ArtworkDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_ARTWORK_ID = "extra_artwork_id";

    private ImageView      ivArtwork;
    private TextView       tvTitle;
    private TextView       tvArtist;
    private Chip           chipYear;
    private TextView       tvDescription;
    private ImageButton    btnFavorite;
    private MaterialButton btnAudio;
    private MaterialButton btn3D;
    private MaterialButton btnVR;

    private ArtworkEntity    artwork;
    private MuseumRepository repo;
    private MediaPlayer      mediaPlayer;
    private TextToSpeech     tts;
    private boolean          isPlaying = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artwork_details);

        repo = new MuseumRepository(getApplication());
        int artworkId = getIntent().getIntExtra(EXTRA_ARTWORK_ID, -1);

        AppDatabase.DB_EXECUTOR.execute(() -> {
            artwork = repo.getArtworkByIdSync(artworkId);
            runOnUiThread(() -> {
                if (artwork == null) { finish(); return; }
                setupToolbar();
                bindViews();
                populateData();
                setupListeners();
            });
        });
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void bindViews() {
        ivArtwork     = findViewById(R.id.iv_artwork);
        tvTitle       = findViewById(R.id.tv_title);
        tvArtist      = findViewById(R.id.tv_artist);
        chipYear      = findViewById(R.id.tv_year);
        tvDescription = findViewById(R.id.tv_description);
        btnFavorite   = findViewById(R.id.btn_favorite);
        btnAudio      = findViewById(R.id.btn_audio);
        btn3D         = findViewById(R.id.btn_3d);
        btnVR = findViewById(R.id.btn_vr);
    }

    private void populateData() {
        loadImageFromAssets(artwork.getImageUri());

        tvTitle.setText(artwork.getTitle());
        tvArtist.setText("✏  " + artwork.getArtist());
        chipYear.setText(artwork.getYearLabel());
        tvDescription.setText(artwork.getDescription());
        android.util.Log.d("DEBUG_3D", "has3DModel=" + artwork.has3DModel() + " uri=" + artwork.getModel3DUri());
        updateFavoriteIcon();

        // Le bouton audio est toujours actif (raw ou TTS en fallback)
        btnAudio.setEnabled(true);
        btnAudio.setAlpha(1f);

        boolean has3D = artwork.has3DModel();
        btn3D.setEnabled(has3D);
        btn3D.setAlpha(has3D ? 1f : 0.4f);

        boolean hasVR = artwork.has3DModel();
        btnVR.setEnabled(hasVR);
        btnVR.setAlpha(hasVR ? 1f : 0.4f);
    }

    /**
     * Charge l'image depuis assets/images/ si disponible, sinon fond coloré.
     */
    private void loadImageFromAssets(String imageUri) {
        if (imageUri != null && imageUri.startsWith("file:///android_asset/")) {
            String assetPath = imageUri.replace("file:///android_asset/", "");
            try {
                InputStream is = getAssets().open(assetPath);
                Bitmap bmp = BitmapFactory.decodeStream(is);
                is.close();
                if (bmp != null) {
                    ivArtwork.setImageBitmap(bmp);
                    return;
                }
            } catch (IOException ignored) {}
        }
        ivArtwork.setBackgroundColor(
                ContextCompat.getColor(this, R.color.colorPrimaryLight));
    }

    private void setupListeners() {

        // ── Favori ────────────────────────────────────────────────────────────
        btnFavorite.setOnClickListener(v -> {
            repo.toggleFavorite(artwork);
            artwork.setFavorite(!artwork.isFavorite());
            updateFavoriteIcon();
            String msg = artwork.isFavorite() ? "Ajouté aux favoris ❤" : "Retiré des favoris";
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        });

        // ── Audio guide ───────────────────────────────────────────────────────
        btnAudio.setOnClickListener(v -> {
            if (isPlaying) {
                stopAudio();
            } else {
                String audioName = artwork.getAudioUri(); // ex: "joconde"
                if (audioName != null && !audioName.isEmpty()) {
                    playRawAudio(audioName);
                } else {
                    startTTS(); // fallback si audioUri est null
                }
            }
        });

        // ── Bouton 3D/AR ──────────────────────────────────────────────────────
        btn3D.setOnClickListener(v -> {
            if (!artwork.has3DModel()) {
                Toast.makeText(this, "Modèle 3D non disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, ArViewerActivity.class);
            intent.putExtra(ArViewerActivity.EXTRA_ARTWORK_ID,    artwork.getId());
            intent.putExtra(ArViewerActivity.EXTRA_ARTWORK_TITLE, artwork.getTitle());
            startActivity(intent);
        });

        btnVR.setOnClickListener(v -> {
            if (!artwork.has3DModel()) {
                Toast.makeText(this, "Modèle 3D non disponible", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(this, VrViewerActivity.class);
            intent.putExtra(VrViewerActivity.EXTRA_ARTWORK_TITLE, artwork.getTitle());
            intent.putExtra(VrViewerActivity.EXTRA_MODEL_URI, artwork.getModel3DUri());
            startActivity(intent);
        });
    }

    // ── Lecture depuis res/raw ─────────────────────────────────────────────────

    /**
     * Joue le fichier audio depuis res/raw/ via son nom de ressource.
     * audioName = "joconde"  →  R.raw.joconde
     * Si le fichier est absent → fallback TTS automatique.
     */
    private void playRawAudio(String audioName) {
        int resId = getResources().getIdentifier(
                audioName, "raw", getPackageName()
        );

        if (resId == 0) {
            // Ressource introuvable dans res/raw → fallback TTS
            Toast.makeText(this, "Fichier audio non trouvé, utilisation de la voix", Toast.LENGTH_SHORT).show();
            startTTS();
            return;
        }

        if (mediaPlayer != null) {
            mediaPlayer.release();
            mediaPlayer = null;
        }

        mediaPlayer = MediaPlayer.create(this, resId);

        if (mediaPlayer == null) {
            startTTS();
            return;
        }

        mediaPlayer.setOnCompletionListener(mp -> stopAudio());
        mediaPlayer.start();
        isPlaying = true;
        btnAudio.setText("⏹  Arrêter l'audio");
    }

    // ── TextToSpeech (fallback si pas de fichier raw) ─────────────────────────

    private void startTTS() {
        String texte = artwork.getTitle() + ". Par " + artwork.getArtist()
                + ", " + artwork.getYearLabel() + ". "
                + artwork.getDescription();
        tts = new TextToSpeech(this, status -> {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.FRENCH);
                if (result == TextToSpeech.LANG_MISSING_DATA
                        || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    tts.setLanguage(Locale.ENGLISH);
                }
                tts.speak(texte, TextToSpeech.QUEUE_FLUSH, null, "audio_guide");
                isPlaying = true;
                btnAudio.setText("⏹  Arrêter l'audio");
            } else {
                Toast.makeText(this, "Audio non disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void stopAudio() {
        if (mediaPlayer != null) {
            try { mediaPlayer.stop(); } catch (Exception ignored) {}
            mediaPlayer.release();
            mediaPlayer = null;
        }
        if (tts != null) {
            tts.stop();
            tts.shutdown();
            tts = null;
        }
        isPlaying = false;
        btnAudio.setText("▶  Audio Guide");
    }

    // ── Icône favori ──────────────────────────────────────────────────────────

    private void updateFavoriteIcon() {
        int iconRes  = artwork.isFavorite()
                ? R.drawable.ic_favorite_filled : R.drawable.ic_favorite_border;
        int colorRes = artwork.isFavorite()
                ? R.color.colorFavorite : R.color.colorGrey;
        btnFavorite.setImageResource(iconRes);
        btnFavorite.setColorFilter(ContextCompat.getColor(this, colorRes));
    }

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override protected void onPause() {
        super.onPause();
        if (isPlaying) stopAudio();
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) { mediaPlayer.release(); mediaPlayer = null; }
        if (tts != null)         { tts.shutdown(); tts = null; }
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}