package com.virtualmuseum.vr;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.util.Log;

import com.virtualmuseum.activities.CardboardManager;

/**
 * Vue OpenGL pour le rendu stéréoscopique VR.
 * Le renderer effectif sera connecté ici (mode stéréo, suivi tête).
 */
public class VrGlSurfaceView extends GLSurfaceView {

    private static final String TAG = "VrGlSurfaceView";

    private CardboardManager cardboardManager;
    private String modelUri;

    public VrGlSurfaceView(Context context) {
        super(context);
        init();
    }

    public VrGlSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        setEGLContextClientVersion(2);
        setEGLConfigChooser(8, 8, 8, 8, 16, 8);
    }

    /** Démarre le rendu VR avec le modèle 3D donné. */
    public void startVr(CardboardManager manager, String modelUri) {
        this.cardboardManager = manager;
        this.modelUri = modelUri;
        Log.i(TAG, "Démarrage rendu VR : " + modelUri);

        // TODO : brancher ici votre renderer OpenGL stéréoscopique
        // setRenderer(new VrStereoRenderer(getContext(), manager, modelUri));
    }

    public CardboardManager getCardboardManager() { return cardboardManager; }
    public String getModelUri() { return modelUri; }
}