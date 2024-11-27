<template>
  <v-container>
    <h1>S'inscrire au concours</h1>
    <form @submit.prevent="submit" class="form-container">
      <v-text-field v-model="siret" @input="siretFormat"
        :error-messages="siretError ? 'Le N°siret doit être composé de 14 chiffres' : ''" label="N°Siret"
        placeholder="Exemple : 271 405 681 16097" type="input" clearable class="normal-field form-field"></v-text-field>

      <v-text-field v-model="name" :error-messages="nameError ? 'Le nom ne doit pas être vide' : ''" label="Nom"
        type="input" clearable class="normal-field form-field"></v-text-field>

      <v-text-field v-model="street" :error-messages="streetError ? 'La rue ne doit pas être vide' : ''" label="Rue"
        type="input" clearable class="form-field"></v-text-field>

      <v-text-field v-model="postal_code"
        :error-messages="postal_codeError ? 'Le code postal est composé de 5 chiffres' : ''" label="Code postal"
        placeholder="Exemple : 78 400" type="input" clearable class="normal-field form-field"></v-text-field>

      <v-text-field v-model="city" :error-messages="cityError ? 'La ville ne doit pas être vide' : ''" label="Ville"
        type="input" clearable class="normal-field form-field"></v-text-field>

      <v-text-field v-model="telephone_number" @input="telephoneFormat"
        :error-messages="telephone_numberError ? 'Le numéro de téléphone est composé de 10 chiffres' : ''"
        label="Numéro téléphone" placeholder="Exemple : 01 44 55 66 44" type="input" clearable
        class="normal-field form-field"></v-text-field>

      <v-text-field v-model="contact_name"
        :error-messages="contact_nameError ? 'Le nom du contact ne doit pas être vide' : ''" label="Nom contact"
        type="input" clearable class="normal-field form-field"></v-text-field>

      <v-textarea v-model="bakery_description"
        :error-messages="bakery_descriptionError ? 'La description de la boulangerie ne doit pas être vide' : ''"
        label="Description boulangerie"
        placeholder="Exemple : La Boulangerie du Bon Pain, située au cœur du village, séduit par ses senteurs alléchantes de pain frais et de viennoiseries dorées.Son décor chaleureux et son accueil souriant..."
        class="form-field"></v-textarea>

      <v-textarea v-model="products_decription"
        :error-messages="products_decriptionError ? 'La description des produits ne doit pas être vide' : ''"
        label="Description des produits"
        placeholder="Exemple : La boulangerie propose une variété de produits faits maison, du pain de campagne à la croûte dorée aux viennoiseries feuilletées. Croissants, pains au chocolat, éclairs au chocolat..."
        class="form-field"></v-textarea>

      <v-checkbox v-model="checkbox"
        :error-messages="checkboxError ? 'Les conditions d\'utilisations doivent être acceptées' : ''" label="En soumettant ce formulaire, j'ai lu et accepté les conditions d'utilisations relatives à la
        collecte de mes données." color="green"></v-checkbox>

      <v-btn class="me-4 submit-btn" type="submit" color="green">
        Valider
      </v-btn>
    </form>

    <div class="text-center pa-4">
      <v-dialog v-model="modal" width="auto">
        <v-card max-width="400" prepend-icon="mdi-information" :text="modal" title="Infomation">
          <template v-slot:actions>
            <v-btn class="ms-auto" text="Ok" @click="modal = null;" color="black"></v-btn>
          </template>
        </v-card>
      </v-dialog>
    </div>

  </v-container>
</template>

<script setup>
import axios from 'axios';
import { ref } from 'vue'
// var remplit par le formulaire
const siret = ref('');
const name = ref('');
const street = ref('');
const postal_code = ref('');
const city = ref('');
const telephone_number = ref('');
const contact_name = ref('');
const bakery_description = ref('');
const products_decription = ref('');
const checkbox = ref(false);

// var de gestion des erreurs
const siretError = ref(false);
const nameError = ref(false);
const streetError = ref(false);
const postal_codeError = ref(false);
const cityError = ref(false);
const telephone_numberError = ref(false);
const contact_nameError = ref(false);
const bakery_descriptionError = ref(false);
const products_decriptionError = ref(false);
const checkboxError = ref(false);

//var pour la modale d'affichage
const modal = ref(false);

//Formatage su numéro siret + téléphone
const siretFormat = () => {
  let siretValue = siret.value.replace(/\s+/g, '');

  siretValue = siretValue.replace(/(\d{3})(?=\d)/g, function (match, p1, offset) {
    return offset < 9 ? p1 + ' ' : p1;
  });

  siret.value = siretValue;
};

const telephoneFormat = () => {
  let telephoneValue = telephone_number.value.replace(/\s+/g, '');

  telephoneValue = telephoneValue.replace(/(\d{2})(?=\d)/g, '$1 ');

  telephone_number.value = telephoneValue;
};

//Valider les champs de formulaire
const validSiret = () => {
  const regex = /^\d{14}$/;
  return siretError.value = !siret.value || !regex.test(siret.value.replace(/\s+/g, ''));
};

const validName = () => {
  return nameError.value = !name.value || name.value.trim() === '';
};

const validStreet = () => {
  return streetError.value = !street.value || street.value.trim() === '';
};

const validCode_postal = () => {
  const regex = /^\d{5}$/;
  return postal_codeError.value = !postal_code.value || !regex.test(postal_code.value.replace(/\s+/g, ''));
};

const validCity = () => {
  return cityError.value = !city.value || city.value.trim() === '';
};

const validTelephone_number = () => {
  const regex = /^\d{10}$/;
  return telephone_numberError.value = !telephone_number.value || !regex.test(telephone_number.value.replace(/\s+/g, ''));
};

const validContact_name = () => {
  return contact_nameError.value = !contact_name.value || contact_name.value.trim() === '';
};

const validBakery_description = () => {
  return bakery_descriptionError.value = !bakery_description.value || bakery_description.value.trim() === '';
};

const validProducts_decription = () => {
  return products_decriptionError.value = !products_decription.value || products_decription.value.trim() === '';
};

const validCheckBox = () => {
  return checkboxError.value = !checkbox.value;
};

const valideForm = () => {
  return !(validSiret() || validName() || validStreet() || validCode_postal() || validCity()
    || validTelephone_number() || validContact_name() || validBakery_description() ||
    validProducts_decription() || validCheckBox()
  );
};

async function submit() {
  if (!valideForm()) {
    modal.value = "Formulaire incomplet ou incorrect.";
    return;
  }
  try {
    const newBakery = {
      "siret": siret.value,
      "name": name.value,
      "street": street.value,
      "postal_code": postal_code.value,
      "city": city.value,
      "telephone_number": telephone_number.value,
      "contact_name": contact_name.value,
      "bakery_description": bakery_description.value,
      "products_decription": products_decription.value,
      "user": {
        "email": "test2@gmail.com"
      }
    }
    const response = await axios.post(import.meta.env.VITE_API_URL + "/api/bakeries", newBakery);
    modal.value = response.data.message;
  } catch (error) {
    if (error.response && error.response.data.message) {
      modal.value = error.response.data.message;
    }
    else {
      modal.value = 'Erreur lors de l\'inscripton de la boulangerie';
    }
  }
}

</script>

<style scoped>
h1 {
  display: flex;
  justify-content: center;
  margin-bottom: 1.5rem;
}

.form-container {
  margin-left: 100px;
  max-width: 550px;
}

.normal-field {
  max-width: 350px;
}

.submit-btn {
  float: right;
  margin-top: 20px;
}

.form-field {
  margin-bottom: -1px;
}
</style>