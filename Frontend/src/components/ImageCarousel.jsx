import { useState } from "react";
import { Modal, Box } from "@mui/material";
import { Swiper, SwiperSlide } from "swiper/react";
import { Autoplay, Pagination } from "swiper/modules";
import "swiper/css";
import "swiper/css/pagination";

export default function ImageCarousel({ images }) {
  const [open, setOpen] = useState(false);
  const [selectedModalImage, setSelectedModalImage] = useState(null);
  const [grabbing, setGrabbing] = useState(false);

  return (
    <Box
      sx={{
        position: "relative",
        "&:hover": { cursor: !grabbing ? "grab" : "grabbing" },
        userSelect: "none",
      }}
    >
      <Swiper
        modules={[Autoplay, Pagination]}
        slidesPerView={1}
        loop
        autoplay={{ delay: 5000 }}
        pagination={{ clickable: true }}
      >
        {images.map((image, idx) => (
          <SwiperSlide key={idx}>
            <Box
              sx={{
                position: "relative",
                width: "100%",
                aspectRatio: { xs: "16/24", sm: "16/15", md: "16/9" },
                borderRadius: 2,
                overflow: "hidden",
              }}
              onPointerUp={() => setGrabbing(false)}
              onPointerDown={() => setGrabbing(true)}
            >
              {/* Background image */}
              <Box
                component="img"
                src={image}
                alt={`bg-${idx}`}
                loading="lazy"
                sx={{
                  position: "absolute",
                  top: 0,
                  left: 0,
                  width: "100%",
                  height: "100%",
                  objectFit: "cover",
                  filter: "blur(5px) brightness(0.5) grayscale(0.5) ",
                  zIndex: 0,
                }}
              />

              {/* Main image */}
              <Box
                component="img"
                src={image}
                alt={`Slide ${idx}`}
                loading="lazy"
                sx={{
                  position: "relative",
                  width: "100%",
                  height: "100%",
                  objectFit: "contain",
                  borderRadius: 2,
                  zIndex: 1,
                }}
                onClick={() => {
                  setSelectedModalImage(image);
                  setOpen(true);
                }}
              />
            </Box>
          </SwiperSlide>
        ))}
      </Swiper>

      <Modal open={open} onClick={() => setOpen(false)}>
        <Box
          sx={{
            display: "flex",
            justifyContent: "center",
            alignItems: "center",
            height: "100%",
          }}
        >
          <img
            src={selectedModalImage}
            alt="Full view"
            style={{
              maxWidth: "90%",
              maxHeight: "90%",
              borderRadius: "4px",
            }}
          />
        </Box>
      </Modal>
    </Box>
  );
}
