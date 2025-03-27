import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";
import SignInBakery from "@/views/formSignIn.vue";
import CreateAccount from "@/views/createAccount.vue";
import Login from "@/views/login.vue";
import Management from "@/views/management.vue";
import Results from "@/views/results.vue";
import MyBakery from "@/views/myBakery.vue";
import { jwtDecode } from "jwt-decode";
import useAuthStore from '@/stores/authStore';

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Index,
            meta: { requiresAuth: false }
        },
        {
            path: '/contestParams',
            name: 'contestParams',
            component: ContestParams,
            meta: { requiresAuth: false }
        },
        {
            path: '/sign-in',
            name: 'Sign-in',
            component: SignInBakery,
            meta: { requiresAuth: true }
        },
        {
            path: '/create-account',
            name: 'CreateAccount',
            component: CreateAccount,
            meta: { requiresAuth: false }
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
            meta: { requiresAuth: false }
        },
        {
            path: '/management',
            name: 'Management',
            component: Management,
            meta: { requiresAuth: true, roles: ['ROLE_GERANT'] }
        },
        {
            path: '/results',
            name: 'Results',
            component: Results,
            meta: { requiresAuth: false }
        },
        {
            path: '/my-bakery',
            name: 'MyBakery',
            component: MyBakery,
            meta: { requiresAuth: true, roles: ['ROLE_PARTICIPANT'] }
        },
    ],
});

router.beforeEach((to, from, next) => {
    const authStore = useAuthStore();
    authStore.checkAuth();   
    if (to.meta.requiresAuth && !authStore.token){
        next('/login');
    }else{
        if(to.meta.roles){
            let decodedToken = jwtDecode(authStore.token);
            if(!decodedToken.roles.some(role => to.meta.roles.includes(role))){
                next('/');
            }
        }
        next();
    };
});

export default router;