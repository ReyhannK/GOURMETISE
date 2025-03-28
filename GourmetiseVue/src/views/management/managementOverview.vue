<template>
    <v-container>
      <h1>Gestion du concours</h1>
      <v-row class="text-center" justify="center">
        <v-col cols="12" md="6">
          <v-card 
            class="card-item" 
            @click="goToModifyParams"
            elevation="2"
            outlined
            hover
          >
            <v-card-title class="headline">
              <v-icon left>mdi-cogs</v-icon> 
              Modifier les paramètres du concours
            </v-card-title>
          </v-card>
        </v-col>
        <v-col cols="12" md="6">
          <v-card 
            class="card-item" 
            @click="goToPublishRanking"
            elevation="2"
            outlined
            hover
          >
            <v-card-title class="headline">
              <v-icon left>mdi-trophy</v-icon>
              Publier le classement du concours
            </v-card-title>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </template>
  
  <script setup>
  import { useRouter } from 'vue-router';
  
  const router = useRouter();
  
  function goToModifyParams() {
    router.push({ name: 'ManagementParams' });
  }
  
  function goToPublishRanking() {
    router.push({ name: 'ManagementGenerate' });
  }
  </script>
  
  <style scoped>
  h1 {
    text-align: center;
    margin-top: 30px;
    margin-bottom: 50px;
    font-weight: 600;
  }
  
  .card-item {
    transition: all 0.3s ease;
    cursor: pointer;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
  }
  
  .card-item:hover {
    background-color: #e3f2fd;
    transform: translateY(-3px); 
  }
  
  .card-item .v-card-title {
    font-size: 1.3rem;
    font-weight: 600;
    text-align: left;
    color: #333;
    display: flex;
    align-items: center;
  }
  
  .v-icon {
    margin-right: 10px;
    font-size: 2rem;
    color: #1e88e5;
  }
  
  .v-card {
    border-radius: 8px;
  }
  
  .v-card-title {
    margin: 0;
    padding: 0;
  }
  
  .v-card--elevation-2 {
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
  }
  </style>
  