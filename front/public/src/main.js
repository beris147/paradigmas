// The Vue build version to load with the `import` command
// (runtime-only or standalone) has been set in webpack.base.conf with an alias.
import Vue from 'vue'
import App from './App'
import router from './router'
import { BootstrapVue, IconsPlugin, BCard, BImg, BEmbed, BJumbotron, VBHover, BNavbar } from 'bootstrap-vue'
import 'bootstrap/dist/css/bootstrap.css'
import 'bootstrap-vue/dist/bootstrap-vue.css'
import { library } from '@fortawesome/fontawesome-svg-core'
import { faUserSecret } from '@fortawesome/free-solid-svg-icons'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import { faFacebookSquare, faTwitterSquare, faInstagramSquare, faYoutubeSquare, faFacebookF, faTwitter, faInstagram, faYoutube } from '@fortawesome/free-brands-svg-icons'
import { Tweet } from 'vue-tweet-embed'
import VueInstagram from 'vue-instagram'
import InstagramEmbed from 'vue-instagram-embed'
import jQuery from 'jquery'
import axios from 'axios'

library.add(faUserSecret, faFacebookSquare, faTwitterSquare, faInstagramSquare, faYoutubeSquare, faFacebookF, faTwitter, faInstagram, faYoutube)

Vue.config.productionTip = false

// Install BootstrapVue
Vue.use(BootstrapVue)
// Optionally install the BootstrapVue icon components plugin
Vue.use(IconsPlugin)
Vue.use(jQuery)
Vue.component('b-card', BCard)
Vue.component('b-img', BImg)
Vue.component('font-awesome-icon', FontAwesomeIcon)
Vue.component('b-embed', BEmbed)
Vue.component('b-jumbotron', BJumbotron)
Vue.directive('b-hover', VBHover)
Vue.component('b-navbar', BNavbar)
Vue.component('Tweet', Tweet)
Vue.component('vue-instagram', VueInstagram)
Vue.component('instagram-embed', InstagramEmbed)

Vue.prototype.$http = axios
Vue.prototype.$server = 'server/api'

/* eslint-disable no-new */
new Vue({
    el: '#app',
    router,
    components: { App },
    template: '<App/>'
})