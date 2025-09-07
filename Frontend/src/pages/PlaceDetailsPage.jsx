import ImageCarousel from "../components/ImageCarousel";
import { Box } from "@mui/material";
export default function PlaceDetailsPage({ placeId }) {
  return (
    <Box
      sx={{
        width: "100%",
      }}
    >
      <ImageCarousel
        images={[
          "https://ik.imagekit.io/culinairy/places/ChIJpZzSWgK9oRQRbNGvR8CXTd0/0.jpg",
          "https://ik.imagekit.io/culinairy/places/ChIJpZzSWgK9oRQRbNGvR8CXTd0/1.jpg",
          "https://ik.imagekit.io/culinairy/places/ChIJpZzSWgK9oRQRbNGvR8CXTd0/2.jpg",
        ]}
      />
    </Box>
  );
}
