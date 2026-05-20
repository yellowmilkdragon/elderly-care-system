import { defineStore } from "pinia";

export const useUserStore = defineStore("user", {
  state: () => ({
    token: localStorage.getItem("ec-token") || "",
    profile: {
      username: "admin",
      nickname: "系统管理员",
      roleCode: "ADMIN"
    }
  }),
  actions: {
    login(payload) {
      this.token = payload.token || "mock-jwt-token";
      this.profile = payload.profile || this.profile;
      localStorage.setItem("ec-token", this.token);
    },
    logout() {
      this.token = "";
      localStorage.removeItem("ec-token");
    }
  }
});
