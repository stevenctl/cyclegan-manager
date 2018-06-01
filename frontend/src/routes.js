import VueRouter from 'vue-router'
import Layout from './components/Layout'
import LandingPage from './components/pages/LandingPage'
import Vue from 'vue/dist/vue'

Vue.use(VueRouter)

export default new VueRouter({
    mode: 'hash',
    routes: [
        {
            path: "/",
            component: Layout,
            children: [
                {path: "/", component: LandingPage}
            ]
        }
    ]
})