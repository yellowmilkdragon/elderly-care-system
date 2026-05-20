import axios from "axios";

const apiHost = window.location.hostname || "localhost";
const envBaseUrl = import.meta.env.VITE_API_BASE_URL;
const fallbackBaseUrl = `http://${apiHost}:8081/api`;

const http = axios.create({
  baseURL: envBaseUrl || fallbackBaseUrl,
  timeout: 10000
});

http.interceptors.request.use((config) => {
  const token = localStorage.getItem("ec-token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});

http.interceptors.response.use(
  (response) => {
    const payload = response.data;
    if (payload && payload.success === false) {
      const error = new Error(payload.message || "Request failed");
      error.response = { data: payload };
      return Promise.reject(error);
    }
    return payload;
  },
  (error) => Promise.reject(error)
);

export default http;
