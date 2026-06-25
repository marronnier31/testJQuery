import { storeToRefs } from "pinia";
import pinia from "../stores/pinia";
import { useAuthStore } from "../stores/authStore";

const authStore = useAuthStore(pinia);

export const { user } = storeToRefs(authStore);

export function login(userData) {
  authStore.login(userData);
}

export function logout() {
  authStore.logout();
}

export function getLandingPath(userType) {
  return authStore.getLandingPath(userType);
}

export function useAuth() {
  return {
    user,
    login,
    logout,
    getLandingPath,
  };
}
