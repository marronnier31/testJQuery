<script setup>
import { RouterLink } from "vue-router";
import logoUrl from "../../assets/login-logo.png";
import styles from "./FindPage.module.css";
</script>

<template>
  <main :class="styles.page">
    <section :class="styles.card">
      <div :class="styles.logoBox">
        <img :src="logoUrl" alt="HappyJob" :class="styles.logo" />
      </div>

      <div :class="styles.header">
        <h1 :class="styles.title">아이디/비밀번호 찾기</h1>
      </div>

      <section :class="styles.placeholder">
        <div :class="styles.badge">구현 예정</div>
        <h2 :class="styles.placeholderTitle">아이디/비밀번호 찾기</h2>
        <p :class="styles.placeholderText">
          가입 정보로 아이디를 찾거나 임시 비밀번호 발급을 요청하는 페이지입니다.
        </p>
      </section>

      <div :class="styles.footer">
        <RouterLink to="/login" :class="styles.link">로그인으로 돌아가기</RouterLink>
        <RouterLink to="/register" :class="styles.link">회원가입</RouterLink>
      </div>
    </section>
  </main>
</template>
