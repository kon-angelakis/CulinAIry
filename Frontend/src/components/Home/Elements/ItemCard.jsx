import { LuHeart, LuStar, LuArrowLeft, LuArrowRight } from "react-icons/lu";
import Swiper from "swiper/bundle";
import "swiper/css/bundle";
import { useRef, useEffect } from "react";
import "./ItemCard.css";

function ItemCard({ name, logo, type, stars, reviewCount, backgroundPics }) {
  //Swiper references for each card
  const swiperContainerRef = useRef(null);
  const prevImageRef = useRef(null);
  const nextImageRef = useRef(null);

  useEffect(() => {
    // Swiper instance
    const swiperInstance = new Swiper(swiperContainerRef.current, {
      direction: "horizontal",
      loop: true,
      navigation: {
        nextEl: nextImageRef.current,
        prevEl: prevImageRef.current,
      },
      on: {
        beforeInit: (swiper) => {
          swiper.params.navigation.nextEl = nextImageRef.current;
          swiper.params.navigation.prevEl = prevImageRef.current;
        },
      },
    });
    return () => {
      swiperInstance.destroy();
    };
  }, []);
  return (
    <div className="card-container">
      <div className="card">
        <div ref={swiperContainerRef} className="swiper">
          <div className="swiper-wrapper">
            {backgroundPics.map((pic, index) => (
              <img
                key={index}
                className="swiper-slide"
                src={pic}
                loading="lazy"
                alt={`Background ${index + 1}`}
              />
            ))}
          </div>
        </div>

        <div className="card-favourite">
          <LuHeart />
        </div>
        <div className="card-cycle-image">
          <div className="cycle-image-left" ref={prevImageRef}>
            <LuArrowLeft />
          </div>
          <div className="cycle-image-right" ref={nextImageRef}>
            <LuArrowRight />
          </div>
        </div>
        <div className="card-place-summary">
          <div className="place-logo">
            <img src={logo} alt=""></img>
          </div>
          <div className="place-details">
            <div className="place-name">{name}</div>
            <div className="place-type">{type}</div>
          </div>
          <div className="place-ratings">
            <div className="place-stars">
              {stars ? (
                <>
                  {stars}/5{" "}
                  <LuStar
                    fill="gold"
                    stroke="gold"
                    style={{ margin: "0.4em" }}
                  />
                </>
              ) : (
                ""
              )}
            </div>
            <div className="place-review-count">
              {reviewCount ? `(${reviewCount})` : ""}
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default ItemCard;
