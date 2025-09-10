import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { createTheme, ThemeProvider, responsiveFontSizes } from "@mui/material";
import { BrowserRouter as Router } from "react-router";
import App from "./App.jsx";

import "@fontsource-variable/outfit";
import "@fontsource/paytone-one";

let customTheme = createTheme({
  palette: {
    primary: {
      main: "#D62929",
    },
    secondary: {
      main: "#FFAB03",
    },
    background: {
      default: "#FAFAFA",
      paper: "#FFFFFF",
    },
    text: {
      primary: "#2C2C2C",
      secondary: "#6B6B6B",
    },
  },
  typography: {
    fontFamily: "'Outfit Variable', sans-serif",
    h1: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h2: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h3: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h4: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
  },
});

customTheme = responsiveFontSizes(customTheme);

createRoot(document.getElementById("root")).render(
  <ThemeProvider theme={customTheme}>
    <Router>
      <App />
    </Router>
  </ThemeProvider>
);
