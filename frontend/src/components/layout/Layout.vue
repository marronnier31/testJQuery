<script setup>
import { RouterView } from "vue-router";
import Header from "./Header.vue";
import Sidebar from "./Sidebar.vue";
import styles from "./Layout.module.css";
</script>

<template>
  <div :class="styles.wrapper">
    <Sidebar />

    <div :class="styles.main">
      <Header />
      <main :class="styles.content">
        <RouterView />
      </main>
    </div>
  </div>
</template>
