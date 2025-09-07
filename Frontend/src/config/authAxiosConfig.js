import axios from "axios";

const API_BASE_URL = "https://24nfbc8snalq.share.zrok.io/v1/api";

// Axios implementation for authenticated request intercepting
const authAxios = axios.create({
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    skip_zrok_interstitial: true,
  },
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
