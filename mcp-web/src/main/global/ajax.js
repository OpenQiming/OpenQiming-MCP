import axios from 'axios';
import { Message } from 'element-ui'
import config from '../config';
import qs from "qs";

let ajax = axios.create({
  baseURL: `${config.apiUrl}/`,
  withCredentials: true,
  timeout: 60 * 60 * 1000,
  headers: {}
});

//错误处理
const err = (error) => {
  let status = error.response.status || '';
  if (status === 401) {
    localStorage.removeItem("token");
    if (config.env != 'yanshi') {
      // location.href = location.origin;
      // console.log('errpr',error.response.data.data)
      location.href = error.response.data.data;
    } else {
      location.href = location.origin;
      Message.error('请您登录');
    }
  } else if (status === 500) {
    Message.error(error.response.data);
  }
  return Promise.reject(error)
}

//请求拦截
ajax.interceptors.request.use(config => {

  return config
}, err)

//响应拦截
ajax.interceptors.response.use((response) => {
  return response.data;
}, err)

//重写get请求，请求参数不用加params
let getMethod = ajax.get;
ajax.get = function (url, data, config) {
  let req = config || {};
  config = Object.assign(req, {
    params: data
  });

  let promise = getMethod(url, config).then(r => {
    return r;
  });

  return promise;
};

export default ajax;