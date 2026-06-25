<script setup>
import styles from "./PlaceholderPage.module.css";

defineProps({
  title: {
    type: String,
    required: true,
  },
  description: {
    type: String,
    default: "",
  },
});
</script>

<template>
  <div :class="styles.container">
    <div :class="styles.badge">구현 예정</div>
    <h2 :class="styles.title">{{ title }}</h2>
    <p v-if="description" :class="styles.description">{{ description }}</p>
  </div>
</template>
