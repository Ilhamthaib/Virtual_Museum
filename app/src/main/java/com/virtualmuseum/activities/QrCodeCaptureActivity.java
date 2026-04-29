package com.virtualmuseum.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Activité de capture du QR code du casque Cardboard.
 * Stub minimal : affiche un écran simple avec deux boutons
 * (simuler un scan / annuler). À remplacer plus tard par
 * une vraie intégration ZXing ou ML Kit.
 */
public class QrCodeCaptureActivity extends AppCompatActivity {

    public static final String EXTRA_QR_RESULT = "extra_qr_result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setGravity(android.view.Gravity.CENTER);
        root.setPadding(48, 48, 48, 48);
        root.setBackgroundColor(0xFF000000);

        TextView title = new TextView(this);
        title.setText("📷 Scan QR du casque Cardboard");
        title.setTextColor(0xFFFFFFFF);
        title.setTextSize(18);
        title.setPadding(0, 0, 0, 48);
        root.addView(title);

        Button btnSimulate = new Button(this);
        btnSimulate.setText("Simuler un scan réussi");
        btnSimulate.setOnClickListener(v -> {
            getIntent().putExtra(EXTRA_QR_RESULT, "default-cardboard-v2");
            setResult(RESULT_OK, getIntent());
            finish();
        });
        root.addView(btnSimulate);

        Button btnCancel = new Button(this);
        btnCancel.setText("Annuler");
        btnCancel.setOnClickListener(v -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        lp.topMargin = 24;
        root.addView(btnCancel, lp);

        setContentView(root);
    }
}