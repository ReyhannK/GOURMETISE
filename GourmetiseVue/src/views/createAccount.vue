<template>
    <v-container class="container">
        <h1>Créer un compte</h1>
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
                :rules="[passwordRule]"
                :error-messages="passwordErrorMessages"
            >
            </v-text-field>

            <div class="d-flex justify-start mb-4">
                <p>Vous avez un compte ? 
                    <router-link to="/login">Connectez-vous ici</router-link>
                </p>
            </div>

            <div class="d-flex justify-end">
                <v-btn class="me-4 submit-btn" type="submit" color="green">
                    Créer
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
  import useAuthStore from '@/stores/authStore';

  const router = useRouter();

  const authStore = useAuthStore();

  const email = ref();
  const password = ref();
  const message = ref();
  const passwordErrorMessages = ref([]);

  const passwordRule = value => {
    const regex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]{10,}$/;
    if (!value) {
      passwordErrorMessages.value = ['Le mot de passe est requis.'];
      return false;
    }
    if (!regex.test(value)) {
      passwordErrorMessages.value = ['Le mot de passe doit contenir au moins 10 caractères, une majuscule, une minuscule, un chiffre et un caractère spécial.'];
      return false;
    }
    passwordErrorMessages.value = [];s
    return true;
  };

  async function submit()
  {
    try{
        const newUser = {
            "email": email.value,
            "password": password.value
        }
        const response = await api.post("/api/register", newUser);

        message.value = response.data.message;
        const token = response.data.token;
    
        authStore.login(token);
        
        router.push('/');
    }catch(error){
        if (error.response && error.response.data.message) {
            message.value = error.response.data.message;
        }
        else {
            message.value = 'Erreur lors de la création de compte';
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
  