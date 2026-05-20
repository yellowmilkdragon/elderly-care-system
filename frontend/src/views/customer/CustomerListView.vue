<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { customerApi, userApi } from "../../api/modules";

const pageLoading = ref(false);
const submitLoading = ref(false);
const customers = ref([]);
const caregivers = ref([]);
const dialogVisible = ref(false);
const detailVisible = ref(false);
const editingId = ref(null);
const selectedCustomer = ref(null);
const actionSummary = ref("客户档案已接入可新增、可编辑、可筛选的交互流程。");
const filters = reactive({
  keyword: "",
  levelId: "",
  caregiverId: ""
});
const form = reactive({
  customerName: "",
  customerAge: 75,
  roomNo: "",
  buildingNo: "606",
  bedId: 1,
  userId: 3,
  levelId: 1,
  bloodType: "A型",
  contactTel: "",
  familyMember: "",
  checkinDate: "",
  expirationDate: ""
});

const levelMap = {
  1: "一级护理",
  2: "二级护理",
  3: "三级护理"
};

const customerCards = computed(() =>
  customers.value.map((customer) => {
    const caregiver = caregivers.value.find((item) => item.id === customer.userId);
    return {
      ...customer,
      caregiverName: caregiver?.nickname || "未分配",
      careLevel: levelMap[customer.levelId] || `护理等级 ${customer.levelId}`
    };
  })
);

const filteredCustomers = computed(() =>
  customerCards.value.filter((customer) => {
    const matchKeyword =
      !filters.keyword ||
      customer.customerName?.includes(filters.keyword) ||
      customer.roomNo?.includes(filters.keyword) ||
      customer.familyMember?.includes(filters.keyword);
    const matchLevel = !filters.levelId || customer.levelId === Number(filters.levelId);
    const matchCaregiver = !filters.caregiverId || customer.userId === Number(filters.caregiverId);
    return matchKeyword && matchLevel && matchCaregiver;
  })
);

async function loadData() {
  pageLoading.value = true;
  try {
    const [customerResponse, caregiverResponse] = await Promise.all([
      customerApi.list(),
      userApi.caregivers()
    ]);
    customers.value = customerResponse.data;
    caregivers.value = caregiverResponse.data;
  } finally {
    pageLoading.value = false;
  }
}

function resetForm() {
  Object.assign(form, {
    customerName: "",
    customerAge: 75,
    roomNo: "",
    buildingNo: "606",
    bedId: 1,
    userId: caregivers.value[0]?.id ?? 3,
    levelId: 1,
    bloodType: "A型",
    contactTel: "",
    familyMember: "",
    checkinDate: "2026-05-17",
    expirationDate: "2026-11-17"
  });
}

function openCreate() {
  editingId.value = null;
  resetForm();
  dialogVisible.value = true;
}

function openEdit(customer) {
  editingId.value = customer.id;
  Object.assign(form, {
    ...customer,
    checkinDate: customer.checkinDate || "2026-05-17",
    expirationDate: customer.expirationDate || "2026-11-17"
  });
  dialogVisible.value = true;
}

function openDetail(customer) {
  selectedCustomer.value = customer;
  detailVisible.value = true;
}

async function submitForm() {
  if (!form.customerName || !form.roomNo) {
    ElMessage.warning("请先填写客户姓名和房间号");
    return;
  }

  submitLoading.value = true;
  actionSummary.value = editingId.value ? "正在更新客户档案..." : "正在创建客户档案...";
  try {
    if (editingId.value) {
      await customerApi.update(editingId.value, { ...form });
      actionSummary.value = `客户 ${form.customerName} 的档案已更新。`;
      ElMessage.success("客户信息已更新");
    } else {
      await customerApi.create({ ...form });
      actionSummary.value = `客户 ${form.customerName} 已创建并加入档案列表。`;
      ElMessage.success("客户已创建");
    }
    dialogVisible.value = false;
    await loadData();
  } catch (error) {
    actionSummary.value = "客户档案提交失败，请检查填写内容后重试。";
    ElMessage.error("客户档案提交失败");
    console.error(error);
  } finally {
    submitLoading.value = false;
  }
}

onMounted(loadData);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="page-card">
      <div class="section-eyebrow">客户档案中心</div>
      <h2 class="page-title">客户管理</h2>
      <p class="page-desc">
        这里把客户基本信息、床位、护理等级和健康管家放在同一工作台里，支持筛选、查看详情与即时编辑。
      </p>
      <div style="margin-top:16px; display:flex; gap:12px; flex-wrap:wrap;">
        <el-input v-model="filters.keyword" placeholder="搜索姓名 / 房间 / 家属" style="max-width: 260px;" clearable />
        <el-select v-model="filters.levelId" placeholder="护理等级" clearable style="width: 160px;">
          <el-option label="一级护理" :value="1" />
          <el-option label="二级护理" :value="2" />
          <el-option label="三级护理" :value="3" />
        </el-select>
        <el-select v-model="filters.caregiverId" placeholder="健康管家" clearable style="width: 180px;">
          <el-option
            v-for="caregiver in caregivers"
            :key="caregiver.id"
            :label="caregiver.nickname"
            :value="caregiver.id"
          />
        </el-select>
        <el-button type="success" @click="openCreate">新增客户</el-button>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">最近状态</div>
      <h2 class="page-title">操作反馈</h2>
      <p class="page-desc">{{ actionSummary }}</p>
    </div>

    <div class="three-column">
      <div v-for="customer in filteredCustomers" :key="customer.id" class="page-card">
        <div style="display:flex;justify-content:space-between;align-items:flex-start;gap:12px;">
          <div>
            <h3 style="margin:0;font-size:20px;">{{ customer.customerName }}</h3>
            <p class="page-desc" style="margin-top:6px;">
              {{ customer.customerAge }} 岁 · {{ customer.bloodType }} · 家属 {{ customer.familyMember || "未登记" }}
            </p>
          </div>
          <span class="status-pill status-info">{{ customer.careLevel }}</span>
        </div>

        <div class="list-stack" style="margin-top:18px;">
          <div class="mini-item">
            <div>
              <strong>房间与床位</strong>
              <span>{{ customer.buildingNo }} 栋 {{ customer.roomNo }} / 床位 {{ customer.bedId }}</span>
            </div>
          </div>
          <div class="mini-item">
            <div>
              <strong>健康管家</strong>
              <span>{{ customer.caregiverName }}</span>
            </div>
          </div>
          <div class="mini-item">
            <div>
              <strong>合同期限</strong>
              <span>{{ customer.checkinDate }} 至 {{ customer.expirationDate }}</span>
            </div>
          </div>
          <div class="mini-item">
            <div>
              <strong>快捷操作</strong>
              <span>支持查看详情和更新档案</span>
            </div>
            <div style="display:flex; gap:8px;">
              <el-button type="primary" plain @click="openDetail(customer)">详情</el-button>
              <el-button type="success" plain @click="openEdit(customer)">编辑</el-button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div v-if="!filteredCustomers.length" class="page-card">
      <div class="section-eyebrow">空结果</div>
      <h2 class="page-title">没有匹配的客户</h2>
      <p class="page-desc">可以调整搜索词、护理等级或健康管家筛选条件后再试。</p>
    </div>

    <el-dialog v-model="dialogVisible" :title="editingId ? '编辑客户' : '新增客户'" width="720px">
      <el-form label-position="top">
        <div class="three-column">
          <el-form-item label="客户姓名">
            <el-input v-model="form.customerName" />
          </el-form-item>
          <el-form-item label="年龄">
            <el-input-number v-model="form.customerAge" :min="60" :max="120" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="血型">
            <el-input v-model="form.bloodType" />
          </el-form-item>
        </div>
        <div class="three-column">
          <el-form-item label="房间号">
            <el-input v-model="form.roomNo" />
          </el-form-item>
          <el-form-item label="床位ID">
            <el-input-number v-model="form.bedId" :min="1" style="width: 100%;" />
          </el-form-item>
          <el-form-item label="楼栋号">
            <el-input v-model="form.buildingNo" />
          </el-form-item>
        </div>
        <div class="three-column">
          <el-form-item label="健康管家">
            <el-select v-model="form.userId" style="width: 100%">
              <el-option
                v-for="caregiver in caregivers"
                :key="caregiver.id"
                :label="caregiver.nickname"
                :value="caregiver.id"
              />
            </el-select>
          </el-form-item>
          <el-form-item label="护理等级">
            <el-select v-model="form.levelId" style="width: 100%">
              <el-option label="一级护理" :value="1" />
              <el-option label="二级护理" :value="2" />
              <el-option label="三级护理" :value="3" />
            </el-select>
          </el-form-item>
          <el-form-item label="联系电话">
            <el-input v-model="form.contactTel" />
          </el-form-item>
        </div>
        <div class="three-column">
          <el-form-item label="入住日期">
            <el-input v-model="form.checkinDate" />
          </el-form-item>
          <el-form-item label="合同到期">
            <el-input v-model="form.expirationDate" />
          </el-form-item>
          <el-form-item label="家属联系人">
            <el-input v-model="form.familyMember" />
          </el-form-item>
        </div>
      </el-form>
      <template #footer>
        <el-button :disabled="submitLoading" @click="dialogVisible = false">取消</el-button>
        <el-button type="success" :loading="submitLoading" @click="submitForm">保存档案</el-button>
      </template>
    </el-dialog>

    <el-drawer v-model="detailVisible" title="客户详情" size="420px">
      <div v-if="selectedCustomer" class="list-stack">
        <div class="mini-item"><strong>姓名</strong><span>{{ selectedCustomer.customerName }}</span></div>
        <div class="mini-item"><strong>年龄</strong><span>{{ selectedCustomer.customerAge }} 岁</span></div>
        <div class="mini-item"><strong>护理等级</strong><span>{{ selectedCustomer.careLevel }}</span></div>
        <div class="mini-item"><strong>房间</strong><span>{{ selectedCustomer.buildingNo }} 栋 {{ selectedCustomer.roomNo }}</span></div>
        <div class="mini-item"><strong>床位</strong><span>{{ selectedCustomer.bedId }}</span></div>
        <div class="mini-item"><strong>健康管家</strong><span>{{ selectedCustomer.caregiverName }}</span></div>
        <div class="mini-item"><strong>家属</strong><span>{{ selectedCustomer.familyMember || "未登记" }}</span></div>
        <div class="mini-item"><strong>电话</strong><span>{{ selectedCustomer.contactTel || "未登记" }}</span></div>
        <div class="mini-item"><strong>合同期</strong><span>{{ selectedCustomer.checkinDate }} 至 {{ selectedCustomer.expirationDate }}</span></div>
      </div>
    </el-drawer>
  </section>
</template>
