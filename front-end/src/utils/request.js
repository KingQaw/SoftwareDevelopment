import axios from "axios";
import { useUserStore } from "@/stores/user";

const URL_API = "http://localhost:8080/api"
const request = axios.create({
    baseURL: URL_API,
    timeout: 10000,
    headers: {
        "Content-Type": "application/json",
    },
});

request.interceptors.request.use(config => {
    const userStore = useUserStore()
    const token = userStore.token
    if (token) {
        config.headers.Authorization = `Bearer ${token}`;
    }
    return config;
});

export default request;