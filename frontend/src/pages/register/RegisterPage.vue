<script setup>
import { RouterLink } from "vue-router";
import logoUrl from "../../assets/login-logo.png";
import styles from "./RegisterPage.module.css";
</script>

<template>
  <main :class="styles.page">
    <section :class="styles.card">
      <div :class="styles.logoBox">
        <img :src="logoUrl" alt="HappyJob" :class="styles.logo" />
      </div>

      <div :class="styles.header">
        <h1 :class="styles.title">회원가입</h1>
      </div>

      <section :class="styles.placeholder">
        <div :class="styles.badge">구현 예정</div>
        <h2 :class="styles.placeholderTitle">회원가입</h2>
        <p :class="styles.placeholderText">
          신규 사용자 계정을 등록하는 페이지입니다.
        </p>
      </section>

      <div :class="styles.footer">
        <RouterLink to="/login" :class="styles.link">로그인으로 돌아가기</RouterLink>
        <RouterLink to="/find" :class="styles.link">
          아이디/비밀번호 찾기
        </RouterLink>
      </div>
    </section>
  </main>
</template>
