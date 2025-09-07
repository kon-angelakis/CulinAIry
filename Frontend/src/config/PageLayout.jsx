import { Box, Paper, Typography } from "@mui/material";
import { Outlet } from "react-router";
import CulinairyAppbar from "../components/CulinairyAppbar";

export default function PageLayout() {
  return (
    <Box
      sx={{
        display: "flex",
        flexDirection: "column",
      }}
    >
      <CulinairyAppbar />
      <Paper
        elevation={2}
        component="main"
        sx={{
          pt: 5,
          pb: 10,
          px: 2,
          width: "100%",
          maxWidth: "clamp(275px, 75vw, 1200px)",
          mx: "auto",
          mt: 5,
        }}
      >
        <Outlet />
        {/* Footer */}
        <Typography
          variant="subtitle1"
          sx={{
            position: "absolute",
            mt: { xs: 30, lg: 40 },
            pb: 2,
            left: 0,
            right: 0,
            color: "black",
          }}
        >
          Konstantinos Angelakis &copy; 2024-{new Date().getFullYear()}{" "}
          <strong>Culinairy</strong>
        </Typography>
      </Paper>
    </Box>
  );
}
