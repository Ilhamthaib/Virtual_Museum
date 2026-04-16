package com.virtualmuseum.database;

import com.virtualmuseum.database.entities.ArtworkEntity;
import com.virtualmuseum.database.entities.CategoryEntity;

import java.util.Arrays;
import java.util.List;

/**
 * DatabaseSeeder — 50 œuvres d'Al-Andalus, 10 par catégorie.
 *
 * Images : app/src/main/assets/images/*.jpg
 *          URI format : "file:///android_asset/images/xxx.jpg"
 *
 * Audio  : app/src/main/res/raw/*.mp3
 *          On stocke uniquement le nom sans extension.
 *          Ex: "miniatures_jeux" → R.raw.miniatures_jeux
 *
 * ⚠️  Si tu modifies ce fichier après le premier lancement,
 *     incrémente la version dans AppDatabase.java puis ▶️ Run.
 */
public class DatabaseSeeder {

    private static final String IMG = "file:///android_asset/images/";
    // Audio → nom de ressource raw UNIQUEMENT (sans extension)
    private static final String MDL = "file:///android_asset/models/";

    public static void seed(AppDatabase db) {

        if (db.categoryDao().count() > 0) return;

        // ── Catégories ────────────────────────────────────────────────────────
        List<CategoryEntity> cats = Arrays.asList(
                new CategoryEntity("Peinture",      "Enluminures et miniatures andalouses",         "🎨", "#E3F2FD"),
                new CategoryEntity("Sculpture",     "Sculptures et objets ciselés d'Al-Andalus",    "🗿", "#F3E5F5"),
                new CategoryEntity("Architecture",  "Monuments et édifices d'Al-Andalus",           "🏛",  "#E8F5E9"),
                new CategoryEntity("Calligraphie",  "Art de l'écriture et manuscrits andalous",     "✍️",  "#FFF8E1"),
                new CategoryEntity("Arts du métal", "Bronzes, ivoires et objets précieux andalous", "⚙️",  "#FCE4EC")
        );
        db.categoryDao().insertAll(cats);

        List<CategoryEntity> saved = db.categoryDao().getAllSync();
        int catPeinture = 1, catSculpture = 1, catArchitecture = 1,
                catCalligraphie = 1, catMetal = 1;
        for (CategoryEntity c : saved) {
            switch (c.getName()) {
                case "Peinture":      catPeinture      = c.getId(); break;
                case "Sculpture":     catSculpture     = c.getId(); break;
                case "Architecture":  catArchitecture  = c.getId(); break;
                case "Calligraphie":  catCalligraphie  = c.getId(); break;
                case "Arts du métal": catMetal         = c.getId(); break;
            }
        }

        List<ArtworkEntity> artworks = Arrays.asList(

                // ═════════════════════════════════════════════════════════════════
                // PEINTURE
                // ═════════════════════════════════════════════════════════════════

                new ArtworkEntity(
                        "Miniatures du Livre des Jeux",
                        "Atelier d'Alphonse X le Sage", 1283,
                        "Recueil de miniatures illustrant les Cantigas de Santa Maria commandées " +
                                "par Alphonse X. Les enlumineurs andalous y représentent musiciens, " +
                                "joueurs d'échecs et scènes de cour avec une précision ethnographique exceptionnelle. " +
                                "Conservé à l'Escorial, Madrid.",
                        catPeinture,
                        IMG + "miniatures_jeux.jpg",
                        "miniatures_jeux",
                        null),

                new ArtworkEntity(
                        "Fresques du Palais de Medina Azahara",
                        "Artistes omeyyades de Cordoue", 950,
                        "Fragments de fresques géométriques et végétales découverts dans " +
                                "les appartements royaux de Madinat al-Zahra. Les pigments utilisés — " +
                                "ocre rouge, bleu égyptien, blanc de plomb — témoignent d'un savoir-faire " +
                                "hérité de l'art byzantin et sassanide.",
                        catPeinture,
                        IMG + "fresques_azahara.jpg",
                        "fresques_azahara",
                        null),

                new ArtworkEntity(
                        "Enluminures du Beatus de Gérone",
                        "Maître Ende (enlumineur)", 975,
                        "Manuscrit apocalyptique enluminé par la moniale Ende, l'une des rares " +
                                "femmes artistes citées par son nom au Moyen Âge. Son style mêle " +
                                "influences mozarabes et islamiques dans des compositions d'une intensité " +
                                "chromatique saisissante. Trésor national espagnol.",
                        catPeinture,
                        IMG + "beatus_gerone.jpg",
                        "beatus_gerone",
                        null),

                new ArtworkEntity(
                        "Peintures murales du Bain Royal de l'Alhambra",
                        "Artistes nasrides de Grenade", 1340,
                        "Cycle de peintures figuratives exceptionnel dans les bains royaux " +
                                "de l'Alhambra : cavaliers, musiciens et scènes de chasse peints " +
                                "à la détrempe sur les voûtes. Rarissime exemple de peinture figurative " +
                                "dans un contexte palatial islamique de la péninsule ibérique.",
                        catPeinture,
                        IMG + "peintures_bain_alhambra.jpg",
                        "peintures_bain_alhambra",
                        null),

                new ArtworkEntity(
                        "Miniatures du Traité de Chirurgie d'Al-Zahrawi",
                        "Copistes de Cordoue", 1000,
                        "Illustrations anatomiques et chirurgicales du Kitab al-Tasrif " +
                                "d'Abu al-Qasim al-Zahrawi, encyclopédie médicale en 30 volumes. " +
                                "Les dessins d'instruments — bistouris, cathéters, forceps — serviront " +
                                "de référence à la médecine européenne pendant cinq siècles.",
                        catPeinture,
                        IMG + "miniatures_zahrawi.jpg",
                        "miniatures_zahrawi",
                        null),

                new ArtworkEntity(
                        "Enluminures du Coran de la Bibliothèque de l'Escorial",
                        "Calligraphes et enlumineurs andalous", 1100,
                        "Coran andalou enluminé avec des frontispices en arabesques dorées " +
                                "d'une finesse extrême. Les entrelacs végétaux — le tawriq — atteignent " +
                                "ici une complexité mathématique qui préfigure les moucharabiehs " +
                                "architecturaux de l'époque nasride.",
                        catPeinture,
                        IMG + "coran_escorial.jpg",
                        "coran_escorial",
                        null),

                new ArtworkEntity(
                        "Diwan d'Ibn Quzman Illustré",
                        "Scribes de Séville", 1150,
                        "Recueil de muwashshah et zajal du poète cordouan Ibn Quzman, " +
                                "accompagné de vignettes représentant les scènes de la vie urbaine " +
                                "andalouse : tavernes, jardins, assemblées poétiques. " +
                                "Document unique sur la société d'Al-Andalus au XIIe siècle.",
                        catPeinture,
                        IMG + "diwan_ibn_quzman.jpg",
                        "diwan_ibn_quzman",
                        null),

                new ArtworkEntity(
                        "Peintures de la Salle des Ambassadeurs",
                        "Artistes nasrides", 1354,
                        "Décor peint des lambris inférieurs de la Salle du Trône de l'Alhambra, " +
                                "alternant médaillons héraldiques nasrides et motifs épigraphiques. " +
                                "Le programme iconographique traduit la devise royale 'Wa la ghalib illa Allah' " +
                                "— Nul vainqueur sinon Dieu.",
                        catPeinture,
                        IMG + "peintures_ambassadeurs.jpg",
                        "peintures_ambassadeurs",
                        null),

                new ArtworkEntity(
                        "Manuscrit Illustré d'Ibn al-Baitar",
                        "Ateliers de Malaga", 1240,
                        "Illustrations botaniques du traité pharmacologique d'Ibn al-Baitar, " +
                                "le plus grand herbier du Moyen Âge avec 1 400 plantes décrites. " +
                                "Les aquarelles représentent avec précision scientifique safran, " +
                                "henné, cannelle et centaines d'autres espèces médicinales.",
                        catPeinture,
                        IMG + "manuscrit_baitar.jpg",
                        "manuscrit_baitar",
                        null),

                new ArtworkEntity(
                        "Miniatures Astronomiques d'Al-Sufi",
                        "Copistes d'Al-Andalus", 1085,
                        "Copies andalouses du Kitab Suwar al-Kawakib (Livre des étoiles fixes) " +
                                "d'Abd al-Rahman al-Sufi. Les 48 constellations ptoléméennes y sont " +
                                "représentées sous forme de figures humaines et animales en miroir, " +
                                "selon la tradition islamique du 'double view' céleste.",
                        catPeinture,
                        IMG + "miniatures_sufi.jpg",
                        "miniatures_sufi",
                        null),

                // ═════════════════════════════════════════════════════════════════
                // SCULPTURE
                // ═════════════════════════════════════════════════════════════════

                new ArtworkEntity(
                        "Cerf en Bronze de Medina Azahara",
                        "Fondeurs omeyyades de Cordoue", 950,
                        "Fontaine zoomorphe en bronze représentant un cerf aux andouillers " +
                                "dressés, découverte dans les jardins du palais califal. " +
                                "L'eau s'écoulait par la bouche de l'animal dans un bassin de marbre. " +
                                "Chef-d'œuvre du bronzage omeyyade hispanique. Musée Archéologique de Cordoue.",
                        catSculpture,
                        IMG + "cerf_bronze.jpg",
                        "cerf_bronze",
                        null),

                new ArtworkEntity(
                        "Pyxide d'Al-Mughira",
                        "Maître Ibn Zaydan, Cordoue", 968,
                        "Coffret en ivoire ciselé offert par le calife Al-Hakam II à son fils " +
                                "Al-Mughira. Les quatre médaillons sculptés montrent des scènes palatiales : " +
                                "musiciens, fauconniers, cueillette de dattes et cavaliers combattants. " +
                                "Considérée comme le sommet de l'ivoire omeyyade. Musée du Louvre, Paris.",
                        catSculpture,
                        IMG + "pyxide_mughira.jpg",
                        "pyxide_mughira",
                        null),

                new ArtworkEntity(
                        "Chapiteaux à Feuilles d'Acanthe de l'Alhambra",
                        "Sculpteurs nasrides de Grenade", 1350,
                        "Série de chapiteaux en marbre blanc ornant les colonnes de la Cour " +
                                "des Lions. La corbeille à double registre — feuilles d'acanthe stylisées " +
                                "en bas, arabesques géométriques en haut — constitue le type canonique " +
                                "du chapiteau nasride, décliné en plus de cent exemplaires dans le palais.",
                        catSculpture,
                        IMG + "chapiteaux_alhambra.jpg",
                        "chapiteaux_alhambra",
                        null),

                new ArtworkEntity(
                        "Fontaine aux Lions de l'Alhambra",
                        "Sculpteurs nasrides", 1362,
                        "Vasque centrale portée par douze lions en marbre blanc d'où jaillissait " +
                                "l'eau selon un système hydraulique de précision. Les lions symbolisent " +
                                "les douze heures du jour et les douze mois de l'année. " +
                                "Le poème épigraphique de Ibn Zamrak gravé sur le bassin en décrit le prodige.",
                        catSculpture,
                        IMG + "fontaine_lions.jpg",
                        "fontaine_lions",
                        null),

                new ArtworkEntity(
                        "Pyxide de la Mosquée de Cordoue",
                        "Atelier califal de Cordoue", 960,
                        "Cylindre d'ivoire sculpté en bas-relief représentant des rinceaux " +
                                "de vigne habités — oiseaux affrontés, gazelles broutant, paons. " +
                                "Le couvercle est orné d'une palmette centrale rayonnante. " +
                                "Testament de la maîtrise andalouse du tournage et de la sculpture sur ivoire.",
                        catSculpture,
                        IMG + "pyxide_mosquee.jpg",
                        "pyxide_mosquee",
                        null),

                new ArtworkEntity(
                        "Stèle Funéraire du Cimetière de Chirinos",
                        "Lapicides de Grenade nasride", 1400,
                        "Stèle en marbre gris ornée d'une épitaphe en coufique andalou fleuri. " +
                                "Le registre supérieur montre une arche en fer à cheval entre deux colonnes, " +
                                "symbole paradisiaque récurrent dans l'épigraphie funéraire d'Al-Andalus. " +
                                "Musée de l'Alhambra, Grenade.",
                        catSculpture,
                        IMG + "stele_chirinos.jpg",
                        "stele_chirinos",
                        null),

                new ArtworkEntity(
                        "Aquamanile Zoomorphe de Séville",
                        "Ateliers almohades", 1180,
                        "Récipient en bronze coulé en forme de griffon ailé, utilisé pour " +
                                "les ablutions princières. Les ailes ajourées et la queue enroulée " +
                                "révèlent l'influence de l'art fatimide égyptien adopté et transformé " +
                                "par les artisans d'Al-Andalus sous les Almohades.",
                        catSculpture,
                        IMG + "aquamanile_seville.jpg",
                        "aquamanile_seville",
                        null),

                new ArtworkEntity(
                        "Chapiteau du Bain de Comares",
                        "Sculpteurs nasrides", 1333,
                        "Chapiteau monolithe en marbre de Macael orné d'épigraphie nasride " +
                                "sur l'abaque — 'wa la ghalib illa Allah' — et d'une corbeille " +
                                "à entrelacs géométriques sur huit faces. Exemplaire du Bain Royal " +
                                "de Comares, l'un des mieux conservés du palais.",
                        catSculpture,
                        IMG + "chapiteau_comares.jpg",
                        "chapiteau_comares",
                        null),

                new ArtworkEntity(
                        "Relief du Calendrier Agricole de Cordoue",
                        "Sculpteurs omeyyades", 970,
                        "Panneau en marbre sculpté illustrant les travaux des douze mois " +
                                "— labourage, vendange, cueillette des olives — dans un style " +
                                "mêlant naturalisme antique et schématisation islamique. " +
                                "Découvert lors des fouilles du palais de Medina Azahara.",
                        catSculpture,
                        IMG + "relief_calendrier.jpg",
                        "relief_calendrier",
                        null),

                new ArtworkEntity(
                        "Portail Sculpté de la Grande Mosquée d'Almería",
                        "Tailleurs de pierre andalous", 1012,
                        "Vantaux de pierre calcaire du portail septentrional ornés de " +
                                "tresses à six brins, rosaces à huit pointes et palmettes trilobées. " +
                                "Le programme décoratif est une transcription lapidaire du répertoire " +
                                "des stucs omeyyades, adapté à la dureté du matériau local.",
                        catSculpture,
                        IMG + "portail_almeria.jpg",
                        "portail_almeria",
                        null),

                // ═════════════════════════════════════════════════════════════════
                // ARCHITECTURE
                // ═════════════════════════════════════════════════════════════════

                new ArtworkEntity(
                        "Mosquée-Cathédrale de Cordoue",
                        "Abd al-Rahman I et successeurs", 785,
                        "La Grande Mosquée de Cordoue, fondée en 785, est agrandie par " +
                                "quatre califes successifs jusqu'en 987. Ses 856 colonnes de marbre " +
                                "et jaspe supportent une forêt d'arcs bicolores en fer à cheval. " +
                                "Le mihrab de al-Hakam II, incrusté de mosaïques byzantines, " +
                                "est considéré comme le plus beau du monde islamique occidental.",
                        catArchitecture,
                        IMG + "mosquee_cordoue.jpg",
                        "mosquee_cordoue",
                        MDL + "mosquee_cordoue.glb"),

                new ArtworkEntity(
                        "Palais de l'Alhambra",
                        "Sultans nasrides de Grenade", 1238,
                        "Complexe palatial nasride sur le Cerro de la Sabika dominant Grenade. " +
                                "L'Alhambra regroupe la Alcazaba militaire, les palais royaux " +
                                "(Comares, Lions, Partal) et le Generalife. Ses stucs ajourés, " +
                                "ses muqarnas en stalactites et ses jardins à canaux d'eau courante " +
                                "constituent le testament le plus accompli de l'architecture andalouse.",
                        catArchitecture,
                        IMG + "alhambra.jpg",
                        "alhambra",
                        MDL + "alhambra.glb"),

                new ArtworkEntity(
                        "Medina Azahara — Cité Palatiale",
                        "Abd al-Rahman III, Calife de Cordoue", 936,
                        "Ville-palais construite à flanc de Sierra Morena sur ordre du calife " +
                                "Abd al-Rahman III pour affirmer sa puissance face aux Fatimides. " +
                                "Dix mille ouvriers travaillèrent vingt-cinq ans à ériger ses terrasses, " +
                                "ses jardins, ses salles de réception aux panneaux de marbre sculptés. " +
                                "Ravagée en 1010, partiellement fouillée depuis 1911.",
                        catArchitecture,
                        IMG + "medina_azahara.jpg",
                        "medina_azahara",
                        MDL + "medina_azahara.glb"),

                new ArtworkEntity(
                        "La Giralda de Séville",
                        "Architecte Ahmad ibn Baso", 1198,
                        "Minaret de la Grande Mosquée almohade de Séville, le plus grand " +
                                "édifice islamique d'Al-Andalus. Sa surface extérieure est ornée " +
                                "de sebka — résille de losanges en brique — sur quatre faces. " +
                                "Transformée en clocher après la Reconquête, elle devient le symbole " +
                                "de Séville. Sa tour jumelle est la Koutoubia de Marrakech.",
                        catArchitecture,
                        IMG + "giralda.jpg",
                        "giralda",
                        null),

                new ArtworkEntity(
                        "Château de Baños de la Encina",
                        "Al-Hakam II, Calife omeyyade", 968,
                        "Forteresse militaire érigée sur un éperon rocheux en Jaén pour " +
                                "contrôler le passage de la Sierra Morena. Ses quatorze tours carrées " +
                                "en pisé calcaire sont reliées par un mur de 300 mètres. " +
                                "L'inscription fondatrice en coufique sur la tour-porte " +
                                "constitue un document épigraphique majeur de la Cordoue califale.",
                        catArchitecture,
                        IMG + "banos_encina.jpg",
                        "banos_encina",
                        null),

                new ArtworkEntity(
                        "Alcazar de Séville — Palais Mudéjar",
                        "Pedro I de Castille et maîtres grenadins", 1364,
                        "Pedro I fait construire son palais en faisant venir des artisans " +
                                "de Grenade nasride : le résultat est l'œuvre mudéjare la plus " +
                                "sophistiquée d'Espagne. Ses salles reproduisent fidèlement " +
                                "l'esthétique de l'Alhambra — stucs, azulejos, muqarnas — " +
                                "dans un palais chrétien. Résidence royale espagnole encore active.",
                        catArchitecture,
                        IMG + "alcazar_seville.jpg",
                        "alcazar_seville",
                        null),

                new ArtworkEntity(
                        "Bains Arabes de Ronda",
                        "Architectes nasrides", 1300,
                        "Hammam médiéval parmi les mieux conservés d'Al-Andalus, " +
                                "alimenté par la rivière Guadalevín via une roue hydraulique à aubes. " +
                                "Ses trois salles — froide, tiède, chaude — sont couvertes " +
                                "de voûtes en berceau percées d'étoiles à huit branches " +
                                "laissant filtrer la lumière zénithale.",
                        catArchitecture,
                        IMG + "bains_ronda.jpg",
                        "bains_ronda",
                        null),

                new ArtworkEntity(
                        "Minaret de San Juan de Córdoba",
                        "Artisans omeyyades", 900,
                        "Minaret de l'ancienne mosquée de l'Ajbar, aujourd'hui intégré " +
                                "dans l'église San Juan de los Caballeros. Sa décoration extérieure " +
                                "à arcatures aveugles et losanges en brique constitue le prototype " +
                                "du minaret hispano-maghrébin qui influencera la Giralda de Séville " +
                                "et la Tour Hassan de Rabat deux siècles plus tard.",
                        catArchitecture,
                        IMG + "minaret_san_juan.jpg",
                        "minaret_san_juan",
                        null),

                new ArtworkEntity(
                        "Pont Romain de Cordoue — Réaménagement Omeyyade",
                        "Ingénieurs du Califat de Cordoue", 719,
                        "Le Puente Romano de Cordoue, bâti sous Auguste, est entièrement " +
                                "reconstruit et orné par les omeyyades qui y ajoutent une tour de " +
                                "garde et une statue de Saint Raphaël recouverte d'inscriptions " +
                                "coraniques. Axe central de la Cordoue califale reliant " +
                                "la médina à la rive gauche du Guadalquivir.",
                        catArchitecture,
                        IMG + "pont_cordoue.jpg",
                        "pont_cordoue",
                        null),

                new ArtworkEntity(
                        "Generalife — Jardins du Plaisir",
                        "Architectes nasrides de Grenade", 1319,
                        "Résidence d'été des sultans nasrides dominant l'Alhambra. " +
                                "Le Patio de la Acequia — cour du canal — est le prototype du " +
                                "jardin paradisiaque islamique : allée d'eau centrale flanquée " +
                                "de jets obliques, cyprès, orangers et rosiers encadrant " +
                                "les reflets du palais. Sa géométrie de l'eau inspira les jardins " +
                                "de la Renaissance italienne.",
                        catArchitecture,
                        IMG + "generalife.jpg",
                        "generalife",
                        null),

                // ═════════════════════════════════════════════════════════════════
                // CALLIGRAPHIE
                // ═════════════════════════════════════════════════════════════════

                new ArtworkEntity(
                        "Coufique Floral de la Mosquée de Cordoue",
                        "Calligraphes du Califat", 965,
                        "Frise épigraphique du mihrab de al-Hakam II en mosaïque d'or " +
                                "et de lapis-lazuli. Le coufique andalou atteint ici sa forme la " +
                                "plus ornée : les hampes des lettres s'épanouissent en feuilles " +
                                "d'acanthe dorées, créant une écriture-jardin dont chaque verset " +
                                "coranique devient motif architectural.",
                        catCalligraphie,
                        IMG + "coufique_cordoue.jpg",
                        "coufique_cordoue",
                        null),

                new ArtworkEntity(
                        "Inscription Fondatrice de l'Alhambra",
                        "Calligraphes nasrides de Grenade", 1333,
                        "Tablette en marbre gravée par Muhammad I ibn al-Ahmar, fondateur " +
                                "de la dynastie nasride. Le texte en thuluth andalou combine " +
                                "l'éloge du sultan et la formule 'wa la ghalib illa Allah'. " +
                                "C'est la première occurrence connue de la devise qui deviendra " +
                                "l'âme épigraphique de tout le complexe de l'Alhambra.",
                        catCalligraphie,
                        IMG + "inscription_alhambra.jpg",
                        "inscription_alhambra",
                        null),

                new ArtworkEntity(
                        "Coran en Maghribi Andalou",
                        "Scriptorium de la Grande Mosquée de Cordoue", 1100,
                        "Coran en parchemin copié en écriture maghribi — variante " +
                                "andalouse aux hastes courbes et points diacritiques colorés. " +
                                "Ce style calligraphique, né à Cordoue au IXe siècle, " +
                                "est encore utilisé aujourd'hui au Maghreb et en Mauritanie. " +
                                "Bibliothèque nationale du Maroc, Rabat.",
                        catCalligraphie,
                        IMG + "coran_maghribi.jpg",
                        "coran_maghribi",
                        null),

                new ArtworkEntity(
                        "Poèmes d'Ibn Zamrak sur les Murs de l'Alhambra",
                        "Ibn Zamrak, poète-calligraphe nasride", 1370,
                        "Le vizir-poète Ibn Zamrak compose des qasidas que les artisans " +
                                "transcrivent en stuc sur les murs du Palais des Lions. " +
                                "C'est la seule fois dans l'histoire islamique qu'un poète signe " +
                                "littéralement son œuvre en l'intégrant à l'architecture. " +
                                "Washington Irving les qualifie de 'poésie vivante dans la pierre'.",
                        catCalligraphie,
                        IMG + "poemes_zamrak.jpg",
                        "poemes_zamrak",
                        null),

                new ArtworkEntity(
                        "Épitaphe du Sultan Muhammad V",
                        "Lapicides nasrides de Grenade", 1391,
                        "Stèle funéraire du sultan Muhammad V en marbre blanc de Macael, " +
                                "inscrite en thuluth nasride d'une régularité mathématique. " +
                                "La composition alterne cartouches rectangulaires et médaillons " +
                                "losangés selon un programme géométrique où écriture et ornement " +
                                "deviennent indiscernables. Musée de l'Alhambra.",
                        catCalligraphie,
                        IMG + "epitaphe_muhammad5.jpg",
                        "epitaphe_muhammad5",
                        null),

                new ArtworkEntity(
                        "Contrat de Vente en Papier de Jativa",
                        "Notaires andalous", 1163,
                        "Document juridique rédigé sur papier fabriqué à Jativa (Xàtiva), " +
                                "premier centre papetier d'Europe occidentale fondé par des artisans " +
                                "andalous. L'écriture cursive andalouse — variante du naskh — " +
                                "alterne formules coraniques d'ouverture et clauses contractuelles " +
                                "avec une élégance pragmatique caractéristique.",
                        catCalligraphie,
                        IMG + "contrat_jativa.jpg",
                        "contrat_jativa",
                        null),

                new ArtworkEntity(
                        "Azulejos Épigraphiques du Palais de Comares",
                        "Potiers et calligraphes nasrides", 1354,
                        "Bandeau de carreaux de faïence à reflets métalliques portant " +
                                "la formule nasride en coufique simple sur fond de rinceaux dorés. " +
                                "L'intégration de l'écriture dans le décor céramique — à mi-chemin " +
                                "entre calligraphie et architecture — est une invention originale " +
                                "des ateliers grenadins du XIVe siècle.",
                        catCalligraphie,
                        IMG + "azulejos_comares.jpg",
                        "azulejos_comares",
                        null),

                new ArtworkEntity(
                        "Diwan d'Ibn al-Khatib",
                        "Ibn al-Khatib, polymathe nasride", 1360,
                        "Recueil poétique autographe du grand vizir et lettré nasride " +
                                "Lisan al-Din ibn al-Khatib, copié de sa propre main en naskh " +
                                "andalou. Le manuscrit contient également des traités médicaux, " +
                                "historiques et mystiques, témoignant de l'idéal humaniste " +
                                "de l'intellectuel d'Al-Andalus à son crépuscule.",
                        catCalligraphie,
                        IMG + "diwan_khatib.jpg",
                        "diwan_khatib",
                        null),

                new ArtworkEntity(
                        "Inscription de la Porte de la Justice",
                        "Calligraphes de Yusuf I", 1348,
                        "Linteau de la Bab al-Shari'a — Porte de la Justice — de l'Alhambra " +
                                "portant une grande main en relief (les cinq piliers de l'Islam) " +
                                "et une inscription en thuluth monumentale. La porte est un " +
                                "manifeste architectural : entrer dans l'Alhambra c'est pénétrer " +
                                "dans un espace régi par la Loi divine.",
                        catCalligraphie,
                        IMG + "inscription_porte_justice.jpg",
                        "inscription_porte_justice",
                        null),

                new ArtworkEntity(
                        "Tablette Arithmétique d'Al-Qalasadi",
                        "Al-Qalasadi, mathématicien de Baza", 1460,
                        "Manuscrit mathématique d'Ali ibn Muhammad al-Qalasadi, " +
                                "l'un des derniers savants d'Al-Andalus. Il y introduit " +
                                "les premiers symboles algébriques connus en écriture arabe — " +
                                "précurseurs de la notation moderne. La calligraphie andalouse " +
                                "tardive y côtoie des diagrammes numériques d'une remarquable modernité.",
                        catCalligraphie,
                        IMG + "tablette_qalasadi.jpg",
                        "tablette_qalasadi",
                        null),

                // ═════════════════════════════════════════════════════════════════
                // ARTS DU MÉTAL
                // ═════════════════════════════════════════════════════════════════

                new ArtworkEntity(
                        "Pyxide au Nom d'Al-Hakam II",
                        "Atelier de Cordoue", 961,
                        "Coffret cylindrique en ivoire sculpté portant l'inscription " +
                                "'Pour al-Hakam, Commandeur des croyants'. Les panneaux sculptés " +
                                "représentent des scènes de chasse et de cour dans un ivoire " +
                                "d'éléphant d'Afrique d'une blancheur exceptionnelle. " +
                                "Musée National d'Art Hispanique, Madrid.",
                        catMetal,
                        IMG + "pyxide_hakam.jpg",
                        "pyxide_hakam",
                        null),

                new ArtworkEntity(
                        "Chandelier en Bronze Damasquiné",
                        "Bronziers de Cordoue", 1000,
                        "Chandelier à sept branches en bronze fondu et damasquiné d'argent, " +
                                "orné de frises de rinceaux et d'inscription en coufique simple. " +
                                "La technique du damasquinage — incrustations de fils précieux — " +
                                "est importée d'Orient et portée à son apogée par les ateliers " +
                                "omeyyades de Cordoue qui l'exportent vers toute la Méditerranée.",
                        catMetal,
                        IMG + "chandelier_bronze.jpg",
                        "chandelier_bronze",
                        null),

                new ArtworkEntity(
                        "Aiguière Omeyyade de la Collection Fortuny",
                        "Fondeurs andalous", 1010,
                        "Aiguière en bronze à bec verseur zoomorphe — tête de rapace — " +
                                "et anse en volute végétale. La panse sphérique est ciselée " +
                                "d'entrelacs et de médaillons animaux. Passage obligé de l'eau " +
                                "pour les ablutions princières, cet objet conjugue fonction " +
                                "rituelle et excellence esthétique.",
                        catMetal,
                        IMG + "aiguiere_fortuny.jpg",
                        "aiguiere_fortuny",
                        null),

                new ArtworkEntity(
                        "Coffret de Leyre",
                        "Maîtres ivoiriers de Cordoue", 1004,
                        "Coffret rectangulaire en ivoire aux parois sculptées de " +
                                "médaillons circulaires renfermant des scènes palatiales : " +
                                "banquet royal, combat de fauves, musicien au luth. " +
                                "Commandé par le hajib Abd al-Malik, il est l'un des " +
                                "derniers grands ivoires omeyyades avant la fitna de 1009. " +
                                "Musée de Navarre, Pampelune.",
                        catMetal,
                        IMG + "coffret_leyre.jpg",
                        "coffret_leyre",
                        null),

                new ArtworkEntity(
                        "Lampe de Mosquée en Laiton Ajouré",
                        "Artisans almohades de Séville", 1175,
                        "Lampe suspendue en laiton ajouré à décor de séka géométrique " +
                                "— résille d'étoiles à six et huit branches — laissant filtrer " +
                                "la lumière de la flamme intérieure. La structure octogonale " +
                                "de la cage métallique préfigure les moucharabiehs en bois " +
                                "qui feront la gloire des intérieurs nasrides.",
                        catMetal,
                        IMG + "lampe_laiton.jpg",
                        "lampe_laiton",
                        null),

                new ArtworkEntity(
                        "Miroir en Bronze Gravé de Pechina",
                        "Artisans de l'émirat de Cordoue", 880,
                        "Disque de bronze poli gravé au revers d'une palmette centrale " +
                                "entourée de six médaillons alternant rinceaux végétaux et " +
                                "inscriptions bénédictoires. Découvert dans le port d'Almería, " +
                                "il témoigne du commerce maritime qui connectait Al-Andalus " +
                                "à l'Ifriqiya et à l'Orient islamique.",
                        catMetal,
                        IMG + "miroir_pechina.jpg",
                        "miroir_pechina",
                        null),

                new ArtworkEntity(
                        "Trésor de la Mosquée de Cordoue — Candélabre",
                        "Orfèvres omeyyades", 976,
                        "Candélabre en argent niellé offert par al-Mansur à la Grande Mosquée " +
                                "lors de son agrandissement en 976. Les fûts sont torsadés et " +
                                "les plateaux gravés de la sourate du Trône. Fondu pendant la " +
                                "Reconquête, il n'est connu que par les descriptions " +
                                "enthousiastes des géographes arabes médiévaux.",
                        catMetal,
                        IMG + "candelabre_mosquee.jpg",
                        "candelabre_mosquee",
                        null),

                new ArtworkEntity(
                        "Clé de Grenade en Fer Forgé",
                        "Serruriers nasrides", 1400,
                        "Grande clé cérémonielle en fer forgé et damasquiné d'or, " +
                                "portant l'inscription 'Allah ouvre, Allah est vainqueur'. " +
                                "Les clés symboliques d'Al-Andalus, remises aux sultans lors " +
                                "des capitulations de villes, constituent un genre artistique " +
                                "propre à la péninsule ibérique médiévale. Musée de l'Alhambra.",
                        catMetal,
                        IMG + "cle_grenade.jpg",
                        "cle_grenade",
                        null),

                new ArtworkEntity(
                        "Écrin à Parfum en Or de Séville",
                        "Orfèvres almohades", 1200,
                        "Flacon à kohl en or repoussé et filigrane, orné de grenats " +
                                "et d'émeraudes en cabochon. La technique du filigrane — " +
                                "fils d'or torsadés soudés en motifs lacés — est l'une des " +
                                "grandes spécialités de l'orfèvrerie andalouse exportée " +
                                "vers le Maghreb et l'Italie médiévale.",
                        catMetal,
                        IMG + "ecrin_parfum.jpg",
                        "ecrin_parfum",
                        null),

                new ArtworkEntity(
                        "Coupe en Cristal de Roche Gravée",
                        "Cristalliers du Califat de Cordoue", 980,
                        "Coupe taillée dans un bloc unique de cristal de roche importé " +
                                "du Maghreb, gravée en creux de palmettes et de gazelles bondissantes. " +
                                "Le cristal de roche — al-ballur — était la matière la plus précieuse " +
                                "du trésor califal omeyyade de Cordoue, dont l'inventaire de 1010 " +
                                "répertorie plus de trois mille pièces.",
                        catMetal,
                        IMG + "coupe_cristal.jpg",
                        "coupe_cristal",
                        null)
        );

        db.artworkDao().insertAll(artworks);
    }
}