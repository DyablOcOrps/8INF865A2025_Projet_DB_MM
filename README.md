Baptiste DUBOIS et Maxime MEROT - Groupe 1

---

# Structure de l'application : 

## Dossier app/kotlin+java/com.example.miarte
<ins>*Dossier model*</ins> : Fichiers avec des datas class utiles au projet
- Art : Classe Art, on retrouve la dednas tout ce qui caractérise une oeuvre d'art (auteur, titre, ...). Chaque oeuvre sera distingué par son ID.
- Category : Classe Category, permet de créer des catégories avec un ID et son nom.

<ins>*Fichier Navigation/AppNavigation*</ins> : Fichier pour gérer la navigation entre les pages de notre application grâce aux outils NavHost, NavController et compose.

<ins>*Dossier UI/components/*</ins> : Composants UI de base de l'application
- BaseScreen.kt : On y retrouve la base de la page qui reste la même dans toutes l'application. 
- TopBar.kt : fonctionnement de la Top Bar qui varie en fonction de la page en cours (Changement des boutons affichées en fonction de la page en cours).

<ins>*Dossier UI/screens/*</ins> : Ensemble des pages (et leur fonctionnement graphique) 
- AddArtScreen.kt : Page pour ajouter une nouvelle oeuvre
- AuthentificationScreen.kt : Page pour s'authentifier avec un compte existant dans la base de données
- DescriptionScreen.kt : Page pour la description de l'oeuvre, si c'est notre oeuvre on peut la supprimer sinon on peut l'acheter
- HomeScreen.kt : Page d'acceuil, on y voit les oeuvres de tout le monde sous différentes catégories
- MyArtsScreen.kt : Page pour voir la liste que l'utilisateur authentifié a partagé
- RegisterScreen.kt : Page pour s'inscrire (créer un nouveau compte dans la base de données)
- SettingsScreen.kt : Page des paramètres (déconnexion et suppression)

<ins>*Fichier viewmodel/MiArteViewModel*</ins> : Le plus gros fichiers, contient toute la logique de l'application

---

# Utilisation de Firebase :

