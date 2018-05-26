import Vue from 'vue/dist/vue'
import Vuetify from 'vuetify'
import VueRouter from 'vue-router'
import router from './routes'

Vue.use(Vuetify);
Vue.use(VueRouter);

new Vue({
    router,
    template: `
        <router-view></router-view>
    `
}).$mount("#vue-app");