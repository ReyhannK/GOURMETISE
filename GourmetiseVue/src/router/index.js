import {createRouter, createWebHistory} from 'vue-router'
import Index from "@/views/index.vue";
import ContestParams from "@/views/contestParams.vue";

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
    ],
})

export default router
