<template>
  <v-container>
    <v-row class="text-center my-12" justify="center">
      <v-col cols="12">
        <img alt="Logo La Gourmetise" class="logo" src="../assets/logo.png" />
        <h1>Bienvenue au Concours de la Meilleure Boulangerie</h1>
        <p>
          Participez au concours de la meilleure boulangerie organisé par La Gourmetise
          et découvrez les talents de la boulangerie dans votre région.
        </p>
        <v-btn class="mt-10" color="red" to="/contestParams">En savoir plus</v-btn>
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
            <v-btn color="primary" :disabled="!isOpen" to="/sign-in-bakery">S'inscrire</v-btn>
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
import axios from 'axios';
import { onMounted, ref } from 'vue';
import { jwtDecode } from "jwt-decode";

const params = ref();
const isOpen = ref('');

async function getContestParams() {
    const token = localStorage.getItem('token');
    if (!token) {
          console.error('Token non trouvé dans le localStorage');
          return;
    }

    let decodedToken = null;
    try {
      decodedToken = jwtDecode(token);
    } catch (error) {
      console.error('Erreur lors du décodage du token:', error);
      return;
    }
  
  try {
    const response = await axios.get(import.meta.env.VITE_API_URL + "/api/contestParams");
    params.value = response.data;
    isOpen.value = isContestOpen();
  } catch (error) {
    console.log(error)
  }
}

const isContestOpen = () => {
  const startRegistrationDate = new Date(params.value?.startRegistration);
  const endRegistrationDate = new Date(params.value?.endRegistration);
  const currentDateObj = new Date();
  //console.log("startRegistrationDate", startRegistrationDate);
  //console.log("endRegistrationDate", endRegistrationDate);
  //console.log("currentDateObj", currentDateObj);
  return currentDateObj >= startRegistrationDate && currentDateObj <= endRegistrationDate;
}

onMounted(() => {
  getContestParams();
});
</script>

<style scoped>
.my-12 {
  margin-top: 3rem;
  margin-bottom: 3rem;
}

.mt-10 {
  margin-top: 2rem;
}
</style>
