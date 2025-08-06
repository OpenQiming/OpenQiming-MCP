import dialog from './dialog';
import user from './user';

export default function(Vue,options){
    Vue.use(dialog,options);
    Vue.use(user,options);
};