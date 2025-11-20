import { createTheme, responsiveFontSizes } from "@mui/material";
import { alpha } from "@mui/material/styles";

// Base typography
const typography = {
  fontFamily: "'Outfit Variable', sans-serif",
  h1: { fontFamily: "'Paytone One', sans-serif" },
  h2: { fontFamily: "'Paytone One', sans-serif" },
  h3: { fontFamily: "'Paytone One', sans-serif" },
  h4: { fontFamily: "'Paytone One', sans-serif" },
  button: { textTransform: "none", fontWeight: 700 },
  caption: { color: "text.secondary" },
};

// Shared shape & spacing
const componentsBase = (palette) => ({
  MuiCssBaseline: {
    styleOverrides: {
      body: {
        WebkitFontSmoothing: "antialiased",
        MozOsxFontSmoothing: "grayscale",
        backgroundColor: palette.background.default,
        color: palette.text.primary,
      },
    },
  },

  MuiAppBar: {
    defaultProps: { elevation: 1 },
    styleOverrides: {
      root: {
        backdropFilter: "saturate(120%) blur(6px)",
      },
    },
  },

  MuiButton: {
    defaultProps: { disableElevation: true },
    styleOverrides: {
      root: {
        borderRadius: 10,
        padding: "10px 16px",
      },
      containedPrimary: {
        // subtle gradient & lift
        backgroundImage: `linear-gradient(180deg, ${palette.primary.main}22, transparent)`,
      },
      outlined: {
        borderWidth: 1,
      },
    },
  },

  MuiIconButton: {
    styleOverrides: {
      root: {
        borderRadius: 10,
        "&:hover": {
          backgroundColor: alpha(palette.primary.main, 0.08),
        },
      },
    },
  },

  MuiOutlinedInput: {
    styleOverrides: {
      root: {
        borderRadius: 10,
        backgroundColor:
          palette.mode === "light"
            ? palette.background.paper
            : alpha(palette.background.paper, 0.9),
      },
      notchedOutline: {
        borderColor: alpha(palette.text.primary, 0.12),
      },
    },
  },

  MuiInputLabel: {
    styleOverrides: {
      root: {
        fontWeight: 600,
      },
    },
  },

  MuiTooltip: {
    styleOverrides: {
      tooltip: {
        borderRadius: 8,
        fontSize: "0.85rem",
      },
      arrow: {
        color: palette.grey?.[700] || palette.background.default,
      },
    },
  },

  MuiSnackbar: {
    defaultProps: {
      anchorOrigin: { vertical: "bottom", horizontal: "center" },
    },
  },

  MuiDrawer: {
    styleOverrides: {
      paper: {
        background: palette.background.paper,
      },
    },
  },

  MuiListItemButton: {
    styleOverrides: {
      root: {
        borderRadius: 8,
        "&.Mui-selected": {
          backgroundColor: alpha(palette.primary.main, 0.12),
          "&:hover": {
            backgroundColor: alpha(palette.primary.main, 0.14),
          },
        },
      },
    },
  },

  // small utility: chips and avatars
  MuiChip: {
    styleOverrides: {
      root: {
        borderRadius: 10,
      },
    },
  },
});

// ---- Light theme tokens ----
let lightTheme = createTheme({
  palette: {
    mode: "light",
    primary: { main: "#D62929", contrastText: "#fff" },
    secondary: { main: "#FFAB03", contrastText: "#000" },
    background: { default: "#f5f5f5ff", paper: "#fafafaff" },
    text: { primary: "#2C2C2C", secondary: "#6B6B6B" },
    success: { main: "#00A86B" },
    error: { main: "#D32F2F" },
    warning: { main: "#FF9800" },
    info: { main: "#1976d2" },
    grey: {
      50: "#fafafa",
      100: "#f5f5f5",
      200: "#eeeeee",
      300: "#e0e0e0",
      400: "#bdbdbd",
      600: "#757575",
      700: "#616161",
      800: "#424242",
    },
  },
  typography,
  spacing: 8,
  components: componentsBase({
    mode: "light",
    primary: { main: "#D62929" },
    background: { default: "#FAFAFA", paper: "#FFFFFF" },
    text: { primary: "#2C2C2C", secondary: "#6B6B6B" },
    grey: {
      700: "#616161",
    },
  }),
});

lightTheme = responsiveFontSizes(lightTheme);

// ---- Dark theme tokens ----
let darkTheme = createTheme({
  palette: {
    mode: "dark",
    primary: { main: "#F16565", contrastText: "#0F0F0F" }, // slightly lighter red for contrast
    secondary: { main: "#FFCA66", contrastText: "#0F0F0F" },
    background: { default: "#0C0C0E", paper: "#121214" },
    text: { primary: "#F5F5F5", secondary: "#CFCFCF" },
    success: { main: "#57D396" },
    error: { main: "#FF6B6B" },
    warning: { main: "#FFB86B" },
    info: { main: "#90CAF9" },
    grey: {
      50: "#f9f9f9",
      100: "#f1f1f1",
      200: "#e0e0e0",
      300: "#bdbdbd",
      400: "#9e9e9e",
      600: "#6b6b6b",
      700: "#575757",
      800: "#3d3d3d",
    },
  },
  typography,
  spacing: 8,
  components: componentsBase({
    mode: "dark",
    primary: { main: "#F16565" },
    background: { default: "#0C0C0E", paper: "#121214" },
    text: { primary: "#F5F5F5", secondary: "#CFCFCF" },
    grey: {
      700: "#575757",
    },
  }),
});

darkTheme = responsiveFontSizes(darkTheme);

// convenience helper
const getTheme = (mode = "light") => (mode === "dark" ? darkTheme : lightTheme);

export { lightTheme, darkTheme, getTheme };
