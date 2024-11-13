<template>
  <v-container>
    <v-row class="text-center my-12" justify="center">
      <v-col cols="12">
        <img alt="Logo La Gourmetise" class="logo" src="../assets/logo.png"/>
        <h1>Bienvenue au Concours de la Meilleure Boulangerie</h1>
        <p>
          Participez au concours de la meilleure boulangerie organisé par La Gourmetise
          et découvrez les talents de la boulangerie dans votre région.
        </p>
      </v-col>
    </v-row>
    <v-row class="text-center" justify="center">
      <v-col cols="12" md="6">
        <v-card outlined>
          <v-card-title class="headline">Participez en tant que boulanger</v-card-title>
          <v-card-text>
            Inscrivez votre boulangerie pour tenter de remporter le titre de la meilleure boulangerie.
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" to="/sign-in">S'inscrire</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
      <v-col cols="12" md="6">
        <v-card outlined>
          <v-card-title class="headline"> Résultats du concours</v-card-title>
          <v-card-text>
            Voir les résultats des concours de la meilleure boulangerie
          </v-card-text>
          <v-card-actions>
            <v-btn color="primary" to="/results">Afficher Résultats</v-btn>
          </v-card-actions>
        </v-card>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
</script>

<style scoped>
.my-12 {
  margin-top: 3rem;
  margin-bottom: 3rem;
}
</style>
