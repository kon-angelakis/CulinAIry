import { Box, Divider, Paper, Skeleton, Typography } from "@mui/material";
import AccessTimeRoundedIcon from "@mui/icons-material/AccessTimeRounded";

export default function ScheduleCard({ schedule, isLoading }) {
  return (
    <Paper sx={{ p: 2 }}>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <AccessTimeRoundedIcon sx={{ mr: 1 }} color="primary" />
        <Typography variant="h6">Schedule</Typography>
      </Box>
      <Divider sx={{ my: 1 }} />
      <Box sx={{ p: 1 }}>
        {isLoading
          ? Array.from({ length: 7 }).map((_, idx) => (
              <Skeleton
                key={idx}
                width="60%"
                sx={{ mx: "auto", my: 0.7 }}
                variant="text"
              />
            ))
          : schedule.map((day, idx) => (
              <Typography
                key={idx}
                variant="subtitle2"
                sx={{ textAlign: "center", my: 0.7 }}
              >
                {day}
              </Typography>
            ))}
      </Box>
    </Paper>
  );
}
