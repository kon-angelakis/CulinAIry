import { createTheme, responsiveFontSizes, ThemeProvider } from "@mui/material";
import { createRoot } from "react-dom/client";
import { BrowserRouter as Router } from "react-router";
import App from "./App.jsx";
import "./index.css";
import { darkTheme, lightTheme } from "./Theme.js";

import "@fontsource-variable/outfit";
import "@fontsource/paytone-one";

createRoot(document.getElementById("root")).render(
  <Router>
    <App />
  </Router>
);
