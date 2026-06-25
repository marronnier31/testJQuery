import { defineStore } from "pinia";

function loadSavedUser() {
  const saved = sessionStorage.getItem("user");
  if (!saved) return null;

  try {
    return JSON.parse(saved);
  } catch {
    sessionStorage.removeItem("user");
    return null;
  }
}

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: loadSavedUser(),
  }),
  actions: {
    login(userData) {
      this.user = userData;
      sessionStorage.setItem("user", JSON.stringify(userData));
    },
    logout() {
      this.user = null;
      sessionStorage.removeItem("user");
    },
    getLandingPath(userType = this.user?.userType) {
      const landing = {
        A: "/admin/dashboard",
        I: "/inst/my-page",
        S: "/stu/my-page",
      };

      return landing[userType] ?? "/login";
    },
  },
});
