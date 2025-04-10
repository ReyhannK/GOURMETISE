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

        <div class="submit-container">
          <button class="submit-btn" type="submit">
            Valider
          </button>
        </div>
      </form>
    </v-container>

    <div class="text-center pa-4">
      <v-dialog v-model="isMessage" width="auto">
        <v-card max-width="400" prepend-icon="mdi-information" :text="message" title="Infomation">
          <template v-slot:actions>
            <v-btn class="ms-auto" text="Ok" @click="isMessage = null; message = '';" color="black"></v-btn>
          </template>
        </v-card>
      </v-dialog>
    </div>
  </v-container>
</template>

<script setup>
import api from "@/API/api.js";
import { onBeforeMount, ref, watch } from 'vue';

const modal = ref(false);
const message = ref('');
const isMessage = ref(false);
const loading = ref(true);

const startRegistration = ref();
const endRegistration = ref();
const startEvaluation = ref();
const endEvaluation = ref();

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
      modal.value = 'Erreur lors de l\'affichage des données du concours.';
    }
  } finally {
    loading.value = false;
  }
}

async function submit() {
  loading.value = true;
  try {
    const data = {
      startRegistration: startRegistration.value,
      endRegistration: endRegistration.value,
      startEvaluation: startEvaluation.value,
      endEvaluation: endEvaluation.value,
    };

    const response = await api.patch("/api/contestParams/dates", data);
    message.value = response.data.message ? response.data.message : "";
    isMessage.value = true;
  } catch (error) {
    if (error.response && error.response.data.message) {
      message.value = error.response.data.message ? error.response.data.message : "";
    }
    else{
      message.value = "Erreur lors de la modifications des paramètres du concours."
    }
  } finally {
    isMessage.value = true;
    loading.value = false;
  }
}


watch(startRegistration, (newStartDate) => {
  if (newStartDate && endRegistration.value) {
    const start = new Date(newStartDate);
    const end = new Date(endRegistration.value);
    if (end < start) {
      start.setDate(start.getDate() + 1);
      endRegistration.value = start.toISOString().split('T')[0];
    }
  }
});

watch(endRegistration, (newEndDate) => {
  if (newEndDate && startEvaluation.value) {
    const endReg = new Date(newEndDate);
    const startEval = new Date(startEvaluation.value);
    if (startEval < endReg) {
      endReg.setDate(endReg.getDate() + 1);
      startEvaluation.value = endReg.toISOString().split('T')[0];
    }
  }
});

watch(startEvaluation, (newStartEvalDate) => {
  if (newStartEvalDate && endEvaluation.value) {
    const startEval = new Date(newStartEvalDate);
    const endEval = new Date(endEvaluation.value);
    if (endEval < startEval) {
      startEval.setDate(startEval.getDate() + 1);
      endEvaluation.value = startEval.toISOString().split('T')[0];
    }
  }
});

watch(endRegistration, (newEndDate) => {
  if (newEndDate && startRegistration.value) {
    const start = new Date(startRegistration.value);
    const end = new Date(newEndDate);
    if (end < start) {
      start.setDate(start.getDate() + 1);
      endRegistration.value = start.toISOString().split('T')[0];
    }
  }
});
</script>

<style scoped>
h1 {
  text-align: center;
  margin-top: 30px;
  margin-bottom: 50px;
  font-weight: 600;
}

.submit-btn {
  padding: 12px 24px;
  background-color: #4CAF50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.submit-btn:hover {
  background-color: #45a049;
}

.date-input-container {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
}

.date-input-container label {
  width: 250px;
  font-weight: 500;
  text-align: left;
}

.date-input-container input {
  flex: 1;
  padding: 8px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

form {
  max-width: 800px;
  margin: 0 auto;
}

.submit-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}
</style>
