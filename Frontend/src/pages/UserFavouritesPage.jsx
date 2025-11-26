import {
  Box,
  MenuItem,
  Pagination,
  Select,
  Stack,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import PlaceCardSmall from "../components/PlaceCardSmall";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";
import useCache from "../hooks/useCache";
import usePreciseLocation from "../hooks/usePreciseLocation";

export default function UserFavouritesPage() {
  const [sorting, setSorting] = useState("DESC");
  const [maxPages, setMaxPages] = useState(null);
  const [paging, setPaging] = useState({
    page: 0,
    size: 10,
  });

  const { userLocation } = usePreciseLocation();

  const { data, isLoading } = useCache({
    queryKey: `Favourites_${sorting}_${paging.page}`,
    endpoint: "/user/places",
    formData: {
      type: "FAVOURITES",
      location: userLocation,
      pagingRequest: paging,
      sortOrder: sorting,
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
    window.scrollTo({ top: 0, behavior: "smooth" });
  }, [paging.page]);

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        Your favourites
      </Typography>

      <Typography variant="body1" sx={{ mb: 6, color: "text.secondary" }}>
        {results && results.length != 0
          ? `Showing ${
              currentCount + paging.page * paging.size
            }/${totalCount} results`
          : "Loading results..."}
      </Typography>
      <Box
        sx={{
          display: "flex",
          justifyContent: "center",
          alignItems: "center",
          mb: 4,
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
          defaultValue={"DESC"}
          onChange={(event) => {
            setSorting(event.target.value);
          }}
        >
          <MenuItem value={"ASC"}>Ascending</MenuItem>
          <MenuItem value={"DESC"}>Descending</MenuItem>
        </Select>
      </Box>

      <Stack spacing={4}>
        {results && results.length !== 0
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
          : Array.from({ length: paging.size }).map((_, idx) => (
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
          onChange={changePage}
          color="primary"
          size="large"
        />
      </Box>
    </Box>
  );
}
