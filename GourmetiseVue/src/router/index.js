import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";
import SignIn from "@/views/formSign-in.vue";

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
            component: SignIn,
        },
    ],
})

export default router