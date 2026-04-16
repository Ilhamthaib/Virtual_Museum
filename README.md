# Virtual Museum — Android App

Projet académique — Module Développement Mobile & Metaverse
Pr. Latifa RASSAM — ENSIASD Taroudant — 2025/2026

## Prérequis

| Outil | Version minimale |
|---|---|
| Android Studio | Ladybug 2024.2+ |
| JDK | 17 (recommandé) |
| Android SDK | API 35 (compileSdk) |
| Émulateur / Téléphone | API 26 minimum (Android 8.0) |

## Lancer le projet

1. Ouvrir avec **Android Studio** → `File → Open → VirtualMuseum_Fixed`
2. Attendre la synchronisation Gradle (connexion internet requise)
3. Lancer sur un émulateur API 26+ avec le bouton ▶️

> ⚠️ Ne **pas** partager `local.properties` — Android Studio le génère automatiquement.

## Structure du projet

```
app/src/main/
├── assets/
│   ├── images/          ← Images des œuvres (JPEG)
│   └── audio/           ← Fichiers audio MP3 (remplacer par de vrais fichiers)
├── java/com/virtualmuseum/
│   ├── activities/       ← CategoriesActivity, ArtworkListActivity, etc.
│   ├── adapters/         ← ArtworkAdapter, CategoryAdapter
│   └── database/         ← Room DB, DAO, Seeder, ViewModel, Repository
└── res/                  ← Layouts, drawables, couleurs, strings
```

## Base de données (Room / SQLite)

- Créée automatiquement au **premier lancement**
- Pré-remplie par `DatabaseSeeder.java` (10 œuvres, 5 catégories)
- Pour réinitialiser : désinstaller l'app sur l'émulateur puis relancer

## Audio Guide

- Le bouton **Audio Guide** utilise **TextToSpeech** (voix Android) comme fallback
- Pour de vrais fichiers audio : remplacer les MP3 dans `assets/audio/`
- Format d'URI : `file:///android_asset/audio/nom_fichier.mp3`

## Fonctionnalités implémentées (MVP)

- ✅ F01 — Liste + détails des œuvres (navigation list → details)
- ✅ F02 — Favoris persistants (ajout/retrait avec Room)
- ✅ F03 — Recherche par titre ou artiste (live filtering)
- ✅ F04 — Persistance Room/SQLite
- ✅ Audio Guide (TTS + MP3 assets)
- ⬜ Extension Metaverse AR (squelette ArViewerActivity prêt)

## Corrections appliquées

1. **AndroidManifest** — `uses-feature android:name="android.hardware.camera"` ajouté
2. **AndroidManifest** — `screenOrientation` supprimé (ignoré depuis Android 16)
3. **MuseumViewModel** — `repo` initialisé avant `artworksByCategory` (fix compilation)
4. **gradle.properties** — RAM réduite à 1024m pour compatibilité
5. **local.properties** — supprimé du ZIP (chemin machine-spécifique)
