# Vue 코드 작성 가이드

---

## 1. 화면 ↔ Vue 파일 ↔ 백엔드 API 매핑표

**프론트 기준 경로:** `frontend/src/pages/`

---

### 1.1 공개 페이지 (로그인 없이 접근 가능)

| 화면                 | Vue 파일                                | URL 경로    | 백엔드 API                                               |
| -------------------- | --------------------------------------- | ----------- | -------------------------------------------------------- |
| 로그인               | `pages/login/`<br>`LoginPage.vue`       | `/login`    | `POST /loginProc.do`                                     |
| 회원가입             | `pages/register/`<br>`RegisterPage.vue` | `/register` | `POST /register.do`                                      |
| 아이디/비밀번호 찾기 | `pages/find/`<br>`FindPage.vue`         | `/find`     | `POST /selectFindInfo.do`<br>`POST /selectFindInfoPw.do` |

---

### 1.2 관리자 (A 계정)

| 메뉴                         | Vue 파일                                             | URL 경로                  | 백엔드 Controller                 |
| ---------------------------- | ---------------------------------------------------- | ------------------------- | --------------------------------- |
| 대시보드                     | `pages/dashboard/`<br>`DashboardPage.vue`            | `/admin/dashboard`        | `ADashboardController.java`       |
| 시험 관리 <br> 시험 일정     | `pages/admin/exam/`<br>`AdminExamPage.vue`           | `/admin/exam/schedule`    | `ATestScheduleController.java`    |
| 시험 관리 <br> 시험 문제     | `pages/admin/exam/`<br>`AdminExamPage.vue`           | `/admin/test-exam`        | `ATestController.java`            |
| 강의 운영 <br> 강의 목록     | `pages/admin/lecture/`<br>`AdminLecturePage.vue`     | `/admin/courseManagement` | `CourseManagementController.java` |
| 강의 운영 <br> 강의실 목록   | `pages/admin/classroom/`<br>`AdminClassroomPage.vue` | `/admin/classrooms`       | `CourseClassController.java`      |
| 사용자 관리 <br> 학생 목록   | `pages/admin/users/`<br>`AdminUsersPage.vue`         | `/admin/stu`              | `AUserController.java`            |
| 사용자 관리 <br> 강사 목록   | `pages/admin/users/`<br>`AdminUsersPage.vue`         | `/admin/inst`             | `AUserController.java`            |
| 커뮤니티 관리 <br> Q&A       | `pages/qna/`<br>`QnaPage.vue`                        | `/admin/qna`              | `AQnaController.java`             |
| 커뮤니티 관리 <br> 설문 조사 | `pages/survey/`<br>`SurveyPage.vue`                  | `/survey`                 | `SurveyController.java`           |
| 커뮤니티 관리 <br> 공지 사항 | `pages/notice/`<br>`NoticePage.vue`                  | `/admin/notices`          | `NoticeNewController.java`        |

---

### 1.3 학생 (S 계정)

| 메뉴                          | Vue 파일                                | URL 경로                  | 백엔드 Controller          |
| ----------------------------- | --------------------------------------- | ------------------------- | -------------------------- |
| 수강 관리 <br> 전체 강의 목록 | `pages/lecture/`<br>`LecturePage.vue`   | `/stu/courses`            | `SCourseController.java`   |
| 수강 관리 <br> 나의 강의      | `pages/lecture/`<br>`LecturePage.vue`   | `/stu/my-courses`         | `SCourseController.java`   |
| 학습 관리 <br> 학습 자료      | `pages/material/`<br>`MaterialPage.vue` | `/stu/materials`          | `SMaterialController.java` |
| 학습 관리 <br> 과제 목록      | `pages/homework/`<br>`HomeworkPage.vue` | `/stu/assignments`        | `SHomeworkController.java` |
| 학습 관리 <br> 과제 결과      | `pages/homework/`<br>`HomeworkPage.vue` | `/stu/assignments-result` | `SHomeworkController.java` |
| 학습 관리 <br> 시험 목록      | `pages/exam/`<br>`ExamPage.vue`         | `/stu/exams`              | `STestController.java`     |
| 커뮤니티 <br> Q&A             | `pages/qna/`<br>`QnaPage.vue`           | `/stu/qna`                | `SQnaController.java`      |
| 커뮤니티 <br> 설문 조사       | `pages/survey/`<br>`SurveyPage.vue`     | `/survey`                 | `SurveyController.java`    |
| 커뮤니티 <br> 공지 사항       | `pages/notice/`<br>`NoticePage.vue`     | `/stu/notices`            | `NoticeNewController.java` |
| 마이페이지                    | `pages/mypage/`<br>`MyPage.vue`         | `/stu/my-page`            | `SMypageController.java`   |

---

### 1.4 강사 (I 계정)

| 메뉴                                 | Vue 파일                                    | URL 경로              | 백엔드 Controller            |
| ------------------------------------ | ------------------------------------------- | --------------------- | ---------------------------- |
| 나의 강의 관리 <br> 강의 계획서      | `pages/attendance/`<br>`AttendancePage.vue` | `/inst/course-plan`   | `ICoursePlanController.java` |
| 나의 강의 관리 <br> 강의 목록        | `pages/lecture/`<br>`LecturePage.vue`       | `/inst/course-list`   | `ICourseController.java`     |
| 나의 강의 관리 <br> 출석 관리        | `pages/attendance/`<br>`AttendancePage.vue` | `/inst/attendance`    | `IAttendanceController.java` |
| 나의 강의 관리 <br> 학습 자료        | `pages/material/`<br>`MaterialPage.vue`     | `/inst/materials`     | `IMaterialController.java`   |
| 나의 강의 관리 <br> 시험 목록        | `pages/exam/`<br>`ExamPage.vue`             | `/inst/exams`         | `ITestController.java`       |
| 나의 강의 관리 <br> 시험 등록        | `pages/exam/`<br>`ExamPage.vue`             | `/inst/exam-register` | `ITestController.java`       |
| 나의 강의 관리 <br> 과제 목록        | `pages/homework/`<br>`HomeworkPage.vue`     | `/inst/assignments`   | `IHomeworkController.java`   |
| 나의 강의 관리 <br> 제출된 과제 목록 | `pages/homework/`<br>`HomeworkPage.vue`     | `/inst/submissions`   | `IHomeworkController.java`   |
| 커뮤니티 <br> Q&A                    | `pages/qna/`<br>`QnaPage.vue`               | `/inst/qna`           | `IQnaController.java`        |
| 커뮤니티 <br> 설문 조사              | `pages/survey/`<br>`SurveyPage.vue`         | `/survey`             | `SurveyController.java`      |
| 커뮤니티 <br> 공지 사항              | `pages/notice/`<br>`NoticePage.vue`         | `/inst/notices`       | `NoticeNewController.java`   |
| 마이페이지                           | `pages/mypage/`<br>`MyPage.vue`             | `/inst/my-page`       | `IMypageController.java`     |

---

## 2. 프로젝트 구조

```text
frontend/src/
├── api/
│   └── axios.js                  # API 요청 공통 설정
├── composables/
│   └── useAuth.js                # 로그인 상태 관리
├── components/
│   ├── layout/
│   │   ├── Layout.vue            # 전체 레이아웃 틀
│   │   ├── Sidebar.vue           # 왼쪽 메뉴
│   │   └── Header.vue            # 상단 헤더
│   └── common/
│       ├── ProtectedRoute.vue    # 로그인 보호 라우트
│       └── PlaceholderPage.vue   # 임시 페이지 컴포넌트
└── pages/                        # 팀원이 작업하는 영역
    ├── login/LoginPage.vue
    ├── register/RegisterPage.vue
    ├── find/FindPage.vue
    ├── dashboard/DashboardPage.vue
    ├── mypage/MyPage.vue
    ├── lecture/LecturePage.vue
    ├── attendance/AttendancePage.vue
    ├── homework/HomeworkPage.vue
    ├── exam/ExamPage.vue
    ├── material/MaterialPage.vue
    ├── qna/QnaPage.vue
    ├── survey/SurveyPage.vue
    ├── notice/NoticePage.vue
    └── admin/
        ├── users/AdminUsersPage.vue
        ├── lecture/AdminLecturePage.vue
        ├── classroom/AdminClassroomPage.vue
        └── exam/AdminExamPage.vue
```

---

## 3. 어디를 수정하나요?

매핑표에서 담당 메뉴의 **Vue 파일**을 열면 아래와 같은 구조입니다.

```vue
<script setup>
import PlaceholderPage from "../../components/common/PlaceholderPage.vue";
</script>

<template>
  <PlaceholderPage
    title="마이페이지"
    description="사용자 정보 조회/수정, 비밀번호 변경 등을 제공하는 페이지입니다."
  />
</template>
```

**`PlaceholderPage`를 지우고 실제 내용을 작성하면 됩니다.**

```vue
<script setup>
import { ref, onMounted } from "vue";

// 여기에 상태(ref), API 호출(onMounted) 등 작성
</script>

<template>
  <div>
    <!-- 여기에 화면 HTML 작성 -->
  </div>
</template>
```

---

## 4. 로그인 사용자 정보 가져오기

로그인한 사용자 정보가 필요하면 `useAuth` composable을 사용합니다.

```vue
<script setup>
import { useAuth } from "../../composables/useAuth";

const { user } = useAuth();

// user 객체 구조:
// user.value.loginId   → 로그인 아이디
// user.value.userNm    → 이름
// user.value.userType  → 'S'(학생) / 'I'(강사) / 'A'(관리자)
</script>

<template>
  <div>안녕하세요, {{ user.userNm }}님</div>
</template>
```

---

## 5. 백엔드 API 호출 방법

`api` 객체를 import해서 사용합니다. (세션 쿠키 자동 포함)

주의사항 (경로 규칙)<br>
프록시 설정 정책상, API 요청 경로는 /api로 시작하거나 .do로 끝나야만 백엔드로 올바르게 전달됩니다.

### GET 요청

```vue
<script setup>
import { ref, onMounted } from "vue";
import api from "../../api/axios";

const data = ref([]);

onMounted(() => {
  api
    .get("/stu/courses")
    .then((res) => {
      data.value = res.data;
    })
    .catch((err) => console.error(err));
});
</script>

<template>
  <div>
    <!-- data를 화면에 렌더링 -->
  </div>
</template>
```

### POST 요청

```vue
<script setup>
import api from "../../api/axios";

// 파라미터 전송 (Spring MVC 기본 형식)
const params = new URLSearchParams();
params.append("키", "값");

api.post("/register.do", params).then((res) => {
  if (res.data.result === "SUCCESS") {
    alert("성공!");
  }
});
</script>
```

---

## 6. CSS 작성 방법

각 페이지 폴더에 `파일명.module.css`를 만들어서 사용합니다.  
다른 컴포넌트와 클래스명이 겹쳐도 자동으로 분리됩니다.

```text
pages/mypage/
├── MyPage.vue
└── MyPage.module.css   ← 새로 만들기
```

```css
/* MyPage.module.css */
.container {
  padding: 24px;
  background: #fff;
  border-radius: 12px;
}

.title {
  font-size: 20px;
  font-weight: 700;
}
```

```vue
<!-- MyPage.vue -->
<script setup>
import styles from "./MyPage.module.css";
</script>

<template>
  <div :class="styles.container">
    <h2 :class="styles.title">마이페이지</h2>
  </div>
</template>
```

---

## 7. 작성 순서 (한 페이지 기준)

1. **매핑표**에서 담당 메뉴의 **Vue 파일** 열기
2. `PlaceholderPage` 코드를 지우고 `<script setup>`과 `<template>` 작성 시작
3. `onMounted` + `api.get()`으로 데이터 불러오기
4. 불러온 데이터를 화면에 렌더링 (`<template>` 안에 HTML 작성)
5. 스타일이 필요하면 같은 폴더에 `*.module.css` 생성 후 적용

---

## 8. 주의사항

- `api/`, `composables/`, `components/layout/` 폴더는 **수정하지 않기**
- `router/index.js`의 라우트는 팀장 확인 후 수정 (라우트 충돌 방지)
- 파일명은 **PascalCase** 유지 (예: `MyPage.vue`, `AdminExamPage.vue`)
- `import` 경로는 `../../api/axios` 처럼 **상대 경로** 사용
- 페이지 스타일은 같은 폴더의 `*.module.css`에 작성

---

## 9. 실행

```bash
# frontend 폴더에서 실행
cd frontend
npm i
npm run dev
```

브라우저에서 `http://localhost:5173` 접속  
(백엔드 서버 `http://localhost:80` 가 먼저 실행 중이어야 합니다)

---

## 10. 테스트 계정

| 구분       | 아이디            | 비밀번호 |
| ---------- | ----------------- | -------- |
| 관리자 (A) | `admin`           | `admin`  |
| 학생 (S)   | `ham`             | `123`    |
| 강사 (I)   | `happyjob_165576` | `1234`   |
