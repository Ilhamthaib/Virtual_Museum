package com.virtualmuseum.vr;

import android.content.Context;
import android.opengl.GLES30;
import android.opengl.GLSurfaceView;
import android.util.Log;

import com.virtualmuseum.activities.CardboardManager;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * Renderer stéréoscopique pour le mode VR Cardboard.
 * Rend la scène deux fois (œil gauche / droit) sur la même surface OpenGL.
 */
public class VrRenderer implements GLSurfaceView.Renderer {

    private static final String TAG = "VrRenderer";

    private final Context context;
    private final CardboardManager cardboardManager;
    private final String modelUri;

    private int surfaceWidth;
    private int surfaceHeight;

    public VrRenderer(Context context, CardboardManager manager, String modelUri) {
        this.context = context;
        this.cardboardManager = manager;
        this.modelUri = modelUri;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES30.glClearColor(0.05f, 0.05f, 0.1f, 1.0f);
        GLES30.glEnable(GLES30.GL_DEPTH_TEST);
        GLES30.glEnable(GLES30.GL_CULL_FACE);

        Log.i(TAG, "Surface VR créée — modèle : " + modelUri);

        // TODO : initialiser ici le loader GLB / les shaders / les buffers
        // Exemples d'options : Filament, Sceneform (déprécié), tinygltf via JNI
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        this.surfaceWidth = width;
        this.surfaceHeight = height;
        GLES30.glViewport(0, 0, width, height);
        Log.i(TAG, "Surface VR redimensionnée : " + width + "x" + height);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT | GLES30.GL_DEPTH_BUFFER_BIT);

        int halfWidth = surfaceWidth / 2;

        // ── Œil gauche ──
        GLES30.glViewport(0, 0, halfWidth, surfaceHeight);
        renderEye(true);

        // ── Œil droit ──
        GLES30.glViewport(halfWidth, 0, halfWidth, surfaceHeight);
        renderEye(false);
    }

    /**
     * Rendu d'un œil (gauche ou droit).
     * Appliquez ici la matrice de projection adaptée + offset latéral
     * pour simuler la séparation inter-pupillaire.
     */
    private void renderEye(boolean isLeftEye) {
        // TODO : appliquer la matrice de projection stéréo
        //  - matrice de vue avec offset (-IPD/2 ou +IPD/2 sur l'axe X)
        //  - matrice de projection en perspective
        //  - draw call de votre modèle 3D
        //
        // Pour brancher la distorsion barillet Cardboard plus tard,
        // il faudra rendre dans un FBO puis appliquer un shader de distorsion.
    }
}