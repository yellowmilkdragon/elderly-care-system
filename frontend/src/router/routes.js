import LoginView from "../views/auth/LoginView.vue";
import DashboardView from "../views/dashboard/DashboardView.vue";
import CustomerListView from "../views/customer/CustomerListView.vue";
import BedOverviewView from "../views/bed/BedOverviewView.vue";
import NursingItemView from "../views/nursing/NursingItemView.vue";
import NursingLevelView from "../views/nursing/NursingLevelView.vue";
import ApprovalView from "../views/approval/ApprovalView.vue";
import CaregiverView from "../views/caregiver/CaregiverView.vue";
import UserManagementView from "../views/system/UserManagementView.vue";
import MainLayout from "../components/layout/MainLayout.vue";

export const routes = [
  {
    path: "/login",
    name: "login",
    component: LoginView,
    meta: { public: true }
  },
  {
    path: "/",
    component: MainLayout,
    redirect: "/dashboard",
    children: [
      { path: "approval", redirect: "/approvals" },
      { path: "dashboard", name: "dashboard", component: DashboardView, meta: { title: "工作台" } },
      { path: "customers", name: "customers", component: CustomerListView, meta: { title: "客户管理" } },
      { path: "beds", name: "beds", component: BedOverviewView, meta: { title: "床位管理" } },
      { path: "nursing/items", name: "nursing-items", component: NursingItemView, meta: { title: "护理项目" } },
      { path: "nursing/levels", name: "nursing-levels", component: NursingLevelView, meta: { title: "护理等级" } },
      { path: "approvals", name: "approvals", component: ApprovalView, meta: { title: "审批管理" } },
      { path: "caregivers", name: "caregivers", component: CaregiverView, meta: { title: "健康管家" } },
      { path: "users", name: "users", component: UserManagementView, meta: { title: "用户管理" } }
    ]
  }
];
