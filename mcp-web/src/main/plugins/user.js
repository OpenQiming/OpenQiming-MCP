import store from '../store'
const User = {
    install(Vue, options) {
        Vue.prototype.$user = {
            hasCap(permission) {
                return !!store.state.user.permission.find(v => v.domId == permission);
            },
        }
    }
};
// <!-- html注释复制地址  -->
export default User