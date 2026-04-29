package com.virtualmuseum.activities;

import android.content.Context;
import android.util.Log;

/**
 * Gestionnaire Cardboard minimal.
 * Encapsule la configuration du casque (paramètres lentilles, distorsion, etc.)
 * Stocke la config QR code scannée et la fournit au renderer stéréo.
 */
public class CardboardManager {

    private static final String TAG = "CardboardManager";

    private final Context context;
    private boolean initialized = false;
    private String deviceParams = null;

    public CardboardManager(Context context) {
        this.context = context.getApplicationContext();
        this.initialized = true;
        Log.i(TAG, "CardboardManager initialisé");
    }

    /** Appelé après scan du QR code du casque. */
    public void setDeviceParams(String params) {
        this.deviceParams = params;
        Log.i(TAG, "Paramètres casque enregistrés");
    }

    public String getDeviceParams() {
        return deviceParams;
    }

    public boolean isInitialized() {
        return initialized;
    }

    public Context getContext() {
        return context;
    }

    /** Libère les ressources. */
    public void close() {
        initialized = false;
        deviceParams = null;
        Log.i(TAG, "CardboardManager fermé");
    }
}