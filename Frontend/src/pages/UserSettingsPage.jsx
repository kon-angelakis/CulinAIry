import { useState, useContext } from "react";
import {
  Box,
  Typography,
  Paper,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from "@mui/material";
import { ThemeContext } from "../App.jsx";

export default function UserSettingsPage() {
  const { mode, setMode, deviceTheme } = useContext(ThemeContext);
  const [locale, setLocale] = useState("en");

  const resolvedMode = mode === "system" ? deviceTheme : mode;

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 4 }}>
        User Settings
      </Typography>

      <Paper sx={{ p: 3, mb: 3 }} elevation={3}>
        <Box sx={{ mb: 4 }}>
          <Typography variant="h6" sx={{ mb: 1, fontWeight: 600 }}>
            Color Theme
          </Typography>
          <FormControl fullWidth>
            <InputLabel>Theme</InputLabel>
            <Select
              value={mode}
              label="Theme"
              onChange={(e) => {
                setMode(e.target.value);
                localStorage.setItem("Theme", mode);
              }}
            >
              <MenuItem value="light">Light</MenuItem>
              <MenuItem value="dark">Dark</MenuItem>
              <MenuItem value="system">System</MenuItem>
            </Select>
          </FormControl>
        </Box>

        <Box>
          <Typography variant="h6" sx={{ mb: 1, fontWeight: 600 }}>
            Language
          </Typography>
          <FormControl fullWidth>
            <InputLabel>Language</InputLabel>
            <Select
              value={locale}
              label="Language"
              onChange={(e) => setLocale(e.target.value)}
            >
              <MenuItem value="en">English</MenuItem>
              <MenuItem value="gr">Greek</MenuItem>
            </Select>
          </FormControl>
        </Box>
      </Paper>
    </Box>
  );
}
