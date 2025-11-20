import { Box, Paper, Skeleton, Typography } from "@mui/material";
export default function SkeletonPlaceCard({ height }) {
  return (
    <Paper
      elevation={8}
      sx={{
        position: "relative",
        borderRadius: 2,
        aspectRatio: "16/10",
        maxHeight: height,
        minHeight: 200,
        width: "100%",
        overflow: "hidden",
      }}
    >
      <Box
        sx={{
          position: "absolute",
          textAlign: "start",
          width: "100%",
          maxHeight: "40%",
          bottom: "0",
          bgcolor: "primary",
          filter: "saturate(60%)",
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
        <Skeleton variant="text" animation="wave" sx={{ mr: 2 }}>
          <Typography variant="h6">"X / 5 ⭐"</Typography>
          <Typography variant="caption" color="text.secondary">
            ("reviewCount")
          </Typography>
        </Skeleton>
      </Box>
    </Paper>
  );
}
