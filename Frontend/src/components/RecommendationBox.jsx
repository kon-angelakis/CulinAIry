import { Box, Divider, Typography, useTheme } from "@mui/material";
import { useLocation } from "react-router";
import PlaceCardSmall from "./PlaceCardSmall.jsx";
import SkeletonPlaceCard from "./SkeletonPlaceCard.jsx";

import { Autoplay, Pagination } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";

// Import Swiper styles
import "swiper/css";
import "swiper/css/pagination";
import "../css/swiper.css";
import useCache from "../hooks/useCache.js";

export default function RecommendationBox({
  title,
  endpoint,
  method,
  formData,
  height = 400,
}) {
  const location = useLocation();
  const theme = useTheme();

  const { data, isLoading, isError } = useCache({
    queryKey: `RecommendationBox${endpoint}`,
    endpoint: endpoint,
    formData: formData,
    method: method,
  });

  const results = data?.data ?? [];

  return (
    <Box
      elevation={3}
      sx={{
        scrollbarWidth: "none",
        width: "90%",
        mx: "auto",
        overflow: "visible",
        py: 6,
      }}
    >
      {results.length > 0 && (
        <>
          <Divider sx={{ my: { xs: 4, lg: 8 } }} />

          <Typography variant="h3" sx={{ mb: { xs: 6, md: 8 } }}>
            {isLoading
              ? "Loading results"
              : isError
              ? "Something went wrong"
              : title}
          </Typography>
          <Swiper
            height={height}
            autoplay={{ delay: Math.random() * 3000 + 5000 }}
            initialSlide={1}
            breakpoints={{
              0: { slidesPerView: 1 },
              600: { slidesPerView: 1.3 },
              900: { slidesPerView: 1.5 },
            }}
            slidesPerGroup={1}
            spaceBetween={50}
            grabCursor={true}
            // pagination={{
            //   dynamicBullets: true,
            //   type: "progressbar",
            // }}
            navigation={true}
            centeredSlides={true}
            freeMode={true}
            rewind={true}
            modules={[Pagination, Autoplay]}
            className="mySwiper"
          >
            {isLoading
              ? Array.from({ length: 11 }).map((_, idx) => (
                  <SwiperSlide
                    key={idx}
                    sx={{
                      flex: "1 1 250px",
                      maxWidth: 400,
                      width: { xs: "250px" },
                    }}
                  >
                    <SkeletonPlaceCard />
                  </SwiperSlide>
                ))
              : !isError &&
                results.map((place) => (
                  <SwiperSlide key={place.id}>
                    <PlaceCardSmall
                      id={place.id}
                      thumbnail={place.thumbnail}
                      name={place.name}
                      category={place.primaryType}
                      rating={place.rating}
                      reviewCount={place.totalRatings}
                      distance={place.distance}
                      height={height}
                    />
                  </SwiperSlide>
                ))}
          </Swiper>
        </>
      )}
    </Box>
  );
}
