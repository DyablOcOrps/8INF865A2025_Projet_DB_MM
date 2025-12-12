Baptiste DUBOIS et Maxime MEROT - Groupe 1

---

*MiArte est une application Android native développée en Kotlin avec Jetpack Compose.  
Elle permet aux utilisateurs de partager leurs œuvres d'art, de découvrir celles des autres, et d'entrer en contact pour des achats potentiels.*

# Structure de l'application : 
Gradle Version : 8.13  
Minimum SDK : API 24 ("Nougat"; Android 7.0)  
Target SDK : API 36 ("Baklava"; Android 16.0)  
Kotlin JVM : 11

## Dossier app/kotlin+java/com.example.miarte
<ins>*Dossier model*</ins> : Les données brutes.
- Art : Définit une œuvre (titre, prix, url image, auteur...). L'ID est généré par Firestore.
- Category : Gestion des filtres (Peinture, Dessin, etc.).

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

<ins>*Fichier viewmodel/MiArteViewModel*</ins> : Le plus gros fichiers, contient toute la logique de l'application (appels Firebase, gestion des états, flux de données StateFlow).

---

# Utilisation de Firebase :
L'application repose sur l'écosystème Firebase.

<ins>*Firebase Authentication*</ins> : Permet de réaliser un système d'authentification et d'inscription simple grâce à firebase (Google). Sur la console on peut trouver la liste des identifiant utilisé.   
<ins>*Firebase Storage*</ins> : Stockage des fichiers images des oeuvres.  
<ins>*Firebase Firestore*</ins> : Base de données NoSQL stockant :  
    - Fiches des oeuvres (collection arts)  
    - Les profils utilisateurs (collection users)  

## Règles de sécurité 
- arts : Lecture publique, Ecriture authentifiée uniquement
- users : Lecture/Ecriture uniquement si L'UID correspond à l'utilisateur
  
---

# Piste d'amélioration pour l'avenir : 
- Implémenter un système de messagerie
- Améliorer le système d'authentification en vérifiant que c'est un mail valide
- Implémenter le payement -> Sûrement compliqué dans le cadre légal d'un cours d'où la non-implémentation dans le projet actuel.
- Ranger le ViewModel en plusieurs petits ViewModel pour améliorer la clarté du code
- Implémenter un système de messagerie pour que les utilisateurs puissent échanger entre eux 
