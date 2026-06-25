<script setup>
import { reactive, ref } from "vue";
import { RouterLink, useRouter } from "vue-router";
import api from "../../api/axios";
import { useAuth } from "../../composables/useAuth";
import logoUrl from "../../assets/login-logo.png";
import styles from "./LoginPage.module.css";

const router = useRouter();
const { login, getLandingPath } = useAuth();

const form = reactive({
  lgn_Id: "",
  pwd: "",
});

const error = ref("");
const loading = ref(false);
const showPassword = ref(false);

async function handleSubmit() {
  error.value = "";

  if (!form.lgn_Id || !form.pwd) {
    error.value = "아이디와 비밀번호를 입력해 주세요.";
    return;
  }

  loading.value = true;

  try {
    const params = new URLSearchParams();
    params.append("lgn_Id", form.lgn_Id);
    params.append("pwd", form.pwd);

    const response = await api.post("/loginProc.do", params);
    const data = response.data;

    if (data.result === "SUCCESS") {
      if (data.chk_tem_password === "Y") {
        alert(data.resultMsg);
      }

      login({
        loginId: data.loginId,
        userNm: data.userNm,
        userType: data.userType,
        usrMnuAtrt: data.usrMnuAtrt,
        serverName: data.serverName,
      });

      router.push(getLandingPath(data.userType));
      return;
    }

    error.value = data.resultMsg || "로그인에 실패했습니다.";
  } catch (err) {
    error.value = "서버와 연결할 수 없습니다. 잠시 후 다시 시도해 주세요.";
    console.error("로그인 오류:", err);
  } finally {
    loading.value = false;
  }
}
</script>

<template>
  <main :class="styles.page">
    <section :class="styles.card">
      <div :class="styles.logoBox">
        <img :src="logoUrl" alt="HappyJob" :class="styles.logo" />
      </div>

      <div :class="styles.header">
        <h1 :class="styles.title">로그인</h1>
      </div>

      <form :class="styles.form" @submit.prevent="handleSubmit">
        <div :class="styles.field">
          <input
            id="lgn_Id"
            v-model="form.lgn_Id"
            type="text"
            name="lgn_Id"
            placeholder="아이디"
            aria-label="아이디"
            :class="styles.input"
            autocomplete="username"
            autofocus
          />
        </div>

        <div :class="styles.field">
          <div :class="styles.passwordBox">
            <input
              id="pwd"
              v-model="form.pwd"
              :type="showPassword ? 'text' : 'password'"
              name="pwd"
              placeholder="비밀번호"
              aria-label="비밀번호"
              :class="[styles.input, styles.passwordInput]"
              autocomplete="current-password"
            />
            <button
              type="button"
              :class="styles.eyeButton"
              :aria-label="showPassword ? '비밀번호 숨기기' : '비밀번호 보기'"
              @click="showPassword = !showPassword"
            >
              <svg viewBox="0 0 24 24" aria-hidden="true">
                <path
                  d="M2.5 12s3.5-6 9.5-6 9.5 6 9.5 6-3.5 6-9.5 6-9.5-6-9.5-6Z"
                />
                <circle cx="12" cy="12" r="3" />
                <path v-if="showPassword" d="M4 4l16 16" />
              </svg>
            </button>
          </div>
        </div>

        <p v-if="error" :class="styles.error">{{ error }}</p>

        <button type="submit" :class="styles.button" :disabled="loading">
          {{ loading ? "로그인 중..." : "로그인" }}
        </button>
      </form>

      <div :class="styles.links">
        <RouterLink to="/register" :class="styles.link">회원가입</RouterLink>
        <span :class="styles.divider">|</span>
        <RouterLink to="/find" :class="styles.link">
          아이디/비밀번호 찾기
        </RouterLink>
      </div>
    </section>
  </main>
</template>
