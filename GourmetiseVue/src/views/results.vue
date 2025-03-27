<template>
    <v-container>
        <h1>Classement du concours</h1>
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
        <v-container v-if="!loading">
            <v-row align="end" style="height: 150px;" no-gutters>
                <v-col v-for="(rank,index) in ranks" :key="index">
                    <v-sheet class="pa-2 ma-2 podium">
                        <v-img
                            :width="200"
                            cover
                            :src=getIcon(index)
                        />
                        <div class="score">
                          <span>{{ rank.name }}</span>
                          <span><strong>{{ parseFloat(rank.average_score).toFixed(2) }}</strong></span>
                        </div>
                    </v-sheet>
                </v-col>
          </v-row>
        </v-container>
    </v-container>
  </template>
  
  <script setup>
  import top2 from '@/assets/top2.png';
  import top1 from '@/assets/top1.png';
  import top3 from '@/assets/top3.png';
  import logo from '@/assets/logo.png';
  import { onBeforeMount, ref } from 'vue';
  import api from "@/API/api.js";

  const ranks = ref(null);
  const loading = ref(true);
  const modal = ref(false);

  onBeforeMount(()=>{
    getResults();
  });

  async function getResults()
  {
    loading.value = true;
    try{
      const response = await api.get("/api/getRankingTop3");
      ranks.value = response.data.rankings;
      modal.value = response.data.message;
    }catch(error){
      if (error.response && error.response.data.message) {
        modal.value = error.response.data.message;
      }
      else {
        modal.value = 'Erreur lors de l\'affichage du classement du concours.';
      }
    }finally{
      loading.value = false;
    }
  }

  const getIcon = (index) => {
    switch (index) {
      case 0:
        return top1;
      case 1:
        return top2;
      case 2:
        return top3;
      default:
          return logo;
    }
  };
  </script>
  
  <style scoped>
  li {
    list-style-type: none;
  }

  .score{
    display: flex;
    flex-direction: column;
  }

  .podium{
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  h1{
    text-align: center;
    margin: 40px 0 ;
  }


  </style>
  