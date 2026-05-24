<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { customerApi, userApi } from "../../api/modules";

// 张文辉：健康管家页面，负责设置服务对象、服务关注和分配反馈。
const pageLoading = ref(false);
const caregivers = ref([]);
const customers = ref([]);
const assignVisible = ref(false);
const assignSubmitting = ref(false);
const assignHint = ref("可将客户直接重新分配给新的健康管家。");
const lastAssign = ref(null);
const assignForm = reactive({
  customerId: null,
  caregiverId: null
});

const busiestCaregiver = computed(() =>
  [...caregivers.value].sort((a, b) => (b.customers?.length || 0) - (a.customers?.length || 0))[0]
);

const unassignedCustomers = computed(() =>
  customers.value.filter((customer) => !customer.userId || customer.userId === -1)
);

async function loadData() {
  // 张文辉：同步加载健康管家与客户列表，用于分配操作和负载分析。
  pageLoading.value = true;
  try {
    const [caregiverResponse, customerResponse] = await Promise.all([
      userApi.caregivers(),
      customerApi.list()
    ]);
    caregivers.value = caregiverResponse.data;
    customers.value = customerResponse.data;
  } finally {
    pageLoading.value = false;
  }
}

function openAssign() {
  // 张文辉：打开分配弹窗前先校验基础数据是否完整。
  if (!customers.value.length) {
    ElMessage.warning("当前没有可分配的客户");
    return;
  }
  if (!caregivers.value.length) {
    ElMessage.warning("当前没有可用的健康管家");
    return;
  }
  assignForm.customerId = customers.value[0].id;
  assignForm.caregiverId = caregivers.value[0].id;
  assignVisible.value = true;
}

async function submitAssign() {
  // 张文辉：提交服务对象分配并刷新健康管家列表。
  if (!assignForm.customerId || !assignForm.caregiverId) {
    ElMessage.warning("请选择客户和健康管家");
    return;
  }
  assignSubmitting.value = true;
  assignHint.value = "正在提交分配结果并刷新服务归属...";
  try {
    const customer = customers.value.find((item) => item.id === assignForm.customerId);
    const caregiver = caregivers.value.find((item) => item.id === assignForm.caregiverId);
    await userApi.assign({ ...assignForm });
    lastAssign.value = {
      customerName: customer?.customerName || `客户 #${assignForm.customerId}`,
      caregiverName: caregiver?.nickname || `管家 #${assignForm.caregiverId}`,
      time: new Date().toLocaleString("zh-CN", { hour12: false })
    };
    assignHint.value = "分配成功，列表中的服务对象已同步更新。";
    ElMessage.success("服务对象分配成功");
    assignVisible.value = false;
    await loadData();
  } catch (error) {
    assignHint.value = "分配失败，请稍后重新提交。";
    ElMessage.error("分配失败，请稍后重试");
    console.error(error);
  } finally {
    assignSubmitting.value = false;
  }
}

onMounted(loadData);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="two-column">
      <div class="page-card">
        <div class="section-eyebrow">健康管家编组</div>
        <h2 class="page-title">服务对象分配</h2>
        <p class="page-desc">
          这里不仅能看每位管家的当前负载，还可以直接为客户重新分配负责人，并在操作后立即看到结果反馈。
        </p>
        <div style="margin-top: 16px; display:flex; gap:12px; align-items:center; flex-wrap:wrap;">
          <el-button type="success" @click="openAssign">分配服务对象</el-button>
          <span class="page-desc">{{ assignHint }}</span>
        </div>
      </div>

      <div class="page-card">
        <div class="section-eyebrow">当前负载最高</div>
        <h2 class="page-title">{{ busiestCaregiver?.nickname || "暂无数据" }}</h2>
        <p class="page-desc">
          当前服务 {{ busiestCaregiver?.customers?.length || 0 }} 位客户：
          {{ busiestCaregiver?.customers?.join("、") || "暂无服务对象" }}
        </p>
        <div class="soft-panel" style="margin-top: 16px;">
          <strong>重点关注：</strong>
          <span class="page-desc">未分配客户 {{ unassignedCustomers.length }} 位，可优先安排给负载较轻的管家。</span>
        </div>
      </div>
    </div>

    <div v-if="lastAssign" class="page-card">
      <div class="section-eyebrow">最近一次操作</div>
      <h2 class="page-title">分配已生效</h2>
      <p class="page-desc">
        {{ lastAssign.customerName }} 已分配给 {{ lastAssign.caregiverName }}，完成时间为 {{ lastAssign.time }}。
      </p>
    </div>

    <div class="three-column">
      <div v-for="caregiver in caregivers" :key="caregiver.id" class="page-card">
        <div style="display:flex;justify-content:space-between;align-items:flex-start;">
          <div>
            <h3 style="margin:0;font-size:21px;">{{ caregiver.nickname }}</h3>
            <p class="page-desc" style="margin-top:6px;">账号 {{ caregiver.username }} · {{ caregiver.phoneNumber }}</p>
          </div>
          <span class="status-pill status-success">{{ caregiver.customers.length }} 人</span>
        </div>

        <div class="list-stack" style="margin-top:18px;">
          <div v-for="customer in caregiver.customers" :key="customer" class="mini-item">
            <div>
              <strong>{{ customer }}</strong>
              <span>当前已分配至该健康管家名下</span>
            </div>
          </div>
          <div v-if="caregiver.customers.length === 0" class="mini-item">
            <div>
              <strong>暂无服务对象</strong>
              <span>可优先承接新入住或重新分配的客户</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="assignVisible" title="分配服务对象" width="560px">
      <el-form label-position="top">
        <el-form-item label="客户">
          <el-select v-model="assignForm.customerId" style="width: 100%">
            <el-option v-for="customer in customers" :key="customer.id" :label="customer.customerName" :value="customer.id" />
          </el-select>
        </el-form-item>
        <el-form-item label="健康管家">
          <el-select v-model="assignForm.caregiverId" style="width: 100%">
            <el-option v-for="caregiver in caregivers" :key="caregiver.id" :label="caregiver.nickname" :value="caregiver.id" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :disabled="assignSubmitting" @click="assignVisible = false">取消</el-button>
        <el-button type="success" :loading="assignSubmitting" @click="submitAssign">确认分配</el-button>
      </template>
    </el-dialog>
  </section>
</template>
