<template>
    <v-container>
      <h1>Les paramètres du concours</h1>
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
        <v-container v-if="params && !loading">
          <v-card class="pa-10 mb-6" outlined max-width="1200px" width="100%">
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>Titre : </strong> {{ params.title }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>La description : </strong>{{ params.description }}</span>
            </v-col>
        
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>Date de début d'inscription : </strong>{{ formatDate(params.startRegistration) }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>Date de fin d'inscription : </strong>{{ formatDate(params.endRegistration) }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>Date de début d'évaluation : </strong>{{ formatDate(params.startEvaluation) }}</span>
            </v-col>
            <v-col cols="12" md="12" class="d-flex align-center">
              <span class="ml-4"><strong>Date de fin d'évaluation : </strong>{{ formatDate(params.endEvaluation) }}</span>
            </v-col>
          </v-card>
        </v-container>
    </v-container>
  </template>
  
  <script setup>
  import { onBeforeMount, ref } from 'vue';
  import api from "@/API/api.js";
  import formatDate from '@/tools/dateUtils.js';

  const params = ref();
  const loading = ref(true);
  const modal = ref(false);

  onBeforeMount(()=>{
    getContestParams();
  });

  async function getContestParams()
  {
    try{
        const response = await api.get("/api/contestParams");
        params.value = response.data.contestParams;
        modal.value = response.data.message;
        console.log(params.value);
    }catch(error){
        if (error.response && error.response.data.message) {
          modal.value = error.response.data.message;
        }
        else {
          modal.value = 'Erreur lors de l\'affichage des paramètres du concours.';
        }
    }finally{
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
    margin: 20px 0;
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
  