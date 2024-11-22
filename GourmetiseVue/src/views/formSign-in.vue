<template>
  <v-container>
    <h1>S'inscrire au concours</h1>
    <form @submit.prevent="submit">
      <v-text-field
        v-model="siret"
        :error-messages="nameError ? 'Name needs to be at least 2 characters.' : ''"
        label="N°Siret"
        type="input"
      ></v-text-field>

      <v-text-field
        v-model="name"
        :error-messages="nameError ? 'Name needs to be at least 2 characters.' : ''"
        label="Name"
        type="input"
        dense 
      ></v-text-field>

      <v-text-field
        v-model="street"
        :error-messages="nameError ? 'Name needs to be at least 2 characters.' : ''"
        label="Rue"
        type="input"
      ></v-text-field>

      <v-text-field
        v-model="postal_code"
        :error-messages="nameError ? 'Name needs to be at least 2 characters.' : ''"
        label="Code postal"
        type="input"
      ></v-text-field>

      <v-text-field
        v-model="city"
        :error-messages="nameError ? 'Name needs to be at least 2 characters.' : ''"
        label="Ville"
        type="input"
      ></v-text-field>

      <v-text-field
        v-model="telephone_number"
        :error-messages="phoneError ? 'Phone number needs to be at least 7 digits.' : ''"
        label="Numéro téléphone"
        type="input"
      ></v-text-field>
      
      <v-text-field
        v-model="contact_name"
        :error-messages="phoneError ? 'Phone number needs to be at least 7 digits.' : ''"
        label="Nom contact"
        type="input"
      ></v-text-field>

      <v-textarea
        v-model="bakery_description"
        :error-messages="phoneError ? 'Phone number needs to be at least 7 digits.' : ''"
        label="Description boulangerie"
      ></v-textarea>

      <v-textarea
        v-model="products_decription"
        :error-messages="phoneError ? 'Phone number needs to be at least 7 digits.' : ''"
        label="Description des produits"
      ></v-textarea>

      <v-checkbox
        v-model="checkbox"
        :error-messages="checkboxError ? 'Must be checked.' : ''"
        label="Option"
        type="checkbox"
        value="1"
      ></v-checkbox>

      <v-btn class="me-4" type="submit">
        Submit
      </v-btn>
    </form>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'

const siret = ref('')
const name = ref('')
const street = ref('')
const postal_code = ref('')
const city = ref('')
const telephone_number = ref('')
const contact_name = ref('')
const bakery_description = ref('')
const products_decription = ref('')
const checkbox = ref(false)

</script>

<style scoped>
.my-12 {
  margin-bottom: 1rem;
}

.mt-10 {
  margin-top: 2rem;
}
</style>