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
          <p>{{ modal }}</p>
        </v-container>
    </v-container>
  </template>
  
  <script setup>
  import api from "@/API/api.js";
  import { ref  } from 'vue';

  const myRank = ref(null);
  const loading = ref(true);
  const modal = ref(false);

onBeforeMount(()=>{
    getMyRank();
  });

  async function getMyRank()
  {
    loading.value = true;
    try{
      const response = await api.get("/api/getMyRank");
      myRank.value = response.data.myRank;
      modal.value = response.data.message;
    }catch(error){
      if (error.response && error.response.data.message) {
        modal.value = error.response.data.message;
      }
      else {
        modal.value = 'Erreur lors de l\'affichage des données de votre boulangerie.';
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

  h1{
    text-align: center;
    margin: 40px 0 ;
  }


  </style>
  