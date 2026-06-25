import axios from "axios";

const api = axios.create({
  baseURL: "/",
  withCredentials: true,
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
  },
});

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 901) {
      alert("세션이 만료되었습니다. 다시 로그인해 주세요.");
      window.location.href = "/login";
    }
    return Promise.reject(error);
  },
);

export default api;
