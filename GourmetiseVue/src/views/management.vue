<template>
    <v-container>
      <div class="container">
        <h1>Générer le classement du concours</h1>
        <v-progress-circular
          v-if="loading"
          indeterminate
          color="primary"
          size="40"
          width="6"
          class="mx-auto"
        />
        <v-container v-if="published && !loading">
          <h2>Le classement du concours est publié ! <v-icon color="green" size="36">mdi-check-circle</v-icon></h2>
          <p>Les visiteurs et participants peuvent visualiser les résultats du concours !</p>
        </v-container>

        <div v-if="!published && !generated && !loading">
          <p>Générer le classement du concours afin d'avoir un aperçu du classement
            pour ensuite pouvoir le publier.
          </p>
          <v-btn color="red" rounded="xs" @click="getResult(); generated = true;">
            Générer
          </v-btn>
        </div>
        <v-container>
          <div v-if="generated && !published && !loading">
            <p>Publier le classement du concours afin que les participants et visiteurs peuvent le consulter.
            </p>
            <v-btn color="red" rounded="xs" @click="publishedContest()">
              Publier le classement
            </v-btn>
          </div>
          <div v-if="(generated || published) && !loading" class="ranking-table-container">
            <table class="ranking-table">
            <thead>
              <tr>
                <th>Siret</th>
                <th>Nom</th>
                <th>Moyenne générale</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(rank,index) in ranks" :key="index">
                <td>{{ rank.siret }}</td>
                <td>{{ rank.name }}</td>
                <td>{{ rank.average_score ? parseFloat(rank.average_score).toFixed(2) : 'N/A' }}</td>
              </tr>
            </tbody>
          </table>
          </div>
        </v-container>
      </div>
    </v-container>
    <div class="text-center pa-4">
      <v-dialog v-model="modal" width="auto">
        <v-card max-width="400" prepend-icon="mdi-information" :text="modal" title="Infomation">
          <template v-slot:actions>
            <v-btn class="ms-auto" text="Ok" @click="modal = null;" color="black"></v-btn>
          </template>
        </v-card>
      </v-dialog>
    </div>
  </template>
  
  <script setup>
  import { onBeforeMount,ref } from 'vue'
  import api from "@/API/api.js";

  const loading = ref(true); 
  const generated = ref(false);
  const published = ref(false);
  const modal = ref(false);
  const ranks = ref('');

  onBeforeMount(()=>{
    getContestParams();
  });

  async function getContestParams()
  {
    try{
        const response = await api.get("/api/contestParams");
        published.value = response.data.published;
        if(published.value){
          getResult();
        }
    }catch(error){
        console.log(error)
    }finally{
      loading.value = false;
    }
  }

  async function getResult() {
    loading.value = true;
    try{
      const response = await api.get("/api/getFullRanking");
      ranks.value = response.data.rankings;
      modal.value = response.data.message;
    }catch(error){
      if (error.response && error.response.data.message) {
        modal.value = error.response.data.message;
      }
      else {
        modal.value = 'Erreur lors de la génération du classement du concours.';
      }
    }finally{
      loading.value = false;
    }
  };

  async function publishedContest() {
    loading.value = true;
    try{
      const contestParams = {
      "published": true
    }
      const response = await api.patch("/api/contestParams", contestParams);
      modal.value = response.data.message;
      published.value = true;
    }catch(error){
      if (error.response && error.response.data.message) {
        modal.value = error.response.data.message;
      }
      else {
        modal.value = 'Erreur lors de la publication du classement du concours.';
      }
    }finally{
      loading.value = false;
    }
  };
  </script>
  
  <style scoped>
  .container{
    margin-top: 30px;
  }

  h1{
    text-align: center;
    font-weight: 600;
    margin-bottom: 20px;
  }

  .ranking-table-container {
  width: 80%;
  margin: 0 auto;
  padding: 10px;
}

.ranking-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

.ranking-table th, .ranking-table td {
  padding: 12px 15px;
  text-align: left;
  border: 1px solid #ddd;
}

.ranking-table th {
  background-color: #333333;
  color: white;
  font-weight: bold;
}

.ranking-table tr:nth-child(even) {
  background-color: #f2f2f2;
}

.ranking-table tr:hover {
  background-color: #ddd;
}

.ranking-table td {
  color: #333;
  font-size: 14px;
}

  </style>
  