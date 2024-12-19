import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";
import SignInBakery from "@/views/formSign-in.vue";
import CreateAccount from "@/views/createAccount.vue";
import Login from "@/views/login.vue";

const router = createRouter({
    history: createWebHistory(import.meta.env.BASE_URL),
    routes: [
        {
            path: '/',
            name: 'home',
            component: Index,
        },
        {
            path: '/contestParams',
            name: 'contestParams',
            component: ContestParams,
        },
        {
            path: '/sign-in',
            name: 'Sign-in',
            component: SignInBakery,
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
})

export default router