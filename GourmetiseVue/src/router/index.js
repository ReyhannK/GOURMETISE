import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";
import SignInBakery from "@/views/formSignIn.vue";
import CreateAccount from "@/views/createAccount.vue";
import Login from "@/views/login.vue";
import Management from "@/views/management.vue";
import { jwtDecode } from "jwt-decode";

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
            meta: { requiresAuth: true }
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
    ],
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.meta.requiresAuth && !token){
        next('/login');
    }else{
        if(to.meta.roles){
            let decodedToken = jwtDecode(token);
            if(!decodedToken.roles.some(role => to.meta.roles.includes(role))){
                next('/');
            }
        }
        next();
    }
});

export default router;