package com.virtualmuseum.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.virtualmuseum.R;
import com.virtualmuseum.vr.VrGlSurfaceView;

public class VrViewerActivity extends Activity {

    public static final String EXTRA_ARTWORK_TITLE = "extra_vr_artwork_title";
    public static final String EXTRA_MODEL_URI     = "extra_vr_model_uri";

    private static final int REQUEST_SCAN_QR = 100;

    private CardboardManager cardboardManager;
    private String artworkTitle;
    private String modelUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vr_viewer);

        artworkTitle = getIntent().getStringExtra(EXTRA_ARTWORK_TITLE);
        modelUri     = getIntent().getStringExtra(EXTRA_MODEL_URI);

        if (artworkTitle == null) artworkTitle = "Œuvre";

        TextView tvTitle = findViewById(R.id.tv_vr_title);
        tvTitle.setText("🥽 VR — " + artworkTitle);

        cardboardManager = new CardboardManager(this);

        // Bouton scan QR code du casque
        findViewById(R.id.btn_scan_qr).setOnClickListener(v -> {
            Intent intent = new Intent(this, QrCodeCaptureActivity.class);
            startActivityForResult(intent, REQUEST_SCAN_QR);
        });

        // Bouton lancer VR
        findViewById(R.id.btn_start_vr).setOnClickListener(v -> startVrRendering());

        // Bouton retour
        findViewById(R.id.btn_vr_back).setOnClickListener(v -> finish());
    }

    private void startVrRendering() {
        // Point d'entrée : ici tu intègres ton renderer OpenGL ES stéréoscopique
        // Pour Meta Quest : utilise le même GLB via OpenXR / Unity / Godot
        Toast.makeText(this,
                "🥽 Mode VR activé — modèle : " + artworkTitle,
                Toast.LENGTH_SHORT).show();

        // Démarre le rendu stéréo (à connecter à ton renderer)
        VrGlSurfaceView vrView = findViewById(R.id.vr_gl_surface);
        if (vrView != null) {
            vrView.setVisibility(View.VISIBLE);
            vrView.startVr(cardboardManager, modelUri);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SCAN_QR && resultCode == RESULT_OK) {
            Toast.makeText(this, "✅ Casque Cardboard configuré !", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onPause()  { super.onPause();  }

    @Override
    protected void onResume() { super.onResume(); }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (cardboardManager != null) cardboardManager.close();
    }
}