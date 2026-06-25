import { createRouter, createWebHistory } from "vue-router";
import { getLandingPath, user } from "../composables/useAuth";
import Layout from "../components/layout/Layout.vue";
import LoginPage from "../pages/login/LoginPage.vue";
import RegisterPage from "../pages/register/RegisterPage.vue";
import FindPage from "../pages/find/FindPage.vue";
import DashboardPage from "../pages/dashboard/DashboardPage.vue";
import MyPage from "../pages/mypage/MyPage.vue";
import LecturePage from "../pages/lecture/LecturePage.vue";
import AttendancePage from "../pages/attendance/AttendancePage.vue";
import HomeworkPage from "../pages/homework/HomeworkPage.vue";
import ExamPage from "../pages/exam/ExamPage.vue";
import MaterialPage from "../pages/material/MaterialPage.vue";
import QnaPage from "../pages/qna/QnaPage.vue";
import SurveyPage from "../pages/survey/SurveyPage.vue";
import NoticePage from "../pages/notice/NoticePage.vue";
import AdminUsersPage from "../pages/admin/users/AdminUsersPage.vue";
import AdminLecturePage from "../pages/admin/lecture/AdminLecturePage.vue";
import AdminClassroomPage from "../pages/admin/classroom/AdminClassroomPage.vue";
import AdminExamPage from "../pages/admin/exam/AdminExamPage.vue";
import NotFoundPage from "../pages/NotFoundPage.vue";

const roles = {
  admin: ["A"],
  student: ["S"],
  instructor: ["I"],
  authenticated: ["A", "S", "I"],
};

const routes = [
  { path: "/login", component: LoginPage, meta: { publicOnly: true } },
  { path: "/register", component: RegisterPage, meta: { public: true } },
  { path: "/find", component: FindPage, meta: { public: true } },
  { path: "/find-id", redirect: "/find" },
  { path: "/find-pw", redirect: "/find" },
  {
    path: "/",
    component: Layout,
    meta: { requiresAuth: true },
    children: [
      { path: "", redirect: () => getLandingPath() },
      {
        path: "admin/dashboard",
        component: DashboardPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/exam/schedule",
        component: AdminExamPage,
        meta: { allowedRoles: roles.admin },
      },
      { path: "admin/test-schedule", redirect: "/admin/exam/schedule" },
      {
        path: "admin/test-exam",
        component: AdminExamPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/courseManagement",
        component: AdminLecturePage,
        meta: { allowedRoles: roles.admin },
      },
      { path: "admin/courses", redirect: "/admin/courseManagement" },
      {
        path: "admin/classrooms",
        component: AdminClassroomPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/stu",
        component: AdminUsersPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/inst",
        component: AdminUsersPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/qna",
        component: QnaPage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "admin/notices",
        component: NoticePage,
        meta: { allowedRoles: roles.admin },
      },
      {
        path: "stu/courses",
        component: LecturePage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/my-courses",
        component: LecturePage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/materials",
        component: MaterialPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/assignments",
        component: HomeworkPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/assignments-result",
        component: HomeworkPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/exams",
        component: ExamPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/qna",
        component: QnaPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "stu/notices",
        component: NoticePage,
        meta: { allowedRoles: roles.student },
      },
      { path: "stu/surveys", redirect: "/survey" },
      {
        path: "stu/my-page",
        component: MyPage,
        meta: { allowedRoles: roles.student },
      },
      {
        path: "inst/course-plan",
        component: AttendancePage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/course-list",
        component: LecturePage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/attendance",
        component: AttendancePage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/materials",
        component: MaterialPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/exams",
        component: ExamPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/exam-register",
        component: ExamPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/assignments",
        component: HomeworkPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/submissions",
        component: HomeworkPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/qna",
        component: QnaPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "inst/notices",
        component: NoticePage,
        meta: { allowedRoles: roles.instructor },
      },
      { path: "inst/surveys", redirect: "/survey" },
      {
        path: "inst/my-page",
        component: MyPage,
        meta: { allowedRoles: roles.instructor },
      },
      {
        path: "survey",
        component: SurveyPage,
        meta: { allowedRoles: roles.authenticated },
      },
      { path: "admin/surveys", redirect: "/survey" },
      { path: "survey/survey.do", redirect: "/survey" },
      { path: ":pathMatch(.*)*", component: NotFoundPage },
    ],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to) => {
  if (to.meta.requiresAuth && !user.value) {
    return { path: "/login", query: { redirect: to.fullPath } };
  }

  if (to.meta.publicOnly && user.value) {
    const landingPath = getLandingPath();
    return landingPath === to.path ? true : landingPath;
  }

  if (
    to.meta.allowedRoles &&
    !to.meta.allowedRoles.includes(user.value?.userType)
  ) {
    return getLandingPath();
  }

  return true;
});

export default router;
