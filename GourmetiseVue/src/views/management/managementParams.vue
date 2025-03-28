<template>
    <v-container>
      <h1>Modifier les paramètres du concours</h1>
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
  
      <v-container v-if="!modal && !loading">
        <form @submit.prevent="submit" class="form-container">
          <v-row class="text-center" justify="center">
            
            <v-col cols="12" md="12">
              <div class="date-input-container">
                <label for="startRegistration">Date de début d'inscription</label>
                <input 
                  type="date" 
                  id="startRegistration" 
                  v-model="startRegistration" 
                  :min="minDate"
                />
              </div>
              <div class="date-input-container">
                <label for="endRegistration">Date de fin d'inscription</label>
                <input 
                  type="date" 
                  id="endRegistration" 
                  v-model="endRegistration" 
                  :min="startRegistration"
                />
              </div>
              <div class="date-input-container">
                <label for="startEvaluation">Date de début d'évaluation</label>
                <input 
                  type="date" 
                  id="startEvaluation" 
                  v-model="startEvaluation" 
                  :min="endRegistration"
                />
              </div>
              <div class="date-input-container">
                <label for="endEvaluation">Date de fin d'évaluation</label>
                <input 
                  type="date" 
                  id="endEvaluation" 
                  v-model="endEvaluation" 
                  :min="startEvaluation"
                />
              </div>
            </v-col>
          </v-row>
  
          <button class="submit-btn" type="submit" color="green">
            Valider
          </button>
        </form>
      </v-container>
    </v-container>
  </template>
  
  <script setup>
  import api from "@/API/api.js";
  import { onBeforeMount, ref } from 'vue';
  
  const modal = ref(false);
  const loading = ref(true);
  
  const startRegistration = ref();
  const endRegistration = ref();
  const startEvaluation = ref();
  const endEvaluation = ref();
  
  const minDate = new Date().toISOString().split("T")[0];
  
  onBeforeMount(() => {
    getContestParams();
  });
  
  async function getContestParams() {
    try {
      const response = await api.get("/api/contestParams");
      const params = response.data.contestParams;
      modal.value = response.data.message;
  
      startRegistration.value = params.startRegistration ? new Date(params.startRegistration).toISOString().split("T")[0] : null;
      endRegistration.value = params.endRegistration ? new Date(params.endRegistration).toISOString().split("T")[0] : null;
      startEvaluation.value = params.startEvaluation ? new Date(params.startEvaluation).toISOString().split("T")[0] : null;
      endEvaluation.value = params.endEvaluation ? new Date(params.endEvaluation).toISOString().split("T")[0] : null;
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
  
  async function submit() {
    console.log("submit");
  }
  </script>
  
  <style scoped>
  h1 {
    text-align: center;
    margin-top: 30px;
    margin-bottom: 50px;
    font-weight: 600;
  }
  
  .submit-btn {
    margin-top: 20px;
    padding: 10px 20px;
    background-color: #4CAF50;
    color: white;
    border: none;
    cursor: pointer;
  }
  
  .submit-btn:hover {
    background-color: #45a049;
  }
  
  .date-input-container {
    margin-bottom: 15px;
    display: flex;
  }
  
  label {
    display: block;
    margin-bottom: 5px;
  }
  
  input[type="date"] {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  </style>
  