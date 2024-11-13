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
      
