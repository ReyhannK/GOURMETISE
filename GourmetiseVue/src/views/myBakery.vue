<template>
    <v-container>
      <h1>Ma boulangerie</h1>
      <v-progress-circular
        v-if="loading"
        indeterminate
        color="primary"
        size="40"
        width="6"
        class="mx-auto"
      />
      <v-container v-if="modal && !loading">
        <v-alert type="error" v-if="modal" class="mb-4" :value="true">{{ modal }}</v-alert>
      </v-container>
      <v-container v-if="myRank && !loading">
        <v-card class="pa-10 mb-6" outlined max-width="1200px" width="100%">
            <v-col cols="12" md="12" class="d-flex align-center">
              <v-icon class="red--text" size="32">mdi-store</v-icon>
              <span class="ml-4"><strong>Ma boulangerie : </strong> {{ myRank.name }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <v-icon class="red--text" size="32">mdi-credit-card</v-icon>
              <span class="ml-4"><strong>N°siret : </strong>{{ myRank.siret }}</span>
            </v-col>
        
            <v-col cols="12" md="12" class="d-flex align-center">
              <v-icon class="red--text" size="32">mdi-star</v-icon>
              <span class="ml-4"><strong>Score moyen obtenu : </strong>{{ parseFloat(myRank.average_score).toFixed(2) }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <v-icon class="red--text" size="32">mdi-trophy</v-icon>
              <span class="ml-4"><strong>Rang : </strong>{{ myRank.rank_value }}</span>
            </v-col>
        </v-card>
      </v-container>
    </v-container>
  </template>
  
  <script setup>
  import api from "@/API/api.js";
  import { ref, onBeforeMount } from 'vue';
  
  const myRank = ref(null);
  const loading = ref(true);
  const modal = ref(false);
  
  onBeforeMount(() => {
    getMyRank();
  });
  
  async function getMyRank() {
    loading.value = true;
    try {
      const response = await api.get("/api/getMyRank");
      myRank.value = response.data.myRank;
      modal.value = response.data.message;
    } catch (error) {
      if (error.response && error.response.data.message) {
        modal.value = error.response.data.message;
      } else {
        modal.value = 'Erreur lors de l\'affichage des données de votre boulangerie.';
      }
    } finally {
      loading.value = false;
    }
  }
  </script>
  
  <style scoped>
  li {
    list-style-type: none;
  }
  
  h1 {
    text-align: center;
    margin: 40px 0;
  }
  
  .v-card {
    background-color: #f9f9f9;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
    width: 100%;
  }
  
  .v-row {
    margin-bottom: 30px;
  }
  
  .v-icon {
    color: goldenrod;
  }
  
  .v-col {
    display: flex;
    align-items: center;
  }
  
  .ml-4 {
    margin-left: 16px;
  }
  
  .v-alert {
    background-color: #ffebee;
    color: #d32f2f;
  }
  </style>
  