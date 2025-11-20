import { Box, Pagination, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import PlaceCardSmall from "../components/PlaceCardSmall";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";
import useCache from "../hooks/useCache";
import usePreciseLocation from "../hooks/usePreciseLocation";

export default function UserFavouritesPage() {
  const [maxPages, setMaxPages] = useState(1);
  const [paging, setPaging] = useState({
    page: 0,
    size: 10,
  });

  const { location } = usePreciseLocation();

  const { data, isLoading } = useCache({
    queryKey: "Favourites",
    endpoint: "/user/places",
    formData: { type: "FAVOURITES", location: location, pagingRequest: paging },
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

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 1 }}>
        Your favourites
      </Typography>

      {/* NEW: result count indicator */}
      <Typography variant="body1" sx={{ mb: 6, color: "text.secondary" }}>
        Showing {currentCount + paging.page * paging.size}/{totalCount} results
      </Typography>

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
