package com.virtualmuseum.activities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.material.button.MaterialButton;
import com.google.ar.core.ArCoreApk;
import com.google.ar.core.Config;
import com.virtualmuseum.R;
import com.virtualmuseum.database.AppDatabase;
import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.repository.MuseumRepository;

import io.github.sceneview.ar.ArSceneView;
import io.github.sceneview.ar.node.ArModelNode;
import io.github.sceneview.ar.node.PlacementMode;
import dev.romainguy.kotlin.math.Float3;


public class ArViewerActivity extends AppCompatActivity {

    public static final String EXTRA_ARTWORK_ID    = "extra_artwork_id";
    public static final String EXTRA_ARTWORK_TITLE = "extra_artwork_title";

    private static final int REQUEST_CAMERA = 1001;

    // Views
    private ArSceneView arSceneView;
    private LinearLayout layoutLoading;
    private TextView tvArStatus;
    private TextView tvLoading;
    private MaterialButton btnReset;
    private MaterialButton btnScaleUp;
    private MaterialButton btnScaleDown;
    private MaterialButton btnToggleMode;

    // State
    private ArModelNode modelNode;
    private float currentScale = 1.0f;
    private boolean isArMode = true;
    private boolean modelPlaced = false;
    private String artworkTitle = "Œuvre";
    private String model3DUri = null;

    // Scale constants
    private static final float SCALE_STEP = 0.25f;
    private static final float SCALE_MIN  = 0.25f;
    private static final float SCALE_MAX  = 4.0f;


    // ─────────────────────────────────────────────────────────────────────────
    // LIFECYCLE
    // ─────────────────────────────────────────────────────────────────────────

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ar_viewer);

        artworkTitle = getIntent().getStringExtra(EXTRA_ARTWORK_TITLE);
        if (artworkTitle == null) artworkTitle = "Œuvre";

        int artworkId = getIntent().getIntExtra(EXTRA_ARTWORK_ID, -1);

        bindViews();
        setupToolbar();
        setupButtons();

        if (artworkId != -1) {
            MuseumRepository repo = new MuseumRepository(getApplication());
            AppDatabase.DB_EXECUTOR.execute(() -> {
                ArtworkEntity artwork = repo.getArtworkByIdSync(artworkId);
                runOnUiThread(() -> {
                    if (artwork != null && artwork.has3DModel()) {
                        model3DUri = artwork.getModel3DUri();
                        checkCameraPermissionAndStart();
                    } else {
                        showError("Aucun modèle 3D disponible pour cette œuvre.");
                    }
                });
            });
        } else {
            checkCameraPermissionAndStart();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (modelNode != null) {
            modelNode.destroy();
            modelNode = null;
        }
        if (arSceneView != null) {
            arSceneView.destroy();
            arSceneView = null;
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // UI
    // ─────────────────────────────────────────────────────────────────────────

    private void bindViews() {
        arSceneView   = findViewById(R.id.ar_scene_view);
        layoutLoading = findViewById(R.id.layout_loading);
        tvArStatus    = findViewById(R.id.tv_ar_status);
        tvLoading     = findViewById(R.id.tv_loading);
        btnReset      = findViewById(R.id.btn_reset);
        btnScaleUp    = findViewById(R.id.btn_scale_up);
        btnScaleDown  = findViewById(R.id.btn_scale_down);
        btnToggleMode = findViewById(R.id.btn_toggle_mode);
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("3D / AR — " + artworkTitle);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setupButtons() {

        btnReset.setOnClickListener(v -> {
            if (modelNode != null) {
                arSceneView.removeChild(modelNode);
                modelNode.destroy();
                modelNode = null;
                modelPlaced = false;
                currentScale = 1.0f;
                setStatus("🔍 Touchez une surface pour placer l'œuvre");
            }
        });

        btnScaleUp.setOnClickListener(v -> {
            if (modelNode != null) {
                currentScale = Math.min(currentScale + SCALE_STEP, SCALE_MAX);
                modelNode.setWorldScale(new Float3(currentScale, currentScale, currentScale));
            }
        });

        btnScaleDown.setOnClickListener(v -> {
            if (modelNode != null) {
                currentScale = Math.max(currentScale - SCALE_STEP, SCALE_MIN);
                modelNode.setWorldScale(new Float3(currentScale, currentScale, currentScale));
            }
        });

        btnToggleMode.setOnClickListener(v -> toggleArMode());
    }

    // ─────────────────────────────────────────────────────────────────────────
    // CAMERA PERMISSION
    // ─────────────────────────────────────────────────────────────────────────

    private void checkCameraPermissionAndStart() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            initScene();
        } else {
            ActivityCompat.requestPermissions(
                    this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA
            );
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults); // ✅ FIX

        if (requestCode == REQUEST_CAMERA) {
            if (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                initScene();
            } else {
                Toast.makeText(
                        this,
                        "Caméra refusée — mode 3D activé",
                        Toast.LENGTH_LONG
                ).show();
                isArMode = false;
                initScene();
            }
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // SCENE
    // ─────────────────────────────────────────────────────────────────────────

    private void initScene() {

        ArCoreApk.Availability availability =
                ArCoreApk.getInstance().checkAvailability(this);

        if (availability == ArCoreApk.Availability.UNSUPPORTED_DEVICE_NOT_CAPABLE) {
            isArMode = false;
            Toast.makeText(this,
                    "ARCore non supporté — mode 3D",
                    Toast.LENGTH_SHORT).show();
        }

        if (isArMode) {
            setupArMode();
        } else {
            setup3DMode();
        }

        loadModel();
    }

    private void setupArMode() {

        arSceneView.configureSession((session, config) -> {
            config.setPlaneFindingMode(
                    Config.PlaneFindingMode.HORIZONTAL_AND_VERTICAL
            );
            config.setLightEstimationMode(
                    Config.LightEstimationMode.ENVIRONMENTAL_HDR
            );
            return null;
        });

        arSceneView.setOnTouchListener((v, event) -> {
            if (!modelPlaced && modelNode != null) {
                arSceneView.addChild(modelNode);
                modelPlaced = true;
                btnReset.setEnabled(true);
                setStatus("✅ Modèle placé");
            }
            return false;
        });

        setStatus("🔍 Touchez une surface plane pour placer l'œuvre");
    }

    private void setup3DMode() {
        btnToggleMode.setText("🥽 Voir en AR");
        setStatus("📱 Mode 3D — rotation et zoom");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MODEL
    // ─────────────────────────────────────────────────────────────────────────

    private void loadModel() {

        String modelUri = (model3DUri != null)
                ? model3DUri
                : "models/default_monument.glb";

        tvLoading.setText("Chargement de " + artworkTitle + "…");
        layoutLoading.setVisibility(View.VISIBLE);

        modelNode = new ArModelNode(arSceneView.getEngine());
        modelNode.setPlacementMode(PlacementMode.INSTANT);

        modelNode.loadModelGlbAsync(
                modelUri,
                true,
                currentScale,
                new Float3(0f, 0f, 0f),
                error -> {
                    layoutLoading.setVisibility(View.GONE);
                    showFallback3D(artworkTitle);
                    return null;
                },
                instance -> {
                    layoutLoading.setVisibility(View.GONE);
                    if (!isArMode) {
                        arSceneView.addChild(modelNode);
                        modelPlaced = true;
                    }
                    return null;
                }
        );
    }

    // ─────────────────────────────────────────────────────────────────────────
    // MODE SWITCH
    // ─────────────────────────────────────────────────────────────────────────

    private void toggleArMode() {

        if (modelNode == null) return;

        // Retirer le modèle de la scène
        arSceneView.removeChild(modelNode);
        modelPlaced = false;

        isArMode = !isArMode;

        if (isArMode) {
            // Mode AR
            btnToggleMode.setText("📱 Voir en 3D");
            setStatus("🔍 Touchez une surface plane pour placer l'œuvre");
            Toast.makeText(this, "Mode AR activé", Toast.LENGTH_SHORT).show();
        } else {
            // Mode 3D
            btnToggleMode.setText("🥽 Voir en AR");
            arSceneView.addChild(modelNode);
            modelPlaced = true;
            setStatus("📱 Mode 3D — rotation et zoom");
            Toast.makeText(this, "Mode 3D activé", Toast.LENGTH_SHORT).show();
        }
    }

    // ─────────────────────────────────────────────────────────────────────────
    // HELPERS
    // ─────────────────────────────────────────────────────────────────────────

    private void setStatus(String msg) {
        if (tvArStatus != null) tvArStatus.setText(msg);
    }

    private void showFallback3D(String title) {
        arSceneView.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        tvLoading.setText(
                "🏛 " + title + "\n\n" +
                        "Modèle 3D non disponible.\n" +
                        "Placez un fichier GLB dans assets/models/"
        );
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    private void showError(String message) {
        arSceneView.setVisibility(View.GONE);
        layoutLoading.setVisibility(View.VISIBLE);
        tvLoading.setText("⚠️ " + message);
        findViewById(R.id.progress_bar).setVisibility(View.GONE);
    }

    @Override
    public boolean onSupportNavigateUp() {
        getOnBackPressedDispatcher().onBackPressed();
        return true;
    }
}