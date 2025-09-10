import {
  Box,
  Button,
  Paper,
  Rating,
  Skeleton,
  Stack,
  TextField,
  Tooltip,
  Typography,
} from "@mui/material";
import ImageCarousel from "../components/ImageCarousel";

import { useState } from "react";

// Icons
import FavoriteBorderRoundedIcon from "@mui/icons-material/FavoriteBorderRounded";
import RateReviewRoundedIcon from "@mui/icons-material/RateReviewRounded";

export default function PlaceBanner({
  name,
  photos,
  rating,
  totalRatings,
  isLoading,
}) {
  const [showReviewForm, setShowReviewForm] = useState(false);
  const [reviewText, setReviewText] = useState("");
  const [reviewRating, setReviewRating] = useState(0);

  return (
    <Paper
      sx={{
        p: 2,
      }}
    >
      {isLoading ? (
        <Skeleton variant="rectangular" height={600} />
      ) : (
        <ImageCarousel images={photos || []} />
      )}

      {/* General Info */}
      <Box>
        <Stack
          spacing={2}
          direction="column"
          sx={{ p: 2, justifyContent: "space-evenly", alignItems: "center" }}
        >
          {isLoading ? (
            <Skeleton width={150} height={80} />
          ) : name ? (
            <Typography variant="h3">{name}</Typography>
          ) : (
            "-"
          )}

          <Rating size="large" value={rating || 0} precision={0.5} readOnly />
          {isLoading ? (
            <Skeleton width={50} height={60} />
          ) : rating ? (
            <Tooltip title={`${totalRatings} reviews`} placement="top">
              <Typography variant="h5" color="text.secondary">
                {rating} / 5
              </Typography>
            </Tooltip>
          ) : (
            "-"
          )}

          <Stack direction="column" spacing={2}>
            <Button
              variant="outlined"
              startIcon={<FavoriteBorderRoundedIcon />}
            >
              Add to favourites
            </Button>
            <Button
              variant="contained"
              startIcon={<RateReviewRoundedIcon />}
              onClick={() => setShowReviewForm((prev) => !prev)}
            >
              Leave a review
            </Button>
          </Stack>
        </Stack>
      </Box>

      {showReviewForm && (
        <Paper variant="outlined" sx={{ p: 2 }}>
          <Typography variant="h6" gutterBottom>
            Share your thoughts
          </Typography>
          <Rating
            name="new-review-rating"
            value={reviewRating}
            onChange={(_, newValue) => setReviewRating(newValue)}
          />
          <TextField
            multiline
            rows={4}
            placeholder="Write your review..."
            value={reviewText}
            onChange={(e) => setReviewText(e.target.value)}
            fullWidth
            sx={{ mt: 2 }}
          />
          <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
            <Button
              variant="contained"
              onClick={() => {
                console.log("Submit review", { reviewRating, reviewText });
              }}
            >
              Submit
            </Button>
            <Button variant="text" onClick={() => setShowReviewForm(false)}>
              Cancel
            </Button>
          </Stack>
        </Paper>
      )}
    </Paper>
  );
}
