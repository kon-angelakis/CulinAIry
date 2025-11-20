import { Box, MenuItem, Select, Stack, Typography } from "@mui/material";
import { useState } from "react";
import { useLocation } from "react-router";
import PlaceCardSmall from "../components/PlaceCardSmall";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";
import useCache from "../hooks/useCache";

export default function SearchResultsPage() {
  const location = useLocation();
  const { formData } = location.state || {};
  const [filter, setFilter] = useState("Distance");
  const [sorting, setSorting] = useState("Ascending");

  const { data, isLoading, isError } = useCache({
    queryKey: `SearchResults`,
    endpoint: "/search/places",
    formData: formData,
    method: "POST",
  });

  const results = data?.data ?? [];

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        Showing {results.length} results
      </Typography>
      <Select
        labelId="searchFilter"
        id="searchFilter"
        value={filter}
        label="Filter By"
        defaultValue={"Distance"}
        onChange={(event) => {
          setFilter(event.target.value);
        }}
      >
        <MenuItem value={"Distance"}>Distance</MenuItem>
        <MenuItem value={"Relevance"}>Relevance</MenuItem>
        <MenuItem value={"Rating"}>Rating</MenuItem>
      </Select>
      <Select
        labelId="defaultSort"
        id="defaultSort"
        value={sorting}
        label="Sort By"
        defaultValue={"Ascending"}
        onChange={(event) => {
          setSorting(event.target.value);
        }}
      >
        <MenuItem value={"Ascending"}>Ascending</MenuItem>
        <MenuItem value={"Descending"}>Descending</MenuItem>
      </Select>
      <Stack spacing={4}>
        {results && results.length != 0
          ? results.map((place) => (
              <PlaceCardSmall
                key={place.id}
                id={place.id}
                thumbnail={place.thumbnail}
                name={place.name}
                category={place.primaryType}
                rating={place.rating}
                reviewCount={place.totalRatings}
                distance={place.distance}
                height={350}
              />
            ))
          : Array.from({ length: 11 }).map((_, idx) => (
              <SkeletonPlaceCard key={idx} height={350} />
            ))}
      </Stack>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          width: "100%",
          mt: 3,
        }}
      >
        {/* <Pagination
          count={maxPage}
          page={paging.page + 1}
          onChange={(e, value) => changePage(e, value)}
          color="primary"
          size="large"
        /> */}
      </Box>
    </Box>
  );
}
