import { Box, Grid } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useEffect, useState } from "react";
import { useLocation, useNavigate, useParams } from "react-router";
import ReviewsPanel from "../components/ReviewsPanel";
import authAxios from "../config/authAxiosConfig";

// Icons
import PlaceBanner from "../components/PlaceBanner";
import PlaceContactCard from "../components/PlaceContactCard";
import RecommendationBox from "../components/RecommendationBox";
import ScheduleCard from "../components/ScheduleCard";

export default function PlaceDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();
  const loc = useLocation();
  const state = loc.state;

  const [isFavourite, setIsFavourite] = useState(false);
  const [reviews, setReviews] = useState(null);
  const [myReview, setMyReview] = useState(null);

  const fetchDetails = async () => {
    const response = await authAxios.get(`/places/${id}/details`);
    if (!response.data.success) navigate("/home");
    return response.data.data;
  };

  const fetchReviews = async () => {
    const response = await authAxios.get(`/places/${id}/reviews`);
    setReviews(response.data.data);
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
    fetchReviews();
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
            location={results.location}
            weightedRating={state?.weightedRating}
            distance={state?.distanceFromUser}
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
        {/*Reviews*/}
        <Grid size={12}>
          {reviews && <ReviewsPanel reviews={reviews} isLoading={isLoading} />}
        </Grid>
        <Grid size={12}>
          {results.location &&
            results.primaryType &&
            results.secondaryTypes &&
            results.id && (
              <RecommendationBox
                title={"Similar to this"}
                endpoint={"/recommendations/similar"}
                method={"POST"}
                formData={{
                  location: {
                    longitude: results.location.x,
                    latitude: results.location.y,
                  },
                  primaryType: results.primaryType,
                  secondaryTypes: results.secondaryTypes,
                  originalId: results.id,
                }}
                height={450}
              />
            )}
        </Grid>
      </Grid>
    </Box>
  );
}
