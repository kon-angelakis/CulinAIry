import { Box, Divider, Typography } from "@mui/material";
import PlaceCardSmall from "./PlaceCardSmall.jsx";
import SkeletonPlaceCard from "./SkeletonPlaceCard.jsx";

import { useInView } from "react-intersection-observer";
import { Autoplay, EffectCoverflow, Pagination } from "swiper/modules";
import { Swiper, SwiperSlide } from "swiper/react";

// Import Swiper styles
import React from "react";
import "swiper/css";
import "swiper/css/effect-coverflow";
import "swiper/css/pagination";
import "../css/swiper.css";
import useCache from "../hooks/useCache.js";

export default React.memo(function RecommendationBox({
  title,
  endpoint,
  method,
  formData,
  height = 400,
}) {
  const { ref, inView } = useInView({ triggerOnce: true });
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
      }}
    >
      {results.length > 0 && (
        <div ref={ref}>
          {inView && (
            <>
              <Divider sx={{ my: { xs: 8, lg: 12 } }} />

              <Typography variant="h3" sx={{ mb: { xs: 6, md: 8 } }}>
                {isLoading
                  ? "Loading results"
                  : isError
                  ? "Something went wrong"
                  : title}
              </Typography>
              <Box
                sx={{
                  width: { xs: "100%", sm: "80%", md: "70%", lg: "60%" },
                  mx: "auto",
                }}
              >
                <Swiper
                  height={height}
                  autoplay={{ delay: Math.random() * 3000 + 5000 }}
                  initialSlide={1}
                  effect={"coverflow"}
                  coverflowEffect={{
                    rotate: 50,
                    stretch: 0,
                    depth: 100,
                    modifier: 0.9,
                    slideShadows: true,
                  }}
                  spaceBetween={50}
                  grabCursor={true}
                  // pagination={{
                  //   dynamicBullets: true,
                  //   type: "progressbar",
                  // }}
                  centeredSlides={true}
                  rewind={true}
                  modules={[Pagination, Autoplay, EffectCoverflow]}
                  className="mySwiper"
                >
                  {isLoading
                    ? Array.from({ length: 10 }).map((_, idx) => (
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
              </Box>
            </>
          )}
        </div>
      )}
    </Box>
  );
});
