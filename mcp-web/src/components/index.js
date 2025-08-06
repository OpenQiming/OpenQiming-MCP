export default function (Vue, options) {
     //container
    Vue.component('PageContainer', () => import(/* webpackChunkName: "ui-chunk" */ './container/PageContainer'));

}
