import { useState } from "react";
import Slider from "react-slick";
import "slick-carousel/slick/slick-theme.css";
import "slick-carousel/slick/slick.css";
import { Modal, Box } from "@mui/material";

export default function ImageCarousel({ images }) {
  const sliderSettings = {
    arrows: false,
    dots: true,
    infinite: true,
    draggable: true,
    slidesToShow: 1,
    slidesToScroll: 1,
    autoplay: true,
    speed: 500,
    autoplaySpeed: 5000,
    easing: "ease-in-out",
    lazyLoad: true,
  };

  const [open, setOpen] = useState(false);
  const [selectedModalImage, setselectedModalImage] = useState(null);

  return (
    <Box>
      <div className="slider-container">
        <Slider {...sliderSettings}>
          {images.map((image, idx) => (
            <Box key={idx} sx={{ "&:hover": { cursor: "pointer" } }}>
              <img
                src={image}
                style={{
                  width: "100%",
                  aspectRatio: "16/9",
                  objectFit: "cover",
                  borderRadius: "4px",
                }}
                onClick={() => {
                  setselectedModalImage(image);
                  setOpen(true);
                }}
              />
            </Box>
          ))}
        </Slider>

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
      </div>
    </Box>
  );
}
