<script setup>
import { computed, onMounted, ref } from "vue";
import { dashboardApi, approvalApi, customerApi, userApi } from "../../api/modules";

const summary = ref({});
const outwards = ref([]);
const backdowns = ref([]);
const customers = ref([]);
const caregivers = ref([]);
const pageLoading = ref(false);

const pendingCount = computed(() => (summary.value.outwardPending ?? 0) + (summary.value.backdownPending ?? 0));
const occupancyRate = computed(() => {
  const occupied = summary.value.occupiedBeds ?? 0;
  const total = (summary.value.occupiedBeds ?? 0) + (summary.value.freeBeds ?? 0) + (summary.value.outwardBeds ?? 0);
  return total ? Math.round((occupied / total) * 100) : 0;
});

const capacityPressure = computed(() => {
  if (occupancyRate.value >= 85) return { label: "高压运行", className: "status-danger" };
  if (occupancyRate.value >= 65) return { label: "平稳运行", className: "status-warning" };
  return { label: "空间充足", className: "status-success" };
});

const busiestCaregiver = computed(() =>
  [...caregivers.value].sort((a, b) => (b.customers?.length || 0) - (a.customers?.length || 0))[0]
);

const warningRadar = computed(() => [
  {
    title: "床位压力",
    value: `${occupancyRate.value}%`,
    desc: occupancyRate.value >= 85 ? "需要尽快预留调床与退住缓冲位" : "当前床位供给仍可支撑日常运营"
  },
  {
    title: "审批积压",
    value: `${pendingCount.value} 条`,
    desc: pendingCount.value > 0 ? "建议优先清理影响床位状态的申请" : "审批流转保持通畅"
  },
  {
    title: "外出回院",
    value: `${summary.value.outwardBeds ?? 0} 张`,
    desc: (summary.value.outwardBeds ?? 0) > 0 ? "请重点关注返院登记与床位恢复" : "当前无外出床位占用"
  }
]);

const smartSuggestions = computed(() => {
  const suggestions = [];
  if ((summary.value.freeBeds ?? 0) <= 1) {
    suggestions.push("建议把空闲床位优先保留给临时调床和新入住客户，避免高峰时段无床可调。");
  }
  if (pendingCount.value > 0) {
    suggestions.push("建议值班管理员先处理审批模块，再安排床位与服务对象调整，减少后续重复操作。");
  }
  if ((summary.value.outwardBeds ?? 0) > 0) {
    suggestions.push("建议设置返院提醒卡片，主动追踪外出客户，避免床位状态长期停留在外出。");
  }
  if (busiestCaregiver.value?.customers?.length >= 2) {
    suggestions.push(`建议关注 ${busiestCaregiver.value.nickname} 的服务负荷，可考虑把部分客户分配给空闲管家。`);
  }
  return suggestions.length ? suggestions : ["当前运营节奏较平稳，可以继续补强护理记录、客户关怀与数据沉淀。"];
});

async function loadData() {
  pageLoading.value = true;
  try {
    const [summaryResponse, outwardResponse, backdownResponse, customerResponse, caregiverResponse] = await Promise.all([
      dashboardApi.summary(),
      approvalApi.outward(),
      approvalApi.backdown(),
      customerApi.list(),
      userApi.caregivers()
    ]);

    summary.value = summaryResponse.data;
    outwards.value = outwardResponse.data;
    backdowns.value = backdownResponse.data;
    customers.value = customerResponse.data;
    caregivers.value = caregiverResponse.data;
  } finally {
    pageLoading.value = false;
  }
}

onMounted(loadData);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="hero-panel page-card">
      <div>
        <div class="section-eyebrow">智慧运营中心</div>
        <h2 class="hero-title">今天的运营节奏一眼就明白</h2>
        <p class="page-desc hero-desc">
          把客户、床位、审批和人员调度放进同一张运营雷达图里，不只是展示数据，而是直接告诉值班人员应该先做什么。
        </p>
      </div>
      <div class="hero-badges">
        <div class="hero-badge">
          <span>床位压力</span>
          <strong>{{ occupancyRate }}%</strong>
        </div>
        <div class="hero-badge">
          <span>运行状态</span>
          <strong>{{ capacityPressure.label }}</strong>
        </div>
      </div>
    </div>

    <div class="metric-grid">
      <div class="page-card metric-card">
        <span class="metric-label">在院客户</span>
        <strong class="metric-value">{{ summary.customerCount ?? 0 }}</strong>
        <div class="metric-foot">今日护理、服务与回访工作都围绕这些客户展开。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">床位使用率</span>
        <strong class="metric-value">{{ occupancyRate }}%</strong>
        <div class="metric-foot">当前已入住床位 {{ summary.occupiedBeds ?? 0 }} 张。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">待处理审批</span>
        <strong class="metric-value">{{ pendingCount }}</strong>
        <div class="metric-foot">外出与退住申请需要值班人员及时处理。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">健康管家</span>
        <strong class="metric-value">{{ summary.caregiverCount ?? 0 }}</strong>
        <div class="metric-foot">当前已参与服务分配的服务人员数量。</div>
      </div>
    </div>

    <div class="two-column">
      <div class="page-card">
        <div class="section-eyebrow">运营雷达</div>
        <h2 class="page-title">风险热度</h2>
        <div class="radar-grid" style="margin-top: 18px;">
          <div v-for="item in warningRadar" :key="item.title" class="radar-card">
            <span>{{ item.title }}</span>
            <strong>{{ item.value }}</strong>
            <p>{{ item.desc }}</p>
          </div>
        </div>
      </div>

      <div class="page-card">
        <div class="section-eyebrow">智能建议</div>
        <h2 class="page-title">系统下一步建议</h2>
        <div class="list-stack" style="margin-top: 18px;">
          <div v-for="tip in smartSuggestions" :key="tip" class="mini-item">
            <div>
              <strong>运营建议</strong>
              <span>{{ tip }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="three-column">
      <div class="page-card">
        <div class="section-eyebrow">入住清单</div>
        <h2 class="page-title">最新客户</h2>
        <div class="list-stack" style="margin-top: 18px;">
          <div v-for="customer in customers.slice(0, 4)" :key="customer.id" class="mini-item">
            <div>
              <strong>{{ customer.customerName }}</strong>
              <span>{{ customer.roomNo }} 房 / 床位 {{ customer.bedId }}</span>
            </div>
            <span class="status-pill status-info">{{ customer.bloodType }}</span>
          </div>
        </div>
      </div>

      <div class="page-card">
        <div class="section-eyebrow">审批提醒</div>
        <h2 class="page-title">待办队列</h2>
        <div class="list-stack" style="margin-top: 18px;">
          <div v-for="outward in outwards.slice(0, 2)" :key="`out-${outward.id}`" class="mini-item">
            <div>
              <strong>外出申请 #{{ outward.id }}</strong>
              <span>{{ outward.outgoingReason }}</span>
            </div>
            <span class="status-pill status-warning">待审批</span>
          </div>
          <div v-for="backdown in backdowns.slice(0, 2)" :key="`back-${backdown.id}`" class="mini-item">
            <div>
              <strong>退住申请 #{{ backdown.id }}</strong>
              <span>{{ backdown.retreatReason }}</span>
            </div>
            <span class="status-pill status-danger">待确认</span>
          </div>
        </div>
      </div>

      <div class="page-card">
        <div class="section-eyebrow">服务负荷</div>
        <h2 class="page-title">人员分布</h2>
        <div class="list-stack" style="margin-top: 18px;">
          <div v-for="caregiver in caregivers" :key="caregiver.id" class="mini-item">
            <div>
              <strong>{{ caregiver.nickname }}</strong>
              <span>{{ caregiver.customers.join("、") || "暂无服务对象" }}</span>
            </div>
            <span class="status-pill status-success">{{ caregiver.customers.length }} 人</span>
          </div>
        </div>
      </div>
    </div>
  </section>
</template>
