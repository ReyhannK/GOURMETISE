<template>
    <v-container class="container">
        <h1>Se connecter</h1>
        <form @submit.prevent="submit" class="form-container">
            <v-text-field 
                v-model="email"
                label="Email"
                type="email"
                clearable
                required
                class="form-field"
                prepend-icon="mdi-email"
            >
            </v-text-field>

            <v-text-field 
                v-model="password"
                label="Mot de passe"
                type="password"
                clearable
                required
                class="form-field"
                prepend-icon="mdi-lock"
            >
            </v-text-field>

            <div class="d-flex justify-start mb-4">
                <p>Vous n'avez pas de compte ? 
                    <router-link to="/create-account">Créer un compte ici</router-link>
                </p>
            </div>

            <div class="d-flex justify-end">
                <v-btn class="me-4 submit-btn" type="submit" color="green">
                    Se connecter
                </v-btn>
            </div>
        </form>

        <div class="text-center pa-4">
            <v-dialog v-model="message" width="auto">
                <v-card max-width="400" prepend-icon="mdi-information" :text="message" title="Infomation">
                    <template v-slot:actions>
                        <v-btn class="ms-auto" text="Ok" @click="message = null;" color="black"></v-btn>
                    </template>
                </v-card>
            </v-dialog>
        </div>
    </v-container>
</template>
  
<script setup>
  import api from '@/API/api';
  import { useRouter } from 'vue-router';
  import { ref } from 'vue';

  const router = useRouter();

  const email = ref();
  const password = ref();

  const message = ref();
  const token = ref();

  async function submit()
  {
    try{
        const user = {
            "email": email.value,
            "password": password.value
        }
        
        const response = await api.post("/api/login", user);
        console.log(response.data);

        message.value = response.data.message;
        token.value = response.data.token;
        
        localStorage.setItem('token', token.value);
        await router.push('/');
    }catch(error){
        console.log(error.response);
        if (error.response && error.response.data.message) {
            message.value = error.response.data.message;
        }
        else {
            message.value = 'Erreur lors de la connexion';
        }
        console.log(message.value);
    }
  }
</script>
  
<style scoped>
.container{
    display: flex;
    justify-content: center; 
    align-items: center;    
    min-height: 70vh;  
    flex-direction: column; 
}

h1 {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.form-container {
  max-width: 600px;
}

.form-field {
  margin-bottom: 8px;
  width: 300px;
}
</style>
  