# Gourmetise2024
Projet Gourmetise SIO2024

- **Structure du projet**  
    - Doc : documentation du projet (Diagramme de classes,...)
    - GourmetiseAPI : API du projet crééé avec Symfony
    - GourmetiseVue : application web


- **GourmetiseAPI**
    - Pré-requis : PHP 8.3.10 / Composer ...
    - Configuration de la BDD (src\.env) :  
    	- *DATABASE_URL="mysql://root:@127.0.0.1:3306/gourmetise_db?serverVersion=5.7.33&charset=utf8mb4"*
    - Créer la base de données :  
    	- exécuter la commande *php bin/console doctrine:database:create*
    - Exécuter la migration :  
    	- exécuter la commande *php bin/console make:migration*
      

- **GourmetiseVue**
    - Pré-requis : Node 22.11.0 / Npm 10.9.0
    - installer les dépendances :  
        - exécuter la commande *npm install*
    - Configuration de l'API :
      - Créer un fichier *.env* à la racine du projet
      - Ajouter la ligne suivante : *VITE_API_URL=[URL_API]*
    - Lancer le serveur de développement :
        - exécuter la commande *npm run dev*
