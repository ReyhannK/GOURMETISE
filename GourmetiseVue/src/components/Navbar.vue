<template>
  <v-app-bar app color="red" dark>
    <v-row align="center" class="flex-grow-1">
      <v-col class="d-flex" cols="auto">
        <img src="../assets/logo.png" alt="Logo La Gourmetise" class="logo" />
      </v-col>

      <v-col class="d-flex" cols="auto">
        <v-app-bar-title>La Gourmetise - Concours</v-app-bar-title>
      </v-col>
    </v-row>

    <v-spacer></v-spacer>

    <v-row align="center" class="d-flex">
      <v-btn>
        <router-link to="/" class="router-link">
          Accueil
        </router-link>
      </v-btn>
      <v-btn>
        <router-link to="/sign-in" class="router-link">
          S'inscrire
        </router-link>
      </v-btn>
      <v-btn>
        <router-link to="/results" class="router-link">
          Afficher résultats
        </router-link>
      </v-btn>
    </v-row>

    <v-spacer></v-spacer>

    <div class="mr">
      <v-btn icon @click="logout">
        <v-icon>mdi-logout</v-icon>
    </v-btn>
    </div>
  </v-app-bar>
</template>

<script setup>
  import { useRouter } from 'vue-router';

  const router = useRouter();

  const logout = () =>{
    localStorage.removeItem('token');
    router.push('/login');
  };
  
</script>

<style scoped>
a {
  text-decoration: none;
}

.router-link {
  color: white;
  text-decoration: none;
}

img {
  max-height: 65px;
}

.mr {
  margin-right: 50px;
}
</style>
