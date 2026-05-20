<script setup>
import { computed, onMounted, reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { bedApi, customerApi } from "../../api/modules";

const pageLoading = ref(false);
const overview = ref({});
const rooms = ref([]);
const customers = ref([]);
const availableBeds = ref([]);
const transferVisible = ref(false);
const transferSubmitting = ref(false);
const transferHint = ref("点击“办理调床”后，可立即为老人重新分配空闲床位。");
const lastTransfer = ref(null);
const filters = reactive({
  floor: "",
  status: ""
});
const transferForm = reactive({
  customerId: null,
  newBedId: null
});

const statusMap = {
  1: { label: "空闲", className: "status-success" },
  2: { label: "入住", className: "status-info" },
  3: { label: "外出", className: "status-warning" }
};

const floorSummary = computed(() =>
  rooms.value.reduce((acc, room) => {
    acc[room.roomFloor] ??= { total: 0, occupied: 0, free: 0, outward: 0 };
    room.beds.forEach((bed) => {
      acc[room.roomFloor].total += 1;
      if (bed.bedStatus === 1) acc[room.roomFloor].free += 1;
      if (bed.bedStatus === 2) acc[room.roomFloor].occupied += 1;
      if (bed.bedStatus === 3) acc[room.roomFloor].outward += 1;
    });
    return acc;
  }, {})
);

const selectableCustomers = computed(() => customers.value.filter((item) => item.id != null));
const floors = computed(() => [...new Set(rooms.value.map((item) => item.roomFloor))]);

const filteredRooms = computed(() =>
  rooms.value
    .filter((room) => !filters.floor || room.roomFloor === filters.floor)
    .map((room) => ({
      ...room,
      beds: room.beds.filter((bed) => !filters.status || String(bed.bedStatus) === String(filters.status))
    }))
    .filter((room) => room.beds.length > 0)
);

const smartInsight = computed(() => {
  if ((overview.value.freeBeds ?? 0) <= 1) {
    return "当前空闲床位非常紧张，建议先处理退住和外出返院流程，再安排新入住。";
  }
  if ((overview.value.outwardBeds ?? 0) > 0) {
    return "当前存在外出床位，建议值班人员重点跟进返院时间，避免床位状态滞后。";
  }
  return "床位运行平稳，可以优先优化楼层分布和服务对象就近安排。";
});

async function loadData() {
  pageLoading.value = true;
  try {
    const [overviewResponse, roomsResponse, customerResponse, bedResponse] = await Promise.all([
      bedApi.overview(),
      bedApi.rooms(),
      customerApi.list(),
      bedApi.available()
    ]);
    overview.value = overviewResponse.data;
    rooms.value = roomsResponse.data;
    customers.value = customerResponse.data;
    availableBeds.value = bedResponse.data;
  } finally {
    pageLoading.value = false;
  }
}

function openTransfer() {
  if (!selectableCustomers.value.length) {
    ElMessage.warning("当前没有可调床的客户数据");
    return;
  }
  if (!availableBeds.value.length) {
    ElMessage.warning("当前没有空闲床位可供调配");
    return;
  }
  transferForm.customerId = selectableCustomers.value[0].id;
  transferForm.newBedId = availableBeds.value[0].id;
  transferVisible.value = true;
}

async function submitTransfer() {
  if (!transferForm.customerId || !transferForm.newBedId) {
    ElMessage.warning("请选择客户和目标床位");
    return;
  }
  transferSubmitting.value = true;
  transferHint.value = "正在提交调床申请并刷新床位状态...";
  try {
    const customer = customers.value.find((item) => item.id === transferForm.customerId);
    const bed = availableBeds.value.find((item) => item.id === transferForm.newBedId);
    await bedApi.transfer({ ...transferForm });
    lastTransfer.value = {
      customerName: customer?.customerName || `客户 #${transferForm.customerId}`,
      roomNo: bed?.roomNo || "目标房间",
      bedNo: bed?.bedNo || "目标床位",
      time: new Date().toLocaleString("zh-CN", { hour12: false })
    };
    transferHint.value = "调床已完成，楼层占用与空闲床位已同步刷新。";
    ElMessage.success("调床成功，床位状态已更新");
    transferVisible.value = false;
    await loadData();
  } catch (error) {
    transferHint.value = "调床失败，请检查目标床位是否仍为空闲状态。";
    ElMessage.error("调床失败，请稍后重试");
    console.error(error);
  } finally {
    transferSubmitting.value = false;
  }
}

onMounted(loadData);
</script>

<template>
  <section v-loading="pageLoading" class="page-shell">
    <div class="hero-panel page-card">
      <div>
        <div class="section-eyebrow">床位指挥台</div>
        <h2 class="hero-title">床位不只是状态，而是一张运营地图</h2>
        <p class="page-desc hero-desc">
          你可以从楼层、状态、当前主人三个维度同时观察床位分布，让调床、返院、护理安排更像真实的运营决策。
        </p>
      </div>
      <div class="hero-badges">
        <div class="hero-badge">
          <span>空闲床位</span>
          <strong>{{ overview.freeBeds ?? 0 }}</strong>
        </div>
        <div class="hero-badge">
          <span>系统建议</span>
          <strong>动态分析</strong>
        </div>
      </div>
    </div>

    <div class="page-card">
      <div class="section-eyebrow">床位总览</div>
      <h2 class="page-title">床位管理</h2>
      <p class="page-desc">
        总床位 {{ overview.totalBeds ?? 0 }}，空闲 {{ overview.freeBeds ?? 0 }}，入住 {{ overview.occupiedBeds ?? 0 }}，外出 {{ overview.outwardBeds ?? 0 }}。
      </p>
      <div style="margin-top: 16px; display:flex; gap:12px; align-items:center; flex-wrap:wrap;">
        <el-button type="success" @click="openTransfer">办理调床</el-button>
        <el-select v-model="filters.floor" placeholder="按楼层筛选" clearable style="width: 160px;">
          <el-option v-for="floor in floors" :key="floor" :label="floor" :value="floor" />
        </el-select>
        <el-select v-model="filters.status" placeholder="按状态筛选" clearable style="width: 160px;">
          <el-option label="空闲" :value="1" />
          <el-option label="入住" :value="2" />
          <el-option label="外出" :value="3" />
        </el-select>
        <span class="page-desc">{{ transferHint }}</span>
      </div>
    </div>

    <div class="two-column">
      <div class="page-card">
        <div class="section-eyebrow">智能洞察</div>
        <h2 class="page-title">床位运行建议</h2>
        <p class="page-desc">{{ smartInsight }}</p>
      </div>

      <div v-if="lastTransfer" class="page-card">
        <div class="section-eyebrow">最近一次操作</div>
        <h2 class="page-title">调床已生效</h2>
        <p class="page-desc">
          {{ lastTransfer.customerName }} 已调整至 {{ lastTransfer.roomNo }} 的 {{ lastTransfer.bedNo }}，完成时间为 {{ lastTransfer.time }}。
        </p>
      </div>
    </div>

    <div class="three-column">
      <div v-for="(data, floor) in floorSummary" :key="floor" class="page-card">
        <div class="section-eyebrow">{{ floor }}</div>
        <h3 style="margin:14px 0 10px;font-size:22px;">楼层热区</h3>
        <div class="list-stack">
          <div class="mini-item"><strong>总床位</strong><span>{{ data.total }}</span></div>
          <div class="mini-item"><strong>已入住</strong><span>{{ data.occupied }}</span></div>
          <div class="mini-item"><strong>空闲</strong><span>{{ data.free }}</span></div>
          <div class="mini-item"><strong>外出</strong><span>{{ data.outward }}</span></div>
        </div>
      </div>
    </div>

    <div class="three-column">
      <div v-for="room in filteredRooms" :key="room.id" class="page-card bed-room-card">
        <div style="display:flex;justify-content:space-between;align-items:center;">
          <h3 style="margin:0;font-size:20px;">{{ room.roomFloor }} / {{ room.roomNo }}</h3>
          <span class="status-pill status-info">{{ room.beds.length }} 张床位</span>
        </div>
        <div class="list-stack" style="margin-top:18px;">
          <div v-for="bed in room.beds" :key="bed.id" class="mini-item">
            <div>
              <strong>{{ bed.bedNo }}</strong>
              <span>床位编号 {{ bed.id }} · 当前主人 {{ bed.ownerName }}</span>
            </div>
            <span class="status-pill" :class="statusMap[bed.bedStatus]?.className || 'status-info'">
              {{ statusMap[bed.bedStatus]?.label || "未知" }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="transferVisible" title="办理调床" width="560px">
      <el-form label-position="top">
        <el-form-item label="选择客户">
          <el-select v-model="transferForm.customerId" style="width: 100%">
            <el-option
              v-for="customer in selectableCustomers"
              :key="customer.id"
              :label="customer.customerName"
              :value="customer.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="目标床位">
          <el-select v-model="transferForm.newBedId" style="width: 100%">
            <el-option
              v-for="bed in availableBeds"
              :key="bed.id"
              :label="`${bed.roomNo} - ${bed.bedNo}`"
              :value="bed.id"
            />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button :disabled="transferSubmitting" @click="transferVisible = false">取消</el-button>
        <el-button type="success" :loading="transferSubmitting" @click="submitTransfer">确认调床</el-button>
      </template>
    </el-dialog>
  </section>
</template>
