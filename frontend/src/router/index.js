import { createRouter, createWebHistory } from "vue-router";
import { routes } from "./routes";
import { useUserStore } from "../stores/user";

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to) => {
  const userStore = useUserStore();
  if (to.meta.public) {
    return true;
  }
  if (!userStore.token) {
    return "/login";
  }
  return true;
});

export default router;
