import axios from "axios";

const api = axios.create({
    baseURL: "http://localhost:8000",
    headers: {
        'Content-Type': 'application/json'
    },
    withCredentials: true,
});

api.interceptors.request.use(
    (config) => {
        const token = localStorage.getItem('token');
        if (token){
            config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
    },
    (error) => {
        return Promise.reject(error);
    }
);

api.interceptors.response.use(
    response => response,
    async error =>{
        if(error.response && error.response.status === 401){
            try{
                const refreshResponse = await api.post('/login/refresh');
    
                api.defaults.headers.common['Authorization'] = `Bearer ${refreshResponse.data.token}`;
                localStorage.setItem('token', refreshResponse.data.token);
    
                error.config.headers['Authorization'] = `Bearer ${refreshResponse.data.token}`;
                return api(error.config)
    
            } catch(refreshError){
                console.log('Le refresh token a échoué, déconnexion requise');
                localStorage.removeItem('token');
                window.location.href = '/login';
            }
        }
        return Promise.reject(error);
    }
);

export default api;