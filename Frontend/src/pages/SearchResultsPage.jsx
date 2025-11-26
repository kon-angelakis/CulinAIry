import {
  Box,
  MenuItem,
  Pagination,
  Select,
  Stack,
  Typography,
} from "@mui/material";
import { useState, useEffect } from "react";
import { useLocation } from "react-router";
import PlaceCardSmall from "../components/PlaceCardSmall";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";
import useCache from "../hooks/useCache";

export default function SearchResultsPage() {
  const location = useLocation();
  const [maxPages, setMaxPages] = useState(null);
  const [paging, setPaging] = useState({
    page: 0,
    size: 10,
  });
  const { formData } = location.state || {};
  const [filter, setFilter] = useState("distance");
  const [sorting, setSorting] = useState(1);

  const { data, isLoading, isError } = useCache({
    queryKey: `SearchResults_${filter}_${sorting}_${paging.page}`,
    endpoint: "/search/places",
    formData: {
      ...formData,
      pagingRequest: paging,
      sortField: filter,
      sortDirection: sorting,
    },
    method: "POST",
  });

  useEffect(() => {
    if (data?.data?.totalPages != null && maxPages == null) {
      setMaxPages(data.data.totalPages);
    }
  }, [data]);

  const results = data?.data ?? [];

  const currentCount = results?.content?.length ? results.content.length : 0;

  const totalCount = results?.totalElements ?? 0;

  const changePage = (e, value) => {
    setPaging({ ...paging, page: value - 1 });
  };

  useEffect(() => {
    window.scrollTo({
      top: 0,
      left: 0,
      behavior: "smooth",
    });
  }, [paging.page]);

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        Search results
      </Typography>

      <Typography variant="body1" sx={{ mb: 6, color: "text.secondary" }}>
        {results && results.length != 0
          ? `Showing ${
              currentCount + paging.page * paging.size
            }/${totalCount} results`
          : "Loading results..."}
      </Typography>
      <Stack
        direction={{ xs: "column", sm: "row" }}
        sx={{ alignItems: "center", mb: 4, justifyContent: "center" }}
        spacing={4}
      >
        <Box
          sx={{
            display: "flex",
            justifyContent: "end",
            alignItems: "center",
          }}
        >
          <Typography variant="h6" sx={{ mr: 2 }} textAlign={"start"}>
            Filter By
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
            <MenuItem value={"distance"}>Distance</MenuItem>
            <MenuItem value={"matchCount"}>Relevance</MenuItem>
            <MenuItem value={"weightedRank"}>Rating</MenuItem>
          </Select>
        </Box>
        <Box
          sx={{
            display: "flex",
            justifyContent: "end",
            alignItems: "center",
          }}
        >
          <Typography variant="h6" sx={{ mr: 2 }}>
            Sort By
          </Typography>
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
            <MenuItem value={1}>Ascending</MenuItem>
            <MenuItem value={-1}>Descending</MenuItem>
          </Select>
        </Box>
      </Stack>
      <Stack spacing={4}>
        {results && results.length != 0
          ? results.content.map((place) => (
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
          : Array.from({ length: 10 }).map((_, idx) => (
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
        <Pagination
          count={maxPages}
          page={paging.page + 1}
          onChange={(e, value) => changePage(e, value)}
          color="primary"
          size="large"
        />
      </Box>
    </Box>
  );
}
