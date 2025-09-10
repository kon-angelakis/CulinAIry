import { Box, Grid } from "@mui/material";
import { useQuery } from "@tanstack/react-query";
import { useState } from "react";
import { useParams } from "react-router";
import ReviewsPanel from "../components/ReviewsPanel";
import authAxios from "../config/authAxiosConfig";

// Icons
import PlaceBanner from "../components/PlaceBanner";
import PlaceContactCard from "../components/PlaceContactCard";
import ScheduleCard from "../components/ScheduleCard";

export default function PlaceDetailsPage() {
  const { id } = useParams();

  const fetchDetails = async () => {
    const response = await authAxios.get(`/places/${id}`);
    return response.data.data;
  };

  const { data: results = {}, isLoading } = useQuery({
    queryKey: ["place", id],
    queryFn: fetchDetails,
    enabled: !!id,
    staleTime: 1000 * 60 * 5,
    cacheTime: 1000 * 60 * 10,
  });

  return (
    <Box>
      <Grid container spacing={4}>
        {/* Banner */}
        <Grid>
          <PlaceBanner
            name={results.name}
            photos={results.photos}
            rating={results.rating}
            totalRatings={results.totalRatings}
            isLoading={isLoading}
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
          {!isLoading && <ReviewsPanel reviews={results.reviews} />}
        </Grid>
      </Grid>
    </Box>
  );
}
