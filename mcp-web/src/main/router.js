import Vue from 'vue';
import VueRouter from 'vue-router';
import store from './store';
import config from './config';
import qs from "qs";
import {  mcpview } from '@/views/home/router';

Vue.use(VueRouter);


const router = new VueRouter({
    mode: 'hash',
    routes: [
        mcpview,
        {
            path: '/',
            redirect: '/mcpview',
        }
    ]
});

const passThrough = function (menu, path) {
    for (let index = 0; index < menu.length; index++) {
        const v = menu[index];
        if (v.pathStr == path) {
            return true;
        }
        for (let index2 = 0; index2 < v.children.length; index2++) {
            const v2 = v.children[index2];
            if (v2.pathStr == path) {
                return true;
            }
            if (v2.children.find(v3 => v3.pathStr == path)) {
                return true;
            }
        }
    }
    return false;
};

router.beforeEach((to, from, next) => {
  /**
   * 2020.08.04
   * 暂无登录
   * 
   */
  next();
  return;
    let title = to.meta.title;
});

export default router;

