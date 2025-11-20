import { Box, Paper } from "@mui/material";
import { Outlet } from "react-router";
import CulinairyAppbar from "../components/CulinairyAppbar";
import CulinairyFooter from "../components/CulinairyFooter";

export default function PageLayout() {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
        position: "relative",
        px: { xs: 1, sm: 2, md: 4, lg: 8 },
      }}
    >
      <CulinairyAppbar />
      <Paper
        elevation={2}
        sx={{
          minHeight: "100vh",
          width: "100%",
          maxWidth: "clamp(300px, 100vw, 1920px)",
          display: "flex",
          flexDirection: "column",
          justifyContent: "start",
          alignItems: "center",
          mt: 0,
          py: 12,
        }}
      >
        <Box
          sx={{
            px: { xs: 3, sm: 6, md: 9, lg: 12 },
            width: "100%",
            overflow: "hidden",
          }}
        >
          <Outlet />
        </Box>
      </Paper>
      <CulinairyFooter />
    </Box>
  );
}
