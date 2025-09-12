import { Paper, Box, Typography, Divider } from "@mui/material";
import StarRoundedIcon from "@mui/icons-material/StarRounded";

export default function ReviewCard({ review }) {
  function getStars(rating) {
    switch (rating) {
      case 1:
        return (
          <Box sx={{ width: "80%" }}>
            <Box>
              <StarRoundedIcon fontSize="large" sx={{ color: "gold" }} />
            </Box>
            <Divider sx={{ mt: 0.5, mb: 3 }}>
              <strong>Terrible</strong>
            </Divider>
          </Box>
        );
        break;
      case 2:
        return (
          <Box sx={{ width: "80%" }}>
            <Box>
              <StarRoundedIcon
                fontSize="large"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="large"
                sx={{ color: "gold", mr: 0.5 }}
              />
            </Box>
            <Divider sx={{ mt: 0.5, mb: 3 }}>
              <strong>Bad</strong>
            </Divider>
          </Box>
        );
        break;
      case 3:
        return (
          <Box sx={{ width: "80%" }}>
            <Box>
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="large"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
            </Box>
            <Divider sx={{ mt: 0.5, mb: 3 }}>
              <strong>Fair</strong>
            </Divider>
          </Box>
        );
        break;
      case 4:
        return (
          <Box sx={{ width: "80%" }}>
            <Box>
              <StarRoundedIcon
                fontSize="small"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="small"
                sx={{ color: "gold", mr: 0.5 }}
              />
            </Box>
            <Divider sx={{ mt: 0.5, mb: 3 }}>
              <strong>Great</strong>
            </Divider>
          </Box>
        );
        break;
      case 5:
        return (
          <Box sx={{ width: "80%" }}>
            <Box>
              <StarRoundedIcon
                fontSize="small"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="large"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="medium"
                sx={{ color: "gold", mr: 0.5 }}
              />
              <StarRoundedIcon
                fontSize="small"
                sx={{ color: "gold", mr: 0.5 }}
              />
            </Box>
            <Divider sx={{ mt: 0.5, mb: 3 }}>
              <strong>Excellent</strong>
            </Divider>
          </Box>
        );
        break;
      default:
        break;
    }
  }

  return (
    <Paper
      elevation={4}
      sx={{
        mb: 2,
        px: 2,
        py: 3,
        minWidth: 50,
        maxWidth: 500,
        bgcolor: "primary.200",
        borderTopLeftRadius: " 25px",
        borderBottomRightRadius: " 25px",
        overflowX: "clip",
        textOverflow: "ellipsis",
      }}
    >
      <Box
        sx={{
          display: "flex",
          flexDirection: "column",
        }}
      >
        <Box
          sx={{
            display: "flex",
            alignItems: "center",
            justifyContent: "center",
          }}
        >
          {getStars(review.rating)}
        </Box>

        <Typography textAlign={"center"} variant={"body2"}>
          {review.text}
        </Typography>
      </Box>
    </Paper>
  );
}
