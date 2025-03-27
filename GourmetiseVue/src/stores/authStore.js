import { defineStore } from 'pinia';
import { ref  } from 'vue';
import { useRouter } from 'vue-router';
import { jwtDecode } from "jwt-decode";

const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null);
  const manager = ref(false);
  const participant = ref(false);
  const router = useRouter(); 

  const isManager = () => {
    try {
      if (token.value) {
        const decodedToken = jwtDecode(token.value);
        manager.value = decodedToken.roles.includes('ROLE_GERANT');
      } else {
        manager.value = false;
      }
    } catch (error) {
      console.error('Erreur de décodage du token:', error);
      manager.value = false;
    }
  };

  const isParticipant = () => {
    try {
      if (token.value) {
        const decodedToken = jwtDecode(token.value);
        participant.value = decodedToken.roles.includes('ROLE_PARTICIPANT');
      } else {
        participant.value = false;
      }
    } catch (error) {
      console.error('Erreur de décodage du token:', error);
      participant.value = false;
    }
  };

  const login = (newToken) => {
    localStorage.setItem('token', newToken);
    token.value = newToken;
    isManager();
    isParticipant();
  };

  const logout = () => {
    localStorage.removeItem('token');
    token.value = null;
    router.push('/login');
  };

  const checkAuth = () => {
    token.value = localStorage.getItem('token');
    isManager();
    isParticipant();
  };

  return {
    token,
    manager,
    participant,
    login,
    logout,
    checkAuth,
    isManager,
  };
});

export default useAuthStore;
