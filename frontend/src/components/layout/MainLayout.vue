<script setup>
import { computed } from "vue";
import { useRoute, useRouter } from "vue-router";
import { useUserStore } from "../../stores/user";

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();

const menus = [
  { label: "工作台", path: "/dashboard", note: "总览与运营态势" },
  { label: "客户管理", path: "/customers", note: "入住客户与档案" },
  { label: "床位管理", path: "/beds", note: "楼层房间与床位" },
  { label: "护理项目", path: "/nursing/items", note: "服务目录与价格" },
  { label: "护理等级", path: "/nursing/levels", note: "等级与服务包" },
  { label: "审批管理", path: "/approvals", note: "外出与退住审批" },
  { label: "健康管家", path: "/caregivers", note: "服务对象分配" },
  { label: "用户管理", path: "/users", note: "账号角色与人员" }
];

const currentTitle = computed(() => route.meta.title || "东软颐养中心管理系统");

function handleSelect(path) {
  router.push(path);
}

function logout() {
  userStore.logout();
  router.push("/login");
}
</script>

<template>
  <div class="layout">
    <aside class="layout-sidebar">
      <div class="brand-block">
        <div class="brand-mark">颐</div>
        <div>
          <div class="brand-title">东软颐养中心</div>
          <div class="brand-subtitle">运营管理平台</div>
        </div>
      </div>

      <div class="nav-caption">核心模块</div>
      <div class="nav-list">
        <button
          v-for="item in menus"
          :key="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path }"
          @click="handleSelect(item.path)"
        >
          <strong>{{ item.label }}</strong>
          <span>{{ item.note }}</span>
        </button>
      </div>

      <div class="sidebar-foot">
        <div class="foot-card">
          <div class="foot-title">今日值守</div>
          <div class="foot-text">把运营、护理和审批放到一个更轻松的工作台里。</div>
        </div>
      </div>
    </aside>

    <div class="layout-main">
      <header class="layout-header">
        <div>
          <div class="section-eyebrow">智能运营视图</div>
          <h1 class="header-title">{{ currentTitle }}</h1>
          <p class="header-subtitle">把床位、护理、审批和服务对象协作放在一张可读的界面里。</p>
        </div>

        <div class="header-user">
          <div class="user-chip">
            <strong>{{ userStore.profile.nickname }}</strong>
            <span>{{ userStore.profile.roleCode }}</span>
          </div>
          <el-button type="success" @click="logout">退出登录</el-button>
        </div>
      </header>

      <main class="layout-content">
        <router-view />
      </main>
    </div>
  </div>
</template>

<style scoped>
.layout {
  min-height: 100vh;
  display: grid;
  grid-template-columns: 300px 1fr;
}

.layout-sidebar {
  display: flex;
  flex-direction: column;
  min-height: 100vh;
  padding: 28px 20px;
  color: #f9f4ec;
  background:
    radial-gradient(circle at top, rgba(201, 131, 69, 0.2), transparent 26%),
    linear-gradient(180deg, #1f342c 0%, #2f4e42 50%, #2a483d 100%);
}

.brand-block {
  display: flex;
  align-items: center;
  gap: 16px;
  margin-bottom: 28px;
}

.brand-mark {
  width: 58px;
  height: 58px;
  border-radius: 20px;
  display: grid;
  place-items: center;
  background: linear-gradient(135deg, #f2d8b2 0%, #d4985e 100%);
  color: #29453a;
  font-size: 24px;
  font-weight: 900;
}

.brand-title {
  font-size: 21px;
  font-weight: 800;
}

.brand-subtitle {
  font-size: 12px;
  opacity: 0.78;
}

.nav-caption {
  margin: 20px 8px 14px;
  font-size: 12px;
  letter-spacing: 0.16em;
  opacity: 0.68;
}

.nav-list {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.nav-item {
  width: 100%;
  border: 1px solid rgba(255, 255, 255, 0.08);
  background: rgba(255, 255, 255, 0.03);
  color: inherit;
  text-align: left;
  border-radius: 18px;
  padding: 14px 16px;
  cursor: pointer;
  transition: 0.2s ease;
}

.nav-item strong {
  display: block;
  font-size: 15px;
}

.nav-item span {
  display: block;
  margin-top: 6px;
  font-size: 12px;
  opacity: 0.72;
}

.nav-item:hover,
.nav-item.active {
  background: rgba(255, 251, 244, 0.12);
  border-color: rgba(242, 216, 178, 0.28);
  transform: translateY(-1px);
}

.sidebar-foot {
  margin-top: auto;
  padding-top: 24px;
}

.foot-card {
  border-radius: 20px;
  padding: 16px;
  background: rgba(255, 255, 255, 0.07);
  border: 1px solid rgba(255, 255, 255, 0.08);
}

.foot-title {
  font-weight: 700;
}

.foot-text {
  margin-top: 8px;
  font-size: 12px;
  line-height: 1.7;
  opacity: 0.8;
}

.layout-main {
  padding: 22px;
}

.layout-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  gap: 20px;
  margin-bottom: 18px;
}

.header-title {
  margin: 12px 0 8px;
  font-size: 36px;
  line-height: 1.05;
}

.header-subtitle {
  margin: 0;
  max-width: 720px;
  color: var(--ec-subtext);
  line-height: 1.7;
}

.header-user {
  display: flex;
  align-items: center;
  gap: 14px;
}

.user-chip {
  min-width: 170px;
  padding: 12px 16px;
  border-radius: 18px;
  background: rgba(255, 252, 247, 0.75);
  border: 1px solid var(--ec-border);
  box-shadow: var(--ec-shadow);
}

.user-chip strong {
  display: block;
}

.user-chip span {
  display: block;
  margin-top: 4px;
  color: var(--ec-subtext);
  font-size: 12px;
}

.layout-content {
  padding-bottom: 24px;
}

@media (max-width: 1024px) {
  .layout {
    grid-template-columns: 1fr;
  }

  .layout-sidebar {
    min-height: auto;
  }

  .layout-header {
    flex-direction: column;
  }
}
</style>
