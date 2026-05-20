<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { userApi } from "../../api/modules";

const pageLoading = ref(false);
const drawerVisible = ref(false);
const selectedUser = ref(null);
const users = ref([]);
const filters = reactive({
  keyword: "",
  roleId: ""
});

const adminCount = computed(() => users.value.filter((user) => user.roleId === 1).length);
const caregiverCount = computed(() => users.value.filter((user) => user.roleId === 2).length);
const filteredUsers = computed(() =>
  users.value.filter((user) => {
    const matchKeyword =
      !filters.keyword ||
      user.nickname?.includes(filters.keyword) ||
      user.username?.includes(filters.keyword) ||
      user.phoneNumber?.includes(filters.keyword);
    const matchRole = !filters.roleId || user.roleId === Number(filters.roleId);
    return matchKeyword && matchRole;
  })
);

async function loadUsers() {
  pageLoading.value = true;
  try {
    const response = await userApi.users();
    users.value = response.data;
  } finally {
    pageLoading.value = false;
  }
}

function openDetail(user) {
  selectedUser.value = user;
  drawerVisible.value = true;
}

onMounted(loadUsers);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="metric-grid">
      <div class="page-card metric-card">
        <span class="metric-label">系统用户</span>
        <strong class="metric-value">{{ users.length }}</strong>
        <div class="metric-foot">管理员与健康管家统一纳入账号工作台。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">管理员</span>
        <strong class="metric-value">{{ adminCount }}</strong>
        <div class="metric-foot">负责审批、配置和日常运营控制。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">健康管家</span>
        <strong class="metric-value">{{ caregiverCount }}</strong>
        <div class="metric-foot">负责护理执行、客户跟进和服务落地。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">当前模式</span>
        <strong class="metric-value">在线</strong>
        <div class="metric-foot">已支持检索、筛选和账号详情查看。</div>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">账号列表</div>
      <h2 class="page-title">用户管理</h2>
      <p class="page-desc">当前后端已接入账号查询，前端补上了筛选和详情查看能力，方便排班与权限梳理。</p>
      <div style="margin-top:16px; display:flex; gap:12px; flex-wrap:wrap;">
        <el-input v-model="filters.keyword" placeholder="搜索姓名 / 账号 / 手机号" clearable style="max-width: 280px;" />
        <el-select v-model="filters.roleId" placeholder="角色筛选" clearable style="width: 160px;">
          <el-option label="系统管理员" :value="1" />
          <el-option label="健康管家" :value="2" />
        </el-select>
      </div>
    </div>

    <div class="page-card">
      <el-table :data="filteredUsers">
        <el-table-column prop="nickname" label="姓名" min-width="120" />
        <el-table-column prop="username" label="账号" min-width="140" />
        <el-table-column prop="phoneNumber" label="手机号" min-width="160" />
        <el-table-column label="角色" width="140">
          <template #default="{ row }">
            <span class="status-pill" :class="row.roleId === 1 ? 'status-info' : 'status-success'">
              {{ row.roleId === 1 ? "系统管理员" : "健康管家" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="说明" min-width="220">
          <template #default="{ row }">
            <span class="page-desc">
              {{ row.roleId === 1 ? "可处理系统运营、审批与配置。" : "可负责客户服务、护理执行与日常跟进。" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" plain size="small" @click="openDetail(row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-drawer v-model="drawerVisible" title="账号详情" size="420px">
      <div v-if="selectedUser" class="list-stack">
        <div class="mini-item"><strong>姓名</strong><span>{{ selectedUser.nickname }}</span></div>
        <div class="mini-item"><strong>账号</strong><span>{{ selectedUser.username }}</span></div>
        <div class="mini-item"><strong>手机号</strong><span>{{ selectedUser.phoneNumber }}</span></div>
        <div class="mini-item">
          <strong>角色</strong>
          <span>{{ selectedUser.roleId === 1 ? "系统管理员" : "健康管家" }}</span>
        </div>
        <div class="mini-item">
          <strong>职责</strong>
          <span>{{ selectedUser.roleId === 1 ? "审批、配置、系统运营" : "客户服务、护理执行、跟进回访" }}</span>
        </div>
      </div>
    </el-drawer>
  </section>
</template>
