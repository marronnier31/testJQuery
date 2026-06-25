const menuPathMap = {
  "/admin/test-schedule": "/admin/exam/schedule",
  "/admin/courses": "/admin/courseManagement",
  "/admin/surveys": "/survey",
  "/inst/surveys": "/survey",
  "/stu/surveys": "/survey",
  "/survey/survey.do": "/survey",
};

export function normalizeMenuUrl(url) {
  if (!url) return url;
  return menuPathMap[url] ?? url;
}
