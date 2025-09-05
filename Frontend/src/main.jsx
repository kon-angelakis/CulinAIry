import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import "./index.css";
import { createTheme, ThemeProvider } from "@mui/material";
import { BrowserRouter as Router } from "react-router";
import App from "./App.jsx";

const customTheme = createTheme({
  palette: {
    primary: {
      main: "#D62929",
    },
    secondary: {
      main: "#FFAB03",
    },
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
