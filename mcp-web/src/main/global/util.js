import _ from 'lodash';

_.downloadFile = function (data, fileName) {
    let blob = new Blob([data]);
    let url = window.URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = fileName;
    link.click();
    URL.revokeObjectURL(url);
}

_.transferFullArea = function (name) {

};
_.simplifyFullArea = function (name) {
   
};

export default _;