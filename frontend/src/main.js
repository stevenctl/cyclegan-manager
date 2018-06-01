import Vue from 'vue/dist/vue.runtime.min'
import Vuetify from 'vuetify'
import 'vuetify/dist/vuetify.css'
import VueRouter from 'vue-router'
import router from './routes'
import App from './App'

Vue.use(Vuetify);
Vue.use(VueRouter);

new Vue({
    router,
    render: h => h(App)
}).$mount("#vue-app");