import Vue from 'vue'
import Router from 'vue-router'
import Welcome from '@/views/welcome'
import Single from '@/views/single'

Vue.use(Router)

export default new Router({
    mode: 'history',
    hash: false,
    routes: [
        {
            path: '/',
            name: 'Welcome',
            component: Welcome
        },
        {
            path: '/nota/:id',
            name: 'theNew',
            component: Single
        },
        { path: '*', redirect: '/' }
    ],
    scrollBehavior (to, from, savedPosition) {
        return { x: 0, y: 0 }
    }
})