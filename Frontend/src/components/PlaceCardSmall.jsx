import { FavoriteRounded } from "@mui/icons-material";
import StarRoundedIcon from "@mui/icons-material/StarRounded";
import { Box, Paper, Tooltip, Typography } from "@mui/material";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router";
import { ThemeContext } from "../App.jsx";
import authAxios from "../config/authAxiosConfig.js";

export default function PlaceCardSmall({
  id,
  thumbnail,
  name,
  category,
  rating,
  distance,
  height = 200,
}) {
  const { mode } = useContext(ThemeContext);

  const navigate = useNavigate();

  const [elevation, setElevation] = useState(2);

  const [favourite, setFavourite] = useState(false);
  const [favouriteTooltip, setFavouriteTooltip] = useState("Add to favourites");

  // Initial favourite state
  useEffect(() => {
    async function fetchFavourite() {
      try {
        const res = await authAxios.get(`/user/favourites/${id}`);
        const isFav = res.data.data;
        setFavourite(isFav);
        setFavouriteTooltip(
          isFav ? "Remove from favourites" : "Add to favourites"
        );
      } catch (err) {
        console.error("Failed to fetch favourite:", err);
      }
    }
    fetchFavourite();
  }, [id]);

  const toggleFavourite = async () => {
    try {
      if (!favourite) {
        await authAxios.post(`/user/favourites/${id}`);
        setFavourite(true);
        setFavouriteTooltip("Remove from favourites");
      } else {
        await authAxios.delete(`/user/favourites/${id}`);
        setFavourite(false);
        setFavouriteTooltip("Add to favourites");
      }
    } catch (err) {
      console.error("Failed to toggle favourite:", err);
    }
  };

  //Add place to history
  const handleClick = async () => {
    const addClickedResponse = await authAxios.post(`/user/clicked/${id}`);
    const addHistoryResponse = await authAxios
      .post(`/user/history/${id}`)
      .then(() => {
        setTimeout(() => {
          navigate(`/place/${id}`);
        }, 1000);
      });
  };

  return (
    <Paper
      elevation={elevation}
      sx={{
        position: "relative",
        borderRadius: 2,
        aspectRatio: "16/10",
        maxHeight: height,
        minHeight: 200,
        width: "100%",
        overflow: "hidden",
        transition: "transform 0.3s ease, box-shadow 0.3s ease",
        "&:hover": {
          cursor: "pointer",
        },
        "&:hover .thumbnail": { filter: "grayscale(0)" },
      }}
      onMouseEnter={() => setElevation(4)}
      onMouseLeave={() => setElevation(2)}
    >
      <Box
        sx={{
          position: "absolute",
          top: 12,
          left: 0,
          right: 0,
          display: "flex",
          justifyContent: "space-between",
          px: 1.5,
          zIndex: 10,
        }}
      >
        {/* Favourite */}
        <Tooltip title={favouriteTooltip} placement="bottom">
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 0.5,
              bgcolor:
                mode == "light"
                  ? "rgba(211, 211, 211, 0.5)"
                  : " rgba(39, 39, 39, 0.5)",
              backdropFilter: "blur(10px)",
              px: 2,
              py: 1,
              borderRadius: 3,
              cursor: "pointer",
              transition: "transform 0.2s",
              ":hover": { transform: "scale(1.05)" },
            }}
            onClick={toggleFavourite}
          >
            <FavoriteRounded
              sx={{
                fontSize: { xs: "1.8rem", sm: "2rem" },
                color: favourite ? "primary.light" : "white",
              }}
            />
            <Typography
              variant="subtitle2"
              sx={{
                color: "text.primary",
              }}
            >
              {/* placeholder */}
            </Typography>
          </Box>
        </Tooltip>

        {/* Rating */}
        <Tooltip
          title={`Weighted rating: ${Math.round(rating.rating * 10) / 10}`}
          placement="bottom"
        >
          <Box
            sx={{
              display: "flex",
              flexDirection: "column",
              alignItems: "center",
              gap: 0.5,
              bgcolor:
                mode == "light"
                  ? "rgba(211, 211, 211, 0.5)"
                  : " rgba(39, 39, 39, 0.5)",
              backdropFilter: "blur(10px)",
              px: 2,
              py: 1,
              borderRadius: 3,
            }}
          >
            <StarRoundedIcon
              sx={{ fontSize: { xs: "1.8rem", sm: "2rem" }, color: "#ffc400" }}
            />
            <Typography
              variant="subtitle2"
              sx={{
                color: "text.primary",
              }}
            >
              {Math.round(rating.rating * 10) / 10}/5
            </Typography>
          </Box>
        </Tooltip>
      </Box>

      <Box
        className="thumbnail"
        component="img"
        loading="lazy"
        src={thumbnail}
        alt={name + " thumbnail"}
        sx={{
          width: "100%",
          height: "96%",
          bgcolor: "grey",
          objectFit: "cover",
          borderRadius: "inherit",
          filter: "grayscale(0.2)",
        }}
        onClick={handleClick}
      />
      <Box
        sx={{
          position: "absolute",
          textAlign: "start",
          width: "100%",
          height: { xs: "30%", lg: "22%" },
          bottom: "0",
          bgcolor:
            mode == "light"
              ? "rgba(211, 211, 211, 0.5)"
              : " rgba(39, 39, 39, 0.5)",
          backdropFilter: "blur(10px)",
          borderBottomLeftRadius: "inherit",
          borderBottomRightRadius: "inherit",
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-evenly",
        }}
      >
        <Box
          textAlign="center"
          sx={{
            display: "flex",
            flexDirection: "column",
            justifyContent: "space-evenly",
          }}
        >
          <Tooltip title={name} arrow placement="top">
            <Typography variant="h4" noWrap>
              {name}
            </Typography>
          </Tooltip>
        </Box>
        <Box
          textAlign="start"
          sx={{
            mr: 2,
            display: "flex",
            flexDirection: "row",
            justifyContent: "center",
            gap: 4,
          }}
        >
          <Tooltip title={category} arrow>
            <Typography variant="h5" color="text.secondary" noWrap>
              {category}
            </Typography>
          </Tooltip>
          <Tooltip title={"Distance"} arrow>
            <Typography variant="h6" color="text.secondary" noWrap>
              {distance < 1000
                ? `${Math.round(distance * 10) / 10} m`
                : `${Math.round((distance / 1000) * 10) / 10} km`}
            </Typography>
          </Tooltip>
        </Box>
      </Box>
    </Paper>
  );
}
