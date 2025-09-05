import axios from "axios";

const API_BASE_URL = "http://192.168.1.70:1010/v1/api";

// Axios implementation for authenticated request intercepting
const authAxios = axios.create({
  baseURL: API_BASE_URL,
});

authAxios.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("AuthToken");

    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

export default authAxios;
