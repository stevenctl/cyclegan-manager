import Vue from 'vue'
import Vuetify from 'vuetify'
import App from './App'

Vue.use(Vuetify);

Vue.component('App', App);

new Vue({
    el: '#vue-app',
    render: function (createElement) {
        return createElement(App)
    }
});