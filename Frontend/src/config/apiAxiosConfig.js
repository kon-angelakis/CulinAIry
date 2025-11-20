import axios from "axios";

const API_BASE_URL = "https://24nfbc8snalq.share.zrok.io/v1/api";

const apiAxios = axios.create({
  //default axios implementation with a base api path
  baseURL: API_BASE_URL,
  headers: {
    "Content-Type": "application/json",
    skip_zrok_interstitial: true,
  },
});

export default apiAxios;
