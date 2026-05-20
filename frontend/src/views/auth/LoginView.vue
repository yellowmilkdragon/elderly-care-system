<script setup>
import { reactive, ref } from "vue";
import { ElMessage } from "element-plus";
import { useRouter } from "vue-router";
import { useUserStore } from "../../stores/user";
import { authApi } from "../../api/modules";

const router = useRouter();
const userStore = useUserStore();
const submitting = ref(false);

const form = reactive({
  username: "admin",
  password: "123456"
});

async function handleLogin() {
  submitting.value = true;
  try {
    const response = await authApi.login(form);
    userStore.login(response.data);
    ElMessage.success("登录成功，正在进入工作台");
    await router.push("/dashboard");
  } catch (error) {
    const message = error?.response?.data?.message || "登录失败，请确认后端服务已启动";
    ElMessage.error(message);
    console.error(error);
  } finally {
    submitting.value = false;
  }
}
</script>

<template>
  <div class="login-page">
    <div class="login-panel">
      <div class="login-copy">
        <div class="eyebrow">Neusoft Elderly Care</div>
        <h1>东软颐养中心管理系统</h1>
        <p>覆盖入住、床位、护理、审批和健康管家协作的一体化运营后台。</p>
      </div>
      <el-card class="login-card" shadow="never">
        <template #header>
          <span>账号登录</span>
        </template>
        <el-form label-position="top">
          <el-form-item label="用户名">
            <el-input v-model="form.username" />
          </el-form-item>
          <el-form-item label="密码">
            <el-input v-model="form.password" show-password />
          </el-form-item>
          <el-button type="primary" class="login-button" :loading="submitting" @click="handleLogin">
            登录系统
          </el-button>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<style scoped>
.login-page {
  min-height: 100vh;
  display: grid;
  place-items: center;
  background:
    radial-gradient(circle at top left, rgba(35, 100, 170, 0.18), transparent 30%),
    radial-gradient(circle at bottom right, rgba(241, 143, 1, 0.18), transparent 32%),
    linear-gradient(180deg, #eef4fb 0%, #f9fbfd 100%);
  padding: 24px;
}

.login-panel {
  width: min(1080px, 100%);
  display: grid;
  grid-template-columns: 1.2fr 420px;
  gap: 32px;
  align-items: center;
}

.login-copy h1 {
  margin: 10px 0 14px;
  font-size: 48px;
  line-height: 1.1;
}

.eyebrow {
  display: inline-block;
  padding: 8px 12px;
  border-radius: 999px;
  background: rgba(35, 100, 170, 0.1);
  color: var(--ec-primary);
  font-weight: 700;
}

.login-copy p {
  font-size: 18px;
  color: var(--ec-subtext);
  max-width: 560px;
}

.login-card {
  border-radius: 22px;
  border: 1px solid var(--ec-border);
}

.login-button {
  width: 100%;
}
</style>
