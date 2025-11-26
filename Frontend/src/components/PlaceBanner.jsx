import {
  Box,
  Button,
  Collapse,
  Divider,
  Paper,
  Rating,
  Skeleton,
  Stack,
  TextField,
  Tooltip,
  Typography,
  useTheme,
} from "@mui/material";
import ImageCarousel from "../components/ImageCarousel";

import { useEffect, useState } from "react";

// Icons
import FavoriteBorderRoundedIcon from "@mui/icons-material/FavoriteBorderRounded";
import FavoriteRoundedIcon from "@mui/icons-material/FavoriteRounded";
import KeyboardArrowDownRoundedIcon from "@mui/icons-material/KeyboardArrowDownRounded";
import LocationOnRoundedIcon from "@mui/icons-material/LocationOnRounded";
import RateReviewRoundedIcon from "@mui/icons-material/RateReviewRounded";

import { AdvancedMarker, Map, Pin } from "@vis.gl/react-google-maps";
import { useParams } from "react-router";
import authAxios from "../config/authAxiosConfig";
import usePreciseLocation from "../hooks/usePreciseLocation";
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
  weightedRating,
  distance,
}) {
  const { id } = useParams();
  const theme = useTheme();
  const { userLocation } = usePreciseLocation();

  const [showReviewForm, setShowReviewForm] = useState(false);
  const [reviewSubmitted, setReviewSubmitted] = useState(false);
  const [favouriteCount, setFavouriteCount] = useState(null);
  const [showRatings, setShowRatings] = useState(false); // <-- missing before

  const [submitReviewForm, setSubmitReviewForm] = useState({
    placeId: id,
    placeName: "",
    text: "",
    rating: 1,
  });

  // Get fav count
  useEffect(() => {
    fetchFavouriteCount();
  }, [isFavourite, id]);

  const toggleFavourite = async () => {
    try {
      if (!isFavourite) {
        await authAxios.post(`/user/favourites/${id}`);
        setIsFavourite(true);
      } else {
        await authAxios.delete(`/user/favourites/${id}`);
        setIsFavourite(false);
      }
    } catch (err) {
      console.error("Failed to toggle favourite:", err);
    }
  };

  const fetchFavouriteCount = async () => {
    try {
      const res = await authAxios.get(`/places/${id}/favouritecount`);
      const favCount = res?.data?.data ?? 0;
      setFavouriteCount(favCount);
    } catch (err) {
      console.error("Failed to fetch favourite count:", err);
      setFavouriteCount(0);
    }
  };

  const submitReview = async () => {
    try {
      await authAxios.post("/user/submitreview", {
        ...submitReviewForm,
        placeName: name,
      });
      setReviewSubmitted(true);
      // small UX delay then reload
      setTimeout(() => {
        window.location.reload();
      }, 1200);
    } catch (err) {
      console.error("Failed to submit review:", err);
    }
  };

  const formatDistance = (d) => {
    if (d == null) return null;
    const n = Number(d);
    if (Number.isNaN(n)) return String(d);
    if (Math.abs(n) >= 1000) return `${Math.round((n / 1000) * 10) / 10} km`;
    return `${Math.round(n)} m`;
  };

  const displayWeighted =
    typeof weightedRating === "number"
      ? Math.round(weightedRating * 10) / 10
      : null;

  return (
    <Paper sx={{ p: { xs: 1.5, sm: 2 } }}>
      {isLoading ? (
        <Skeleton variant="rectangular" height={320} />
      ) : (
        <ImageCarousel images={photos || []} />
      )}

      <Divider sx={{ my: { xs: 3, md: 6 } }} />

      <Stack
        spacing={{ xs: 3, md: 4 }}
        direction={{ xs: "column", lg: "row" }}
        sx={{ alignItems: "stretch" }}
      >
        {/* General info */}
        <Box flex={1}>
          {isLoading ? (
            <Skeleton width={180} height={60} />
          ) : (
            <Typography variant="h2" sx={{ pb: 2, color: "secondary.main" }}>
              {name || "-"}
            </Typography>
          )}

          {/* Ratings */}
          <Stack
            direction="row"
            alignItems="center"
            justifyContent="center"
            spacing={1.5}
            sx={{ mt: 3, mb: 1 }}
          >
            <Typography variant="h4" sx={{ fontWeight: 700, lineHeight: 1 }}>
              {Math.round(weightedRating * 10) / 10 || "-"}
            </Typography>

            <Rating
              value={Math.round(weightedRating * 10) / 10 || 0}
              precision={0.1}
              readOnly
              size="large"
              sx={{ fontSize: { xs: 32, sm: 40 } }}
            />

            <Box
              onClick={() => setShowRatings((prev) => !prev)}
              sx={{
                cursor: "pointer",
                display: "flex",
                alignItems: "center",
                transition: "transform 0.25s ease",
                transform: showRatings ? "rotate(180deg)" : "rotate(0deg)",
                color: "text.secondary",
              }}
            >
              <KeyboardArrowDownRoundedIcon />
            </Box>
          </Stack>

          <Collapse in={showRatings} timeout="auto">
            <Stack
              direction={{ xs: "column", sm: "row" }}
              spacing={{ xs: 2, sm: 4 }}
              justifyContent="center"
              alignItems="center"
              sx={{ mt: 2 }}
            >
              <Stack alignItems="center" spacing={0.5}>
                <Typography variant="subtitle1">Google</Typography>
                <Tooltip title={`${googleTotalRatings} reviews`}>
                  <Typography variant="body2" color="text.secondary">
                    {googleRating} / 5
                  </Typography>
                </Tooltip>
                <Rating value={googleRating || 0} readOnly precision={0.1} />
              </Stack>

              <Stack alignItems="center" spacing={0.5}>
                <Typography variant="subtitle1">Culinairy</Typography>
                <Tooltip title={`${appTotalRatings} reviews`}>
                  <Typography variant="body2" color="text.secondary">
                    {appRating} / 5
                  </Typography>
                </Tooltip>
                <Rating value={appRating || 0} readOnly precision={0.1} />
              </Stack>
            </Stack>
          </Collapse>

          {/* favs part */}
          <Stack spacing={2} mt={4} alignItems="center">
            {!isFavourite ? (
              <Button
                onClick={toggleFavourite}
                variant="outlined"
                startIcon={<FavoriteBorderRoundedIcon />}
              >
                Add to favourites
              </Button>
            ) : (
              <Button
                onClick={toggleFavourite}
                variant="contained"
                color="secondary"
                startIcon={<FavoriteRoundedIcon />}
              >
                Remove from favourites
              </Button>
            )}
          </Stack>

          <Typography sx={{ mt: 1 }} variant="body2" color="text.secondary">
            <strong>{favouriteCount ?? 0}</strong> users also liked this place
          </Typography>

          {/* user review */}
          <Stack mt={4}>
            {userReview?.data ? (
              <>
                <Typography variant="h6">Your review</Typography>
                <ReviewCard review={userReview.data} />
              </>
            ) : (
              <Button
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
            flex: 1,
            height: { xs: 280, sm: 360, lg: "auto" },
            borderRadius: 3,
            overflow: "hidden",
            position: "relative",
          }}
          elevation={4}
        >
          {location && userLocation ? (
            <Box
              sx={{
                width: "100%",
                flex: { xs: "none", lg: 1 },
                height: { xs: 300, lg: "100%" },
                borderRadius: 4,
                overflow: "hidden",
              }}
            >
              <Map
                defaultZoom={13}
                defaultCenter={{ lat: location.y, lng: location.x }}
                gestureHandling="greedy"
                disableDefaultUI
                mapId={"4507aa4305d5313cbdf88773"}
                colorScheme={theme.palette.mode === "dark" ? "DARK" : "LIGHT"}
              >
                <AdvancedMarker
                  position={{
                    lat: userLocation.latitude,
                    lng: userLocation.longitude,
                  }}
                >
                  <Pin
                    background="#4285F4"
                    glyph="👤"
                    glyphColor="#fff"
                    borderColor="#000"
                  />
                </AdvancedMarker>

                <AdvancedMarker position={{ lat: location.y, lng: location.x }}>
                  <Pin glyph="🍔" />
                </AdvancedMarker>
              </Map>

              <Box
                sx={{
                  position: "absolute",
                  bottom: 12,
                  left: "50%",
                  transform: "translateX(-50%)",
                  backgroundColor: "secondary.light",
                  color: "primary.main",
                  px: 2,
                  py: 0.5,
                  borderRadius: 999,
                  boxShadow: 3,
                  display: "flex",
                  alignItems: "center",
                  gap: 1,
                }}
              >
                <LocationOnRoundedIcon fontSize="small" />
                <Typography variant="body2">
                  {distance
                    ? `${
                        distance < 1000
                          ? `${Math.round(distance)} m`
                          : `${Math.round((distance / 1000) * 10) / 10} km`
                      } away`
                    : "Distance unavailable"}
                </Typography>
              </Box>
            </Box>
          ) : (
            <Typography variant="h6" sx={{ p: 2 }}>
              Map preview is currently unavailable.
            </Typography>
          )}
        </Paper>
      </Stack>

      {/* Review form */}
      {showReviewForm && (
        <Paper variant="outlined" sx={{ p: 2, mt: 4 }}>
          {!reviewSubmitted ? (
            <>
              <Typography variant="h6" gutterBottom>
                Share your thoughts
              </Typography>
              <Rating
                value={submitReviewForm.rating}
                onChange={(_, newValue) =>
                  setSubmitReviewForm({
                    ...submitReviewForm,
                    rating: newValue,
                  })
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
                <Button variant="text" onClick={() => setShowReviewForm(false)}>
                  Cancel
                </Button>
              </Stack>
            </>
          ) : (
            <Typography variant="h5">Review Submitted</Typography>
          )}
        </Paper>
      )}
    </Paper>
  );
}
