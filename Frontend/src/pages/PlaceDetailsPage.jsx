import { Box, Button, Grid, Skeleton } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router";
import ReviewsPanel from "../components/ReviewsPanel";
import authAxios from "../config/authAxiosConfig";

// Icons
import PlaceBanner from "../components/PlaceBanner";
import PlaceContactCard from "../components/PlaceContactCard";
import ScheduleCard from "../components/ScheduleCard";

export default function PlaceDetailsPage() {
  const { id } = useParams();

  const [isFavourite, setIsFavourite] = useState(false);
  const [reviewsLoaded, setReviewsLoaded] = useState(null);
  const [myReview, setMyReview] = useState(null);

  const fetchDetails = async () => {
    const response = await authAxios.get(`/places/${id}/details`);
    return response.data.data;
  };

  const fetchReviews = async () => {
    setReviewsLoaded(false);
    const response = await authAxios.get(`/places/${id}/reviews`);
    setReviewsLoaded(true);
    results.reviews = response.data.data;
    return response.data.data;
  };

  const fetchIsFavourited = async () => {
    const response = await authAxios.get(`/user/favourites/${id}`, {
      params: {
        username: JSON.parse(localStorage.getItem("UserDetails")).username,
      },
    });
    setIsFavourite(response.data.data);
  };

  const fetchMyReview = async () => {
    const response = await authAxios.get(`/user/myreview/${id}`);
    setMyReview(response.data);
  };

  const { data: results = {}, isLoading } = useQuery({
    queryKey: ["place", id],
    queryFn: fetchDetails,
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
    cacheTime: 1000 * 60 * 10,
  });

  useEffect(() => {
    fetchIsFavourited();
    fetchMyReview();
  }, []);

  return (
    <Box>
      <Grid container spacing={4}>
        {/* Banner */}
        <Grid size={12}>
          <PlaceBanner
            name={results.name}
            photos={results.photos}
            googleRating={results.rating}
            appRating={results.inappRating}
            googleTotalRatings={results.totalRatings}
            appTotalRatings={results.inappTotalRatings}
            isLoading={isLoading}
            isFavourite={isFavourite}
            setIsFavourite={setIsFavourite}
            userReview={myReview}
          />
        </Grid>
        {/* Contact methods */}
        <Grid size={{ xs: 12, md: 8 }}>
          {results && (
            <PlaceContactCard
              type={results.primaryType}
              secondaryTypes={results.secondaryTypes}
              website={results.website}
              phone={results.phone}
              address={results.address}
              directionsUri={results.directionsUri}
              isLoading={isLoading}
            />
          )}
        </Grid>
        {/* Schedule */}
        <Grid size={{ xs: 12, md: 4 }}>
          {results && (
            <ScheduleCard schedule={results.schedule} isLoading={isLoading} />
          )}
        </Grid>
        <Grid size={12}>
          {results.reviews || reviewsLoaded ? (
            <ReviewsPanel reviews={results.reviews} isLoading={isLoading} />
          ) : (
            <Button
              variant="contained"
              size="large"
              color="secondary"
              onClick={fetchReviews}
              loadingPosition="end"
              loading={reviewsLoaded != null}
              disabled={isLoading}
            >
              {reviewsLoaded != null ? "Loading Reviews" : "Load Reviews"}
            </Button>
          )}
        </Grid>
      </Grid>
    </Box>
  );
}
