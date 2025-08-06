import { mapGetters } from 'vuex';

export default {
    filters: {
        fromNow(val) {
            return moment(val).fromNow();
        },
        format(val, fmt) {
            if (!val) return '--';
            return moment(val).format(fmt || "YYYY-MM-DD HH:mm");
        }
    },
    computed: {
        ...mapGetters([''])
    }
};