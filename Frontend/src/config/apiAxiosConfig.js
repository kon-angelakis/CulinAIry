import axios from "axios";

const API_BASE_URL = "http://192.168.1.70:1010/v1/api";

const apiAxios = axios.create({
  //default axios implementation with a base api path
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
  },
});

export default apiAxios;
