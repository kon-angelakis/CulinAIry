import { Box, Grid, Typography } from "@mui/material";
import SkeletonPlaceCard from "./SkeletonPlaceCard.jsx";
import { useEffect, useState } from "react";
import authAxios from "../config/authAxiosConfig.js";
import PlaceCard from "./PlaceCard.jsx";
import { useLocation } from "react-router";
import { useQuery } from "@tanstack/react-query";

export default function SearchResultsBox({ endpoint, method, formData }) {
  const location = useLocation();
  const [placesResponse, setPlacesResponse] = useState(null);

  const fetchPlaces = async () => {
    try {
      const response = await authAxios({
        url: endpoint,
        method: method,
        ...(method === "GET"
          ? { params: formData.params }
          : { data: formData }),
      });
      setPlacesResponse(response.data);
      return response.data.data;
    } catch (error) {
      console.log(error);
    }
  };

  // useQuery for caching results so if a user backtracks he doesnt requery the backend
  //on page refresh the componenet is unmounted therefore cache loss
  const {
    data: results = [],
    isLoading,
    isError,
    error,
  } = useQuery({
    queryKey: [
      "searchResults",
      {
        formData,
      },
    ],
    queryFn: fetchPlaces,
    enabled: !!formData, //if somehow formData is invalid dont proceed
    staleTime: 1000 * 60 * 5, //5 mins for stale data update
    cacheTime: 1000 * 60 * 10, //10 mins for memory wipe
  });

  return (
    <Box
      sx={{
        maxHeight: "80vh",
        overflowY: "scroll",
        scrollbarWidth: "none",
      }}
    >
      <Typography variant="h4" sx={{ mb: 4 }}>
        {isLoading
          ? "Loading results"
          : isError
          ? `Error retrieving results: ${placesResponse?.message}`
          : `Found ${results.length} entries`}
      </Typography>

      <Grid container spacing={4} sx={{ p: 2 }} justifyContent="space-evenly">
        {isLoading
          ? Array.from({ length: 11 }).map((_, idx) => (
              <Grid
                key={idx}
                item
                sx={{
                  flex: "1 1 250px",
                  maxWidth: 400,
                  width: { xs: "250px" },
                }}
              >
                <SkeletonPlaceCard />
              </Grid>
            ))
          : !isError
          ? results.map((place) => (
              <Grid
                key={place.id}
                item
                sx={{
                  flex: "1 1 250px",
                  maxWidth: 400,
                  width: { xs: "250px" },
                }}
              >
                <PlaceCard
                  id={place.id}
                  thumbnail={place.thumbnail}
                  name={place.name}
                  category={place.primaryType}
                  rating={place.rating}
                  reviewCount={place.totalRatings}
                />
              </Grid>
            ))
          : "-"}
      </Grid>
    </Box>
  );
}
