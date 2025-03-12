import axios from "axios";

const API_BASE_URL = "http://localhost:1010";

// Axios config file that automatically adds the authorization header to all requests
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
