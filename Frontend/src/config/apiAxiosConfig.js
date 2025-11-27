import axios from "axios";

const API_BASE_URL =
  import.meta.env.VITE_DEFAULT_BACKEND_URL +
  import.meta.env.VITE_DEFAULT_BACKEND_API_PATH;

const apiAxios = axios.create({
  //default axios implementation with a base api path
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    skip_zrok_interstitial: true,
  },
});

export default apiAxios;
