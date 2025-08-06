export default {
    state: {
        user: {},
        power: {},
        powerList: [],
        menu: [],
        permission: []
    },
    mutations: {
        login(state, user) {
            state.user = user;
        },
        logout(state) {
            state.user = {};
            state.power = {};
        },
        setPower(state, power) {
            state.power = power.currentPower;
            if (power.powerList) state.powerList = power.powerList;
        },
        setMenu(state, menu){
            state.menu = menu;
        },
        setPermission(state, permission){
            state.permission = permission;
        }
    },
    getters: {
        user(state) {
            return state.user;
        },
        power(state) {
            return state.power;
        },
        powerList(state) {
            return state.powerList;
        },
        menu(state){
            return state.menu;
        },
        permission(state){
            return state.permission;
        }
    }
};