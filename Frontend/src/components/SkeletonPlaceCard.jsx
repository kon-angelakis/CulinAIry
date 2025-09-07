import { Box, Paper, Skeleton, Typography } from "@mui/material";
export default function PlaceCard() {
  return (
    <Paper
      elevation={4}
      sx={{
        position: "relative",
        borderRadius: 2,
        flex: 1,
        width: "100%", // take full width of Grid cell
        aspectRatio: "16/10", // keeps proportions instead of hardcoding height
        maxWidth: 400,
        minWidth: 275,
      }}
    >
      <Skeleton
        variant="rounded"
        sx={{ bgcolor: "grey.400", width: "100%", height: "100%" }}
      >
        <Box
          sx={{
            position: "relative",
            width: 400,
            objectFit: "cover",
            borderRadius: "inherit",
          }}
        />
      </Skeleton>
      <Box
        sx={{
          position: "absolute",
          textAlign: "start",
          width: "100%",
          maxHeight: "40%",
          bottom: "0",
          bgcolor: "rgba(255, 255, 255, 0.5)",
          backdropFilter: "blur(10px)",
          borderBottomLeftRadius: "inherit",
          borderBottomRightRadius: "inherit",
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <Box textAlign="start" sx={{ ml: 2, maxWidth: "70%" }}>
          <Skeleton variant="text" animation="wave">
            <Typography variant="h5" noWrap>
              Place Name
            </Typography>
          </Skeleton>

          <Skeleton variant="text" animation="wave">
            <Typography variant="subtitle1" color="text.secondary" noWrap>
              Place Category
            </Typography>
          </Skeleton>
        </Box>
        <Skeleton
          variant="text"
          animation="wave"
          textAlign="end"
          sx={{ mr: 2 }}
        >
          <Typography variant="h6">"X / 5 ⭐"</Typography>
          <Typography variant="caption" color="text.secondary">
            ("reviewCount")
          </Typography>
        </Skeleton>
      </Box>
    </Paper>
  );
}
