const LoginView = () => import("../views/auth/LoginView.vue");
const DashboardView = () => import("../views/dashboard/DashboardView.vue");
const CustomerListView = () => import("../views/customer/CustomerListView.vue");
const BedOverviewView = () => import("../views/bed/BedOverviewView.vue");
const NursingItemView = () => import("../views/nursing/NursingItemView.vue");
const NursingLevelView = () => import("../views/nursing/NursingLevelView.vue");
const ApprovalView = () => import("../views/approval/ApprovalView.vue");
const CaregiverView = () => import("../views/caregiver/CaregiverView.vue");
const UserManagementView = () => import("../views/system/UserManagementView.vue");
const MainLayout = () => import("../components/layout/MainLayout.vue");

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
