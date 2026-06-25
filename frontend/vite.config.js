import { defineConfig } from "vite";
import vue from "@vitejs/plugin-vue";

export default defineConfig({
  plugins: [vue()],
  server: {
    proxy: {
      "^/.*\\.do": {
        target: "http://localhost:80",
        changeOrigin: true,
        secure: false,
      },
      "/api": {
        target: "http://localhost:80",
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
