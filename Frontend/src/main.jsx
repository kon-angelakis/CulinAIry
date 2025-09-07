import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { createTheme, ThemeProvider } from "@mui/material";
import { BrowserRouter as Router } from "react-router";
import App from "./App.jsx";

import "@fontsource-variable/outfit";
import "@fontsource/paytone-one";

const customTheme = createTheme({
  palette: {
    primary: {
      main: "#D62929",
    },
    secondary: {
      main: "#FFAB03",
    },
  },
  typography: {
    fontFamily: "'Outfit Variable', sans-serif",
    h1: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h2: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h3: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h4: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h5: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
    h6: { fontFamily: "'Paytone One', sans-serif", letterSpacing: "-2px" },
  },
});

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <ThemeProvider theme={customTheme}>
      <Router>
        <App />
      </Router>
    </ThemeProvider>
  </StrictMode>
);
