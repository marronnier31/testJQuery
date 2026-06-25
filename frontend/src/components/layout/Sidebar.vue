<script setup>
import { computed, ref, watch } from "vue";
import { RouterLink, useRoute, useRouter } from "vue-router";
import api from "../../api/axios";
import { useAuth } from "../../composables/useAuth";
import { normalizeMenuUrl } from "../../router/menuPaths";
import logoUrl from "../../assets/login-logo.png";
import styles from "./Sidebar.module.css";

const route = useRoute();
const router = useRouter();
const { user, logout } = useAuth();
const openMenuId = ref(null);

const userTypeLabel = {
  S: "학생",
  I: "강사",
  A: "관리자",
};

const iconByName = {
  대시보드: "dashboard",
  "시험 관리": "exam",
  "시험/과제": "exam",
  "강의 운영": "lecture",
  "사용자 관리": "users",
  "커뮤니티 관리": "qna",
  "나의 강의 관리": "lecture",
  "나의 강의": "lecture",
  커뮤니티: "qna",
  "학습 관리": "homework",
  "수강 관리": "lecture",
  마이페이지: "mypage",
};

const instructorExamUrls = [
  "/inst/exams",
  "/inst/exam-register",
  "/inst/assignments",
  "/inst/submissions",
];

const normalizedMenus = computed(() => {
  return normalizeMenus(user.value?.usrMnuAtrt ?? [], user.value?.userType);
});

watch(
  () => route.path,
  (pathname) => {
    openMenuId.value = findOpenMenuIdForPath(normalizedMenus.value, pathname);
  },
  { immediate: true },
);

function toggleMenu(menuId) {
  openMenuId.value = openMenuId.value === menuId ? null : menuId;
}

function getMenuIcon(iconCode) {
  return iconMap[iconCode] ?? "-";
}

function normalizeIconCode(menu) {
  if (!menu.mnu_ico_cod || menu.mnu_ico_cod === "menu000") {
    return iconByName[menu.mnu_nm] ?? "admin";
  }

  return menu.mnu_ico_cod;
}

function normalizeMenuNode(menu) {
  return {
    ...menu,
    mnu_url: normalizeMenuUrl(menu.mnu_url),
    mnu_ico_cod: normalizeIconCode(menu),
    nodeList: (menu.nodeList ?? []).map((node) => ({
      ...node,
      mnu_url: normalizeMenuUrl(node.mnu_url),
    })),
  };
}

function splitInstructorCourseMenu(menu) {
  const courseNodes = menu.nodeList.filter(
    (node) => !instructorExamUrls.includes(node.mnu_url),
  );
  const examNodes = menu.nodeList.filter((node) =>
    instructorExamUrls.includes(node.mnu_url),
  );

  return [
    {
      ...menu,
      nodeList: courseNodes,
    },
    {
      mnu_id: `${menu.mnu_id}_exam`,
      mnu_nm: "시험/과제",
      mnu_url: null,
      mnu_ico_cod: "exam",
      nodeList: examNodes,
    },
  ].filter((nextMenu) => nextMenu.nodeList.length || nextMenu.mnu_url);
}

function normalizeMenus(menus, userType) {
  const result = [];

  for (const rawMenu of menus) {
    const menu = normalizeMenuNode(rawMenu);
    const hasInstructorExamItems = menu.nodeList.some((node) =>
      instructorExamUrls.includes(node.mnu_url),
    );

    if (userType === "I" && hasInstructorExamItems) {
      result.push(...splitInstructorCourseMenu(menu));
      continue;
    }

    result.push(menu);
  }

  return result;
}

function isMenuGroupActive(menu, pathname) {
  return (menu.nodeList ?? []).some(
    (sub) => normalizeMenuUrl(sub.mnu_url) === pathname,
  );
}

function findOpenMenuIdForPath(menus, pathname) {
  const matchedMenu = menus.find((menu) => isMenuGroupActive(menu, pathname));
  return matchedMenu?.mnu_id ?? null;
}

async function handleLogout() {
  try {
    await api.get("/loginOut.do");
  } catch {
    // 서버 응답과 관계없이 클라이언트 세션은 정리합니다.
  }

  logout();
  router.push("/login");
}
</script>

<template>
  <aside :class="styles.sidebar">
    <RouterLink to="/" :class="styles.logoLink">
      <img :src="logoUrl" alt="HappyJob" :class="styles.logo" />
      <span :class="styles.logoSub">LMS</span>
    </RouterLink>

    <div :class="styles.userInfo">
      <div :class="styles.userRow">
        <div :class="styles.avatar">{{ user?.userNm?.charAt(0) ?? "?" }}</div>
        <div :class="styles.userDetail">
          <span :class="styles.userName">{{ user?.userNm }}</span>
          <span :class="styles.userType">
            {{ userTypeLabel[user?.userType] ?? user?.userType }}
          </span>
        </div>
      </div>
      <button type="button" :class="styles.logoutBtn" @click="handleLogout">
        로그아웃
      </button>
    </div>

    <nav :class="styles.nav">
      <div
        v-for="menu in normalizedMenus"
        :key="menu.mnu_id"
        :class="styles.menuGroup"
      >
        <template v-if="menu.nodeList?.length">
          <button
            type="button"
            :class="[
              styles.menuItem,
              isMenuGroupActive(menu, route.path) ? styles.menuItemActive : '',
            ]"
            :aria-expanded="openMenuId === menu.mnu_id"
            @click="toggleMenu(menu.mnu_id)"
          >
            <span :class="styles.menuName">{{ menu.mnu_nm }}</span>
            <span
              :class="[
                styles.arrow,
                openMenuId === menu.mnu_id ? styles.arrowOpen : '',
              ]"
            >
              ▾
            </span>
          </button>

          <ul v-if="openMenuId === menu.mnu_id" :class="styles.subMenu">
            <li v-for="sub in menu.nodeList" :key="sub.mnu_id">
              <RouterLink
                v-slot="{ href, navigate, isActive }"
                :to="normalizeMenuUrl(sub.mnu_url)"
                custom
              >
                <a
                  :href="href"
                  :class="[
                    styles.subItem,
                    isActive ? styles.subItemActive : '',
                  ]"
                  @click="navigate"
                >
                  {{ sub.mnu_nm }}
                </a>
              </RouterLink>
            </li>
          </ul>
        </template>

        <RouterLink
          v-else-if="menu.mnu_url"
          v-slot="{ href, navigate, isActive }"
          :to="normalizeMenuUrl(menu.mnu_url)"
          custom
        >
          <a
            :href="href"
            :class="[styles.menuItem, isActive ? styles.menuItemActive : '']"
            @click="navigate"
          >
            <span :class="styles.menuName">{{ menu.mnu_nm }}</span>
          </a>
        </RouterLink>
      </div>
    </nav>
  </aside>
</template>
