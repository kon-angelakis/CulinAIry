import { defineConfig } from "vite";
import react from "@vitejs/plugin-react-swc";
import svgr from "@svgr/rollup";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), svgr()],
  server: {
    host: true, // listen on all interfaces
    port: 5173, // your dev port
    strictPort: true,
    allowedHosts: [
      "kizzie-unpneumatic-aridly.ngrok-free.app", //tunnels
      "qyvmzivriyxf.share.zrok.io",
    ],
  },
});
