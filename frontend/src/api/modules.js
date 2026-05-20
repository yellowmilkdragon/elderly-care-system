import http from "./http";

export const authApi = {
  login(payload) {
    return http.post("/auth/login", payload);
  },
  me() {
    return http.get("/auth/me");
  }
};

export const dashboardApi = {
  summary() {
    return http.get("/dashboard/summary");
  }
};

export const customerApi = {
  list() {
    return http.get("/customers");
  },
  detail(id) {
    return http.get(`/customers/${id}`);
  },
  create(payload) {
    return http.post("/customers", payload);
  },
  update(id, payload) {
    return http.put(`/customers/${id}`, payload);
  }
};

export const bedApi = {
  overview() {
    return http.get("/beds/overview");
  },
  rooms() {
    return http.get("/beds/rooms");
  },
  available() {
    return http.get("/beds/available");
  },
  transfer(payload) {
    return http.post("/beds/transfer", payload);
  }
};

export const nursingApi = {
  items() {
    return http.get("/nursing/items");
  },
  levels() {
    return http.get("/nursing/levels");
  },
  createItem(payload) {
    return http.post("/nursing/items", payload);
  },
  updateItem(id, payload) {
    return http.put(`/nursing/items/${id}`, payload);
  },
  createLevel(payload) {
    return http.post("/nursing/levels", payload);
  },
  updateLevel(id, payload) {
    return http.put(`/nursing/levels/${id}`, payload);
  }
};

export const approvalApi = {
  outward() {
    return http.get("/approvals/outward");
  },
  backdown() {
    return http.get("/approvals/backdown");
  },
  auditOutward(id, payload) {
    return http.put(`/approvals/outward/${id}/audit`, payload);
  },
  auditBackdown(id, payload) {
    return http.put(`/approvals/backdown/${id}/audit`, payload);
  },
  markReturned(id) {
    return http.post(`/approvals/outward/${id}/return`);
  }
};

export const userApi = {
  users() {
    return http.get("/users");
  },
  caregivers() {
    return http.get("/caregivers");
  },
  assign(payload) {
    return http.post("/caregivers/assign", payload);
  }
};
