<script setup>
import { computed, onMounted, ref } from "vue";
import { ElMessage } from "element-plus";
import { approvalApi } from "../../api/modules";

const pageLoading = ref(false);
const outwards = ref([]);
const backdowns = ref([]);
const loadingRow = ref("");
const lockedRows = ref(new Set());
const actionSummary = ref("当前还没有新的审批动作。");

const outwardPending = computed(() => outwards.value.filter((item) => item.auditStatus === 0));
const backdownPending = computed(() => backdowns.value.filter((item) => item.auditStatus === 0));
const returnedCount = computed(() => outwards.value.filter((item) => item.actualReturnTime).length);

function isRowLocked(key) {
  return lockedRows.value.has(key);
}

function lockRow(key) {
  lockedRows.value = new Set([...lockedRows.value, key]);
}

function unlockRow(key) {
  const next = new Set(lockedRows.value);
  next.delete(key);
  lockedRows.value = next;
}

async function loadData() {
  pageLoading.value = true;
  try {
    const [outwardResponse, backdownResponse] = await Promise.all([
      approvalApi.outward(),
      approvalApi.backdown()
    ]);
    outwards.value = outwardResponse.data;
    backdowns.value = backdownResponse.data;
  } finally {
    pageLoading.value = false;
  }
}

async function auditOutward(id, auditStatus) {
  const rowKey = `outward-${id}`;
  if (isRowLocked(rowKey)) return;

  lockRow(rowKey);
  loadingRow.value = `${rowKey}-${auditStatus}`;
  actionSummary.value = `正在处理外出申请 #${id}...`;
  try {
    await approvalApi.auditOutward(id, { auditStatus });
    actionSummary.value = `外出申请 #${id} 已${auditStatus === 1 ? "通过" : "驳回"}，该记录不能再次审批。`;
    ElMessage.success(auditStatus === 1 ? "外出申请已通过" : "外出申请已驳回");
    await loadData();
  } catch (error) {
    unlockRow(rowKey);
    const message = error?.response?.data?.message || "外出审批失败";
    actionSummary.value = `外出申请 #${id} 处理失败：${message}`;
    ElMessage.error(message);
    console.error(error);
  } finally {
    loadingRow.value = "";
  }
}

async function auditBackdown(id, auditStatus) {
  const rowKey = `backdown-${id}`;
  if (isRowLocked(rowKey)) return;

  lockRow(rowKey);
  loadingRow.value = `${rowKey}-${auditStatus}`;
  actionSummary.value = `正在处理退住申请 #${id}...`;
  try {
    await approvalApi.auditBackdown(id, { auditStatus });
    actionSummary.value = `退住申请 #${id} 已${auditStatus === 1 ? "通过" : "驳回"}，该记录不能再次审批。`;
    ElMessage.success(auditStatus === 1 ? "退住申请已通过" : "退住申请已驳回");
    await loadData();
  } catch (error) {
    unlockRow(rowKey);
    const message = error?.response?.data?.message || "退住审批失败";
    actionSummary.value = `退住申请 #${id} 处理失败：${message}`;
    ElMessage.error(message);
    console.error(error);
  } finally {
    loadingRow.value = "";
  }
}

async function markReturned(id) {
  const rowKey = `return-${id}`;
  if (isRowLocked(rowKey)) return;

  lockRow(rowKey);
  loadingRow.value = rowKey;
  actionSummary.value = `正在登记外出申请 #${id} 的返院信息...`;
  try {
    await approvalApi.markReturned(id);
    actionSummary.value = `外出申请 #${id} 已完成返院登记，对应床位状态已恢复。`;
    ElMessage.success("返院登记已完成");
    await loadData();
  } catch (error) {
    unlockRow(rowKey);
    const message = error?.response?.data?.message || "返院登记失败";
    actionSummary.value = `外出申请 #${id} 返院登记失败：${message}`;
    ElMessage.error(message);
    console.error(error);
  } finally {
    loadingRow.value = "";
  }
}

onMounted(loadData);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="metric-grid">
      <div class="page-card metric-card">
        <span class="metric-label">外出待审批</span>
        <strong class="metric-value">{{ outwardPending.length }}</strong>
        <div class="metric-foot">待审批记录只能处理一次，首次点击后会立即锁定该行。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">退住待审批</span>
        <strong class="metric-value">{{ backdownPending.length }}</strong>
        <div class="metric-foot">通过或驳回只允许一次选择，不再允许反复切换。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">已返院登记</span>
        <strong class="metric-value">{{ returnedCount }}</strong>
        <div class="metric-foot">返院登记点击后也会立即锁定，避免重复提交。</div>
      </div>
      <div class="page-card metric-card">
        <span class="metric-label">审批总量</span>
        <strong class="metric-value">{{ outwards.length + backdowns.length }}</strong>
        <div class="metric-foot">本页统一处理外出与退住两个流程。</div>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">操作反馈</div>
      <h2 class="page-title">审批结果</h2>
      <p class="page-desc">{{ actionSummary }}</p>
    </div>

    <div class="two-column">
      <div class="page-card">
        <div class="section-eyebrow">外出审批</div>
        <h2 class="page-title">外出申请队列</h2>
        <el-table :data="outwards" style="margin-top: 18px">
          <el-table-column prop="id" label="申请号" width="90" />
          <el-table-column prop="customerId" label="客户ID" width="100" />
          <el-table-column prop="outgoingReason" label="外出事由" />
          <el-table-column prop="expectedReturnTime" label="预计返院" width="180" />
          <el-table-column label="审批状态" width="120">
            <template #default="{ row }">
              <span class="status-pill status-warning">
                {{ row.auditStatus === 0 ? "待审批" : row.auditStatus === 1 ? "已通过" : "已驳回" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="返院状态" width="120">
            <template #default="{ row }">
              <span class="status-pill" :class="row.actualReturnTime ? 'status-success' : 'status-info'">
                {{ row.actualReturnTime ? "已返院" : "未返院" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="320">
            <template #default="{ row }">
              <div style="display:flex;gap:8px;flex-wrap:wrap;">
                <el-button
                  v-if="row.auditStatus === 0"
                  type="success"
                  plain
                  size="small"
                  :disabled="isRowLocked(`outward-${row.id}`)"
                  :loading="loadingRow === `outward-${row.id}-1`"
                  @click="auditOutward(row.id, 1)"
                >
                  通过
                </el-button>
                <el-button
                  v-if="row.auditStatus === 0"
                  type="danger"
                  plain
                  size="small"
                  :disabled="isRowLocked(`outward-${row.id}`)"
                  :loading="loadingRow === `outward-${row.id}-2`"
                  @click="auditOutward(row.id, 2)"
                >
                  驳回
                </el-button>
                <el-button
                  v-if="row.auditStatus === 1 && !row.actualReturnTime"
                  type="primary"
                  plain
                  size="small"
                  :disabled="isRowLocked(`return-${row.id}`)"
                  :loading="loadingRow === `return-${row.id}`"
                  @click="markReturned(row.id)"
                >
                  登记返院
                </el-button>
                <span
                  v-if="row.auditStatus !== 0 && !(row.auditStatus === 1 && !row.actualReturnTime)"
                  class="page-desc"
                >
                  审批已锁定，不能再次选择
                </span>
              </div>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="page-card">
        <div class="section-eyebrow">退住审批</div>
        <h2 class="page-title">退住申请队列</h2>
        <el-table :data="backdowns" style="margin-top: 18px">
          <el-table-column prop="id" label="申请号" width="90" />
          <el-table-column prop="customerId" label="客户ID" width="100" />
          <el-table-column prop="retreatReason" label="退住原因" />
          <el-table-column prop="retreatTime" label="退住时间" width="180" />
          <el-table-column label="审批状态" width="120">
            <template #default="{ row }">
              <span class="status-pill status-danger">
                {{ row.auditStatus === 0 ? "待审批" : row.auditStatus === 1 ? "已通过" : "已驳回" }}
              </span>
            </template>
          </el-table-column>
          <el-table-column label="操作" width="260">
            <template #default="{ row }">
              <div v-if="row.auditStatus === 0" style="display:flex;gap:8px;">
                <el-button
                  type="success"
                  plain
                  size="small"
                  :disabled="isRowLocked(`backdown-${row.id}`)"
                  :loading="loadingRow === `backdown-${row.id}-1`"
                  @click="auditBackdown(row.id, 1)"
                >
                  通过
                </el-button>
                <el-button
                  type="danger"
                  plain
                  size="small"
                  :disabled="isRowLocked(`backdown-${row.id}`)"
                  :loading="loadingRow === `backdown-${row.id}-2`"
                  @click="auditBackdown(row.id, 2)"
                >
                  驳回
                </el-button>
              </div>
              <span v-else class="page-desc">审批已锁定，不能再次选择</span>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </div>
  </section>
</template>
