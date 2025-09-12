import Masonry from "@mui/lab/Masonry";
import {
  Box,
  Button,
  Divider,
  Fab,
  Paper,
  Skeleton,
  Typography,
} from "@mui/material";
import ReviewCard from "./ReviewCard";
import AutoAwesomeRoundedIcon from "@mui/icons-material/AutoAwesomeRounded";
import { useState } from "react";
import authAxios from "../config/authAxiosConfig";

export default function ReviewsPanel({ reviews }) {
  const [summarized, setSummarized] = useState(false);
  const reviewTexts = { reviews: reviews.map((r) => r.text) };

  const [summaryResponse, setsummaryResponse] = useState(null);
  const [isLoading, setIsLoading] = useState(null);

  const handleSummary = () => {
    setSummarized(true);
    setIsLoading(true);
    authAxios
      .post("/ai/summary", reviewTexts)
      .then((response) => {
        setsummaryResponse(response.data);
        setIsLoading(false);
      })
      .catch((error) => {
        setIsLoading(false);
        throw error;
      });
  };

  return (
    <Paper
      sx={{
        p: 2,
        maxHeight: "600px",
        overflowY: "auto",
        scrollbarWidth: "thin",
      }}
    >
      <Typography variant="h5" sx={{ mt: 0, mb: 2 }} gutterBottom>
        Some users have said
      </Typography>
      <Divider sx={{ mb: 2 }} />
      {!summarized ? (
        <Button
          variant="contained"
          startIcon={<AutoAwesomeRoundedIcon sx={{ mr: 1 }} />}
          color="secondary"
          size="medium"
          onClick={handleSummary}
        >
          Summarize
        </Button>
      ) : (
        <Box sx={{ textAlign: "start", p: 2 }}>
          {isLoading ? (
            <Box>
              <Typography
                variant="subtitle1"
                sx={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "start",
                  alignItems: "center",
                }}
              >
                Summarizing
                <AutoAwesomeRoundedIcon
                  sx={{ px: 1, color: "secondary.light" }}
                />
              </Typography>
              <Skeleton animation="wave" variant="text" />
              <Skeleton animation="wave" variant="text" />
              <Skeleton animation="wave" variant="text" width="70%" />
            </Box>
          ) : summaryResponse && summaryResponse.success ? (
            <Box>
              <Typography
                variant="subtitle1"
                sx={{
                  display: "flex",
                  flexDirection: "row",
                  justifyContent: "start",
                  alignItems: "center",
                }}
              >
                Summary
                <AutoAwesomeRoundedIcon
                  sx={{ px: 1, color: "secondary.light" }}
                />
              </Typography>
              <Typography variant="body1" color="text.secondary">
                {summaryResponse.data.summary}
              </Typography>
            </Box>
          ) : (
            <Typography variant="subtitle1">
              Could not generate a summary
            </Typography>
          )}
        </Box>
      )}

      <Divider sx={{ mt: 2, mb: 5 }} />
      <Box
        sx={{
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-around",
          alignItems: "center",
          flexWrap: "wrap",
        }}
      >
        <Masonry columns={{ xs: 1, sm: 2, md: 3, lg: 3 }} spacing={4}>
          {reviews != null && reviews.length > 0 ? (
            reviews.map((review, idx) => (
              <ReviewCard key={idx} review={review} />
            ))
          ) : (
            <Typography variant="body2" color="text.secondary">
              No reviews yet.
            </Typography>
          )}
        </Masonry>
      </Box>
    </Paper>
  );
}
