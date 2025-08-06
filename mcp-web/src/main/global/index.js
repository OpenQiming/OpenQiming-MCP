import _ from './util';
import moment from 'moment';
import ajax from './ajax';


moment.locale('zh-cn');

window._ = _;
window.moment = moment;
window.ajax = ajax;


const enviroment = process.env.enviroment;
console.log("enviroment", enviroment);
if (enviroment == 'prodCN') {

  document.onkeydown = () => {

    //禁用F12
    if (window.event && window.event.keyCode == 123) {
      return false;
      //禁用ctrl+shift+i,
    } else if (window.event.ctrlKey && window.event.shiftKey && window.event.keyCode == 73) {
      return false;
      //屏蔽Shift+F10
    } else if (window.event.shiftKey && window.event.keyCode == 121) {
      return false;
    }
  };

  document.addEventListener('contextmenu', function (e) {
    e.preventDefault(); // 阻止默认右键菜单
    //   alert("右键菜单已被禁用！");
    return false;
  });

}