<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { nursingApi } from "../../api/modules";

const pageLoading = ref(false);
const submitLoading = ref(false);
const levels = ref([]);
const dialogVisible = ref(false);
const editingId = ref(null);
const actionSummary = ref("护理等级可用于服务分层与标准化配置。");
const filters = reactive({
  keyword: "",
  status: ""
});
const form = reactive({
  levelName: "",
  levelStatus: 1
});

const filteredLevels = computed(() =>
  levels.value.filter((level) => {
    const matchKeyword = !filters.keyword || level.levelName?.includes(filters.keyword);
    const matchStatus = !filters.status || level.levelStatus === Number(filters.status);
    return matchKeyword && matchStatus;
  })
);

async function loadLevels() {
  pageLoading.value = true;
  try {
    const response = await nursingApi.levels();
    levels.value = response.data;
  } finally {
    pageLoading.value = false;
  }
}

function openCreate() {
  editingId.value = null;
  Object.assign(form, {
    levelName: "",
    levelStatus: 1
  });
  dialogVisible.value = true;
}

function openEdit(level) {
  editingId.value = level.id;
  Object.assign(form, { ...level });
  dialogVisible.value = true;
}

async function submitForm() {
  if (!form.levelName) {
    ElMessage.warning("请填写护理等级名称");
    return;
  }
  submitLoading.value = true;
  actionSummary.value = editingId.value ? "正在更新护理等级..." : "正在新增护理等级...";
  try {
    if (editingId.value) {
      await nursingApi.updateLevel(editingId.value, { ...form });
      actionSummary.value = `护理等级 ${form.levelName} 已更新。`;
      ElMessage.success("护理等级已更新");
    } else {
      await nursingApi.createLevel({ ...form });
      actionSummary.value = `护理等级 ${form.levelName} 已创建。`;
      ElMessage.success("护理等级已创建");
    }
    dialogVisible.value = false;
    await loadLevels();
  } catch (error) {
    actionSummary.value = "护理等级提交失败，请稍后重试。";
    ElMessage.error("护理等级提交失败");
    console.error(error);
  } finally {
    submitLoading.value = false;
  }
}

onMounted(loadLevels);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="page-card">
      <div class="section-eyebrow">分层策略</div>
      <h2 class="page-title">护理等级</h2>
      <p class="page-desc">通过护理等级定义不同服务深度，便于客户归类和护理资源配置。</p>
      <div style="margin-top:16px; display:flex; gap:12px; flex-wrap:wrap;">
        <el-input v-model="filters.keyword" placeholder="搜索护理等级" clearable style="max-width: 260px;" />
        <el-select v-model="filters.status" placeholder="状态筛选" clearable style="width: 160px;">
          <el-option label="启用" :value="1" />
          <el-option label="停用" :value="2" />
        </el-select>
        <el-button type="success" @click="openCreate">新增护理等级</el-button>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">操作反馈</div>
      <h2 class="page-title">最近状态</h2>
      <p class="page-desc">{{ actionSummary }}</p>
    </div>

    <div class="page-card">
      <el-table :data="filteredLevels">
        <el-table-column prop="levelName" label="等级名称" min-width="200" />
        <el-table-column label="状态" width="140">
          <template #default="{ row }">
            <span class="status-pill" :class="row.levelStatus === 1 ? 'status-success' : 'status-warning'">
              {{ row.levelStatus === 1 ? "启用" : "停用" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="说明" min-width="260">
          <template #default="{ row }">
            <span class="page-desc">
              {{ row.levelStatus === 1 ? "当前等级可参与客户护理分配。" : "当前等级已停用，不建议继续分配。" }}
            </span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" plain size="small" @click="openEdit(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑护理等级' : '新增护理等级'" width="520px">
      <el-form label-position="top">
        <el-form-item label="等级名称">
          <el-input v-model="form.levelName" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="form.levelStatus" style="width: 100%">
            <el-option :value="1" label="启用" />
            <el-option :value="2" label="停用" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :disabled="submitLoading" @click="dialogVisible = false">取消</el-button>
        <el-button type="success" :loading="submitLoading" @click="submitForm">保存等级</el-button>
      </template>
    </el-dialog>
  </section>
</template>
