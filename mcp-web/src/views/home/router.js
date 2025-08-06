const mcpviewPage = () => import(/*webpackChunkName:'home-chunk'*/'./MCPVIEW/Index.vue');

let mcpview = {
    path: '/mcpview', meta: {
        title: 'mcp首页',

    },
    redirect: { name: 'FirstIndex' },
    component: mcpviewPage,

    children: [
        {
            // 首页
            path: 'FirstIndex',
            name: "FirstIndex", meta: {
                title: 'mcp首页',
                cache: true,

            },
            component: () => import('./MCPVIEW/FirstIndex.vue'),
        },
        {
            // 详情
            path: 'mcpDetail',
            name: "mcpDetail", meta: {
                title: 'mcp首页',
                cache: false,

            },
            component: () => import('./MCPVIEW/Detail.vue'),
        },
    ]
};



export { mcpview };