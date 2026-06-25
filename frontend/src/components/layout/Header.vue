<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";
import { useAuth } from "../../composables/useAuth";
import { normalizeMenuUrl } from "../../router/menuPaths";
import styles from "./Header.module.css";

const route = useRoute();
const { user } = useAuth();

const staticMap = {
  "/admin/dashboard": "대시보드",
  "/admin/exam/schedule": "시험 일정",
  "/admin/test-exam": "시험 문제",
  "/admin/courseManagement": "강의 관리",
  "/admin/classrooms": "강의실 관리",
  "/admin/stu": "학생 목록",
  "/admin/inst": "강사 목록",
  "/admin/qna": "Q&A",
  "/admin/notices": "공지사항",
  "/stu/courses": "전체 강의 목록",
  "/stu/my-courses": "나의 강의",
  "/stu/materials": "학습 자료",
  "/stu/assignments": "과제 목록",
  "/stu/assignments-result": "과제 결과",
  "/stu/exams": "시험 목록",
  "/stu/qna": "Q&A",
  "/stu/notices": "공지사항",
  "/stu/my-page": "마이페이지",
  "/inst/course-plan": "강의 계획서",
  "/inst/course-list": "강의 목록",
  "/inst/attendance": "출석 관리",
  "/inst/materials": "학습 자료",
  "/inst/exams": "시험 목록",
  "/inst/exam-register": "시험 등록",
  "/inst/assignments": "과제 목록",
  "/inst/submissions": "제출된 과제 목록",
  "/inst/qna": "Q&A",
  "/inst/notices": "공지사항",
  "/inst/my-page": "마이페이지",
  "/survey": "설문 조사",
};

function findMenuTitle(pathname, menus = []) {
  for (const menu of menus) {
    if (normalizeMenuUrl(menu.mnu_url) === pathname) return menu.mnu_nm;

    for (const sub of menu.nodeList ?? []) {
      if (normalizeMenuUrl(sub.mnu_url) === pathname) return sub.mnu_nm;
    }
  }

  return null;
}

const pageTitle = computed(() => {
  return (
    staticMap[route.path] ??
    findMenuTitle(route.path, user.value?.usrMnuAtrt) ??
    "HappyJob LMS"
  );
});
</script>

<template>
  <header :class="styles.header">
    <div :class="styles.breadcrumb">
      <span :class="styles.pageName">{{ pageTitle }}</span>
    </div>
  </header>
</template>
