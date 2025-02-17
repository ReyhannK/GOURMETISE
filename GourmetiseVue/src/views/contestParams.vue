<template>
    <v-container>
      <li v-for="(value, key) in params" :key="key">
        <strong>{{ key }}:</strong> {{ value }}
      </li>
    </v-container>
  </template>
  
  <script setup>
  import axios from 'axios';
  import { onMounted, ref } from 'vue';
  import { jwtDecode } from "jwt-decode";

  const params = ref();

  async function getContestParams()
  {
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

    try{
        const response = await axios.get(import.meta.env.VITE_API_URL + "/api/contestParams", {
          headers: {
            Authorization: `Bearer ${token}`,
          }
        });
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
  