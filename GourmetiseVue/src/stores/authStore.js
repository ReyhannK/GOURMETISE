import { defineStore } from 'pinia';
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import { jwtDecode } from "jwt-decode";

const useAuthStore = defineStore('auth', () => {
  const token = ref(localStorage.getItem('token') || null);
  const manager = ref(false);
  const router = useRouter(); 

  const isManager = () => {
    try {
      if (token.value) {
        const decodedToken = jwtDecode(token.value);
        console.log(decodedToken.value);
        manager.value = decodedToken.roles.includes('ROLE_GERANT');
      } else {
        manager.value = false;
      }
    } catch (error) {
      console.error('Erreur de décodage du token:', error);
      manager.value = false;
    }
  };

  const login = (newToken) => {
    localStorage.setItem('token', newToken);
    token.value = newToken;
    isManager();
  };

  const logout = () => {
    localStorage.removeItem('token');
    token.value = null;
    manager.value = false;
    router.push('/login');
  };

  const checkAuth = () => {
    token.value = localStorage.getItem('token');
    isManager();
  };

  return {
    token,
    manager,
    login,
    logout,
    checkAuth,
    isManager,
  };
});

export default useAuthStore;
