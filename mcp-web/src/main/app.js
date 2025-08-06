import './global';

import Vue from 'vue';
import router from './router';
import store from './store';

import ElementUI from 'element-ui';
import 'element-ui/lib/theme-chalk/index.css';

import components from '@/components';

import plugins from './plugins';
import VCharts from 'v-charts';
Vue.use(ElementUI);
Vue.use(plugins);
Vue.use(VCharts);
Vue.use(components);

import 'swiper/dist/css/swiper.css'

import '../assets/less/global.less';

import '@wangeditor/editor/dist/css/style.css';


import mixins from './mixins';
Vue.mixin(mixins);

import config from './config';
Vue.prototype.$config = config;

new Vue({
    el: '#app',
    router,
    store,
    template: '<router-view></router-view>'
});
