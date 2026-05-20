<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { nursingApi } from "../../api/modules";

const pageLoading = ref(false);
const submitLoading = ref(false);
const items = ref([]);
const dialogVisible = ref(false);
const editingId = ref(null);
const actionSummary = ref("护理项目支持新增、编辑与状态筛选。");
const filters = reactive({
  keyword: "",
  status: ""
});
const form = reactive({
  serialNumber: "",
  nursingName: "",
  servicePrice: 0,
  executionCycle: "",
  executionTimes: "",
  status: 1,
  message: ""
});

const filteredItems = computed(() =>
  items.value.filter((item) => {
    const matchKeyword =
      !filters.keyword ||
      item.serialNumber?.includes(filters.keyword) ||
      item.nursingName?.includes(filters.keyword);
    const matchStatus = !filters.status || item.status === Number(filters.status);
    return matchKeyword && matchStatus;
  })
);

async function loadItems() {
  pageLoading.value = true;
  try {
    const response = await nursingApi.items();
    items.value = response.data;
  } finally {
    pageLoading.value = false;
  }
}

function resetForm() {
  Object.assign(form, {
    serialNumber: "",
    nursingName: "",
    servicePrice: 0,
    executionCycle: "",
    executionTimes: "",
    status: 1,
    message: ""
  });
}

function openCreate() {
  editingId.value = null;
  resetForm();
  dialogVisible.value = true;
}

function openEdit(item) {
  editingId.value = item.id;
  Object.assign(form, { ...item });
  dialogVisible.value = true;
}

async function submitForm() {
  if (!form.serialNumber || !form.nursingName) {
    ElMessage.warning("请填写项目编号和项目名称");
    return;
  }
  submitLoading.value = true;
  actionSummary.value = editingId.value ? "正在更新护理项目..." : "正在创建护理项目...";
  try {
    const payload = { ...form };
    if (editingId.value) {
      await nursingApi.updateItem(editingId.value, payload);
      actionSummary.value = `护理项目 ${form.nursingName} 已更新。`;
      ElMessage.success("护理项目已更新");
    } else {
      await nursingApi.createItem(payload);
      actionSummary.value = `护理项目 ${form.nursingName} 已创建。`;
      ElMessage.success("护理项目已创建");
    }
    dialogVisible.value = false;
    await loadItems();
  } catch (error) {
    actionSummary.value = "护理项目提交失败，请稍后重试。";
    ElMessage.error("护理项目提交失败");
    console.error(error);
  } finally {
    submitLoading.value = false;
  }
}
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="page-card">
      <div class="section-eyebrow">服务标准库</div>
      <h2 class="page-title">护理项目</h2>
      <p class="page-desc">维护项目编号、执行节奏和价格体系，方便后续服务编排与费用核算。</p>
      <div style="margin-top: 16px; display:flex; gap:12px; flex-wrap:wrap;">
        <el-input v-model="filters.keyword" placeholder="搜索编号或项目名称" clearable style="max-width: 260px;" />
        <el-select v-model="filters.status" placeholder="状态筛选" clearable style="width: 160px;">
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="2" />
        </el-select>
        <el-button type="success" @click="openCreate">新增护理项目</el-button>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">操作反馈</div>
      <h2 class="page-title">最近状态</h2>
      <p class="page-desc">{{ actionSummary }}</p>
    </div>

    <div class="page-card">
      <el-table :data="filteredItems">
        <el-table-column prop="serialNumber" label="项目编号" width="140" />
        <el-table-column prop="nursingName" label="项目名称" min-width="180" />
        <el-table-column prop="servicePrice" label="价格" width="120" />
        <el-table-column prop="executionCycle" label="执行周期" width="140" />
        <el-table-column prop="executionTimes" label="执行次数" width="120" />
        <el-table-column label="状态" width="120">
          <template #default="{ row }">
            <span class="status-pill" :class="row.status === 1 ? 'status-success' : 'status-warning'">
              {{ row.status === 1 ? "启用" : "停用" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="说明" min-width="220">
          <template #default="{ row }">
            <span class="page-desc">{{ row.message || "暂无补充说明" }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" plain size="small" @click="openEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑护理项目' : '新增护理项目'" width="720px">
      <el-form label-position="top">
        <div class="three-column">
          <el-form-item label="项目编号">
            <el-input v-model="form.serialNumber" />
          </el-form-item>
          <el-form-item label="项目名称">
            <el-input v-model="form.nursingName" />
          </el-form-item>
          <el-form-item label="价格">
            <el-input-number v-model="form.servicePrice" :min="0" :precision="2" style="width: 100%;" />
          </el-form-item>
        </div>
        <div class="three-column">
          <el-form-item label="执行周期">
            <el-input v-model="form.executionCycle" />
          </el-form-item>
          <el-form-item label="执行次数">
            <el-input v-model="form.executionTimes" />
          </el-form-item>
          <el-form-item label="状态">
            <el-select v-model="form.status" style="width: 100%">
              <el-option :value="1" label="启用" />
              <el-option :value="2" label="停用" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="项目说明">
          <el-input v-model="form.message" type="textarea" :rows="4" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :disabled="submitLoading" @click="dialogVisible = false">取消</el-button>
        <el-button type="success" :loading="submitLoading" @click="submitForm">保存项目</el-button>
      </template>
    </el-dialog>
  </section>
</template>
