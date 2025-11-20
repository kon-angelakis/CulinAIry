import {
  Box,
  Button,
  Divider,
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
import FavoriteRoundedIcon from "@mui/icons-material/FavoriteRounded";
import RateReviewRoundedIcon from "@mui/icons-material/RateReviewRounded";
import { AdvancedMarker, Map, Pin } from "@vis.gl/react-google-maps";
import { useParams } from "react-router";
import authAxios from "../config/authAxiosConfig";
import ReviewCard from "./ReviewCard";

export default function PlaceBanner({
  name,
  photos,
  googleRating,
  appRating,
  googleTotalRatings,
  appTotalRatings,
  isLoading,
  isFavourite,
  setIsFavourite,
  userReview,
  location,
}) {
  const { id } = useParams();

  const [showReviewForm, setShowReviewForm] = useState(false);
  const [reviewSubmitted, setReviewSubmitted] = useState(false);

  const [submitReviewForm, setSubmitReviewForm] = useState({
    placeId: id,
    placeName: "",
    text: "",
    rating: 1,
  });

  const addToFavs = async () => {
    const response = await authAxios.post(`/user/favourites/${id}`).then(() => {
      setIsFavourite(true);
    });
  };

  const delFromFavs = async () => {
    const response = await authAxios
      .delete(`/user/favourites/${id}`)
      .then(() => {
        setIsFavourite(false);
      });
  };

  const submitReview = async () => {
    const response = await authAxios.post("/user/submitreview", {
      ...submitReviewForm,
      placeName: name,
    });
    setReviewSubmitted(true);
    setTimeout(() => {
      window.location.reload();
    }, 2000);
  };

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
      <Divider sx={{ my: 6 }} />
      <Stack
        spacing={2}
        direction={{ xs: "column", lg: "row" }}
        sx={{
          p: 2,
          justifyContent: "space-evenly",
          alignItems: "stretch",
          display: "flex",
        }}
      >
        {/* General Info */}
        <Box sx={{ flex: 1 }}>
          {isLoading ? (
            <Skeleton width={150} height={80} />
          ) : name ? (
            <Typography variant="h3">{name}</Typography>
          ) : (
            "-"
          )}
          {/* google and app ratings */}
          <Stack
            spacing={{ xs: 6, md: 12 }}
            direction={{ xs: "column", md: "row" }}
            sx={{
              p: 2,
              alignItems: "center",
              justifyContent: "space-evenly",
            }}
          >
            <Stack
              spacing={1}
              direction="column"
              sx={{ p: 1, alignItems: "center" }}
            >
              <Stack
                spacing={1}
                direction="row"
                sx={{
                  justifyContent: "space-between",
                  alignItems: "center",
                  width: "100%",
                }}
              >
                <Typography variant="h6">Google</Typography>
                {isLoading ? (
                  <Skeleton width={50} height={20} />
                ) : googleRating ? (
                  <Tooltip
                    title={`${googleTotalRatings} reviews`}
                    placement="top"
                  >
                    <Typography variant="subtitle1" color="text.secondary">
                      {googleRating} / 5
                    </Typography>
                  </Tooltip>
                ) : (
                  "-"
                )}
              </Stack>

              <Rating
                size="large"
                value={googleRating || 0}
                precision={0.5}
                readOnly
                sx={{ color: "primary.main" }}
              />
            </Stack>

            <Stack
              spacing={1}
              direction="column"
              sx={{ p: 1, alignItems: "center" }}
            >
              <Stack
                spacing={1}
                direction="row"
                sx={{
                  justifyContent: "space-between",
                  alignItems: "center",
                  width: "100%",
                }}
              >
                <Typography variant="h6">Culinairy</Typography>
                {isLoading ? (
                  <Skeleton width={50} height={20} />
                ) : appRating ? (
                  <Tooltip title={`${appTotalRatings} reviews`} placement="top">
                    <Typography variant="subtitle1" color="text.secondary">
                      {appRating} / 5
                    </Typography>
                  </Tooltip>
                ) : (
                  "-"
                )}
              </Stack>

              <Rating
                size="large"
                value={appRating || 0}
                precision={0.5}
                readOnly
                sx={{ color: "primary.main" }}
              />
            </Stack>
          </Stack>
          <Stack direction="column" spacing={4} width={"100%"}>
            {!isFavourite ? (
              <Button
                sx={{ alignSelf: "center" }}
                onClick={addToFavs}
                variant="outlined"
                startIcon={<FavoriteBorderRoundedIcon />}
              >
                Add to favourites
              </Button>
            ) : (
              <Button
                sx={{ alignSelf: "center" }}
                onClick={delFromFavs}
                variant="contained"
                startIcon={
                  <FavoriteRoundedIcon sx={{ color: "primary.light" }} />
                }
                color="secondary"
              >
                Remove from favourites
              </Button>
            )}
            {userReview != null && userReview.data != null ? (
              <Box sx={{ width: "100%", alignSelf: "start" }}>
                <Typography variant="h5" sx={{ mb: 2 }}>
                  Your review
                </Typography>
                <ReviewCard review={userReview.data} />
              </Box>
            ) : (
              <Button
                sx={{ alignSelf: "center" }}
                variant="text"
                startIcon={<RateReviewRoundedIcon />}
                onClick={() => setShowReviewForm((prev) => !prev)}
              >
                Leave a review
              </Button>
            )}
          </Stack>
        </Box>
        {/* Map */}
        <Paper
          sx={{
            width: "100%",
            flex: { xs: "none", lg: 1 },
            height: { xs: 300, lg: "auto" },
            borderRadius: 4,
            overflow: "hidden",
          }}
          elevation={4}
        >
          <Map
            defaultZoom={13}
            defaultCenter={{ lat: 37.88, lng: 23.77 }}
            gestureHandling="greedy"
            disableDefaultUI
            mapId={"4507aa4305d5313cbdf88773"}
            colorScheme="FOLLOW_SYSTEM"
          >
            <AdvancedMarker position={{ lat: 37.88, lng: 23.77 }}>
              <Pin
                background="#4285F4"
                glyph="👤"
                glyphColor="#fff"
                borderColor="#000"
              />
              <Tooltip title="Your location" placement="top" />
            </AdvancedMarker>
            {location && (
              <AdvancedMarker position={{ lat: location.y, lng: location.x }}>
                <Pin
                  background="#FBBC04"
                  glyph="🍔"
                  glyphColor="#000"
                  borderColor="#000"
                />
              </AdvancedMarker>
            )}
          </Map>
        </Paper>
      </Stack>

      {showReviewForm ? (
        !reviewSubmitted ? (
          <Paper variant="outlined" sx={{ p: 2 }}>
            <Typography variant="h6" gutterBottom>
              Share your thoughts
            </Typography>
            <Rating
              name="new-review-rating"
              value={submitReviewForm.rating}
              onChange={(_, newValue) =>
                setSubmitReviewForm({ ...submitReviewForm, rating: newValue })
              }
            />
            <TextField
              multiline
              rows={4}
              placeholder="Write your review..."
              value={submitReviewForm.text}
              onChange={(e) =>
                setSubmitReviewForm({
                  ...submitReviewForm,
                  text: e.target.value,
                })
              }
              fullWidth
              sx={{ mt: 2 }}
            />
            <Stack direction="row" spacing={2} sx={{ mt: 2 }}>
              <Button variant="contained" onClick={submitReview}>
                Submit
              </Button>
              <Button variant="text" onClick={submitReview}>
                Cancel
              </Button>
            </Stack>
          </Paper>
        ) : (
          <Paper variant="outlined" sx={{ p: 2 }}>
            <Typography variant="h4" gutterBottom>
              Review Submitted
            </Typography>
          </Paper>
        )
      ) : (
        <Box></Box>
      )}
    </Paper>
  );
}
