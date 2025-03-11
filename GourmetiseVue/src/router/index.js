import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";
import SignInBakery from "@/views/formSignIn.vue";
import CreateAccount from "@/views/createAccount.vue";
import Login from "@/views/login.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Index,
            meta: { requiresAuth: true }
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
        },
        {
            path: '/login',
            name: 'Login',
            component: Login,
        },
    ],
});

router.beforeEach((to, from, next) => {
    const token = localStorage.getItem('token');
    if (to.meta.requiresAuth && !token){
        next('/login');
    }else{
        next();
    }
});

export default router;