export default {
    methods: {
        getTimeData() {
            ajax
                .post("/homePage/getDataSearchTime",{})
                .then((r) => {
                    this.dataTime = r.data
                });
        },
    }
}