import { Box, Pagination, Stack, Typography } from "@mui/material";
import { useEffect, useState } from "react";
import ReviewCardSlim from "../components/ReviewCardSlim";
import useCache from "../hooks/useCache";

export default function UserReviewsPage() {
  const [maxPages, setMaxPages] = useState(1);
  const [paging, setPaging] = useState({
    page: 0,
    size: 10,
  });

  const { data, isLoading } = useCache({
    queryKey: "Reviews",
    endpoint: "/user/reviews",
    formData: paging,
    method: "POST",
  });

  useEffect(() => {
    if (data?.data?.totalPages != null && maxPages == null) {
      setMaxPages(data.data.totalPages);
    }
  }, [data]);

  const reviews = data?.data ?? [];

  const currentCount = reviews?.content?.length ? reviews.content.length : 0;

  const totalCount = reviews?.totalElements ?? 0;

  const changePage = (e, value) => {
    setPaging({ ...paging, page: value - 1 });
  };

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        Your Reviews
      </Typography>
      <Stack spacing={4}>
        {reviews && reviews.length != 0 ? (
          reviews.content.map((review, index) => (
            <ReviewCardSlim
              key={index}
              placeId={review.placeId}
              place={review.placeName}
              rating={review.rating}
              text={review.text}
            />
          ))
        ) : (
          <Typography>No reviews yet</Typography>
        )}
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
