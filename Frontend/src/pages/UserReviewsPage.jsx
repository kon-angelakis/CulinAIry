import { Box, Button, Pagination, Stack, Typography } from "@mui/material";
import ReviewCardSlim from "../components/ReviewCardSlim";
import { useEffect, useState } from "react";
import authAxios from "../config/authAxiosConfig";

export default function UserReviewsPage() {
  const [maxPage, setMaxPage] = useState(1);
  const [paging, setPaging] = useState({
    page: 0,
    size: 5,
  });
  const [reviews, setReviews] = useState(null);

  const fetchReviews = async () => {
    const response = await authAxios.post("/user/reviews", paging);
    setReviews(response.data.data.content);
    return response.data.data;
  };

  useEffect(() => {
    const load = async () => {
      const response = await fetchReviews();
      setMaxPage(response.totalPages);
    };
    load();
  }, [paging]);

  const changePage = (e, value) => {
    setPaging({ ...paging, page: value - 1 });
  };

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        My Reviews
      </Typography>
      <Stack spacing={4}>
        {reviews && reviews.length != 0 ? (
          reviews.map((review, index) => (
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
          count={maxPage}
          page={paging.page + 1}
          onChange={(e, value) => changePage(e, value)}
          color="primary"
          size="large"
        />
      </Box>
    </Box>
  );
}
