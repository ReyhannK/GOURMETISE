<template>
    <v-container>
      <li v-for="(value, key) in params" :key="key">
        <strong>{{ key }}:</strong> {{ value }}
      </li>
    </v-container>
  </template>
  
  <script setup>
  import { onMounted, ref } from 'vue';
  import api from "@/API/api.js";

  const params = ref();

  async function getContestParams()
  {
    try{
        const response = await api.get("/api/contestParams");
        params.value = response.data;
    }catch(error){
        console.log(error)
    }
  }

  onMounted(()=>{
    getContestParams();
  });
  </script>
  
  <style scoped>
  li {
    list-style-type: none;
  }

  </style>
  