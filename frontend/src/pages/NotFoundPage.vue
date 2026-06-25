<script setup>
import { useRoute } from "vue-router";
import styles from "./NotFoundPage.module.css";

const route = useRoute();
</script>

<template>
  <div :class="styles.container">
    <div :class="styles.icon">?</div>
    <h2 :class="styles.title">라우트 미등록</h2>
    <p :class="styles.description">
      router/index.js에 아래 경로에 대한 route가 아직 없습니다.
    </p>
    <code :class="styles.path">{{ route.path }}</code>
    <p :class="styles.guide">
      router/index.js에 해당 경로와 컴포넌트를 추가하세요.
    </p>
  </div>
</template>
