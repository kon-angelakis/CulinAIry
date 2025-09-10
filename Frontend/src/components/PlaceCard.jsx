import { Box, Fab, Paper, Tooltip, Typography } from "@mui/material";
import { FavoriteRounded } from "@mui/icons-material";
import { useState } from "react";
import { useNavigate } from "react-router";

export default function PlaceCard({
  id,
  thumbnail,
  name,
  category,
  rating,
  reviewCount,
}) {
  const navigate = useNavigate();

  const [elevation, setElevation] = useState(2);

  const [favourite, setFavourite] = useState(false);
  const [favouriteTooltip, setFavouriteTooltip] = useState("Add to favourites");

  const toggleFavourite = () => {
    setFavourite((prev) => !prev);
    setFavouriteTooltip(
      favourite ? "Add to favourites" : "Remove from favourites"
    );
  };

  const openDetailedPage = () => {
    setTimeout(() => {
      navigate(`/place/${id}`);
    }, 1000);
  };

  return (
    <Paper
      elevation={elevation}
      sx={{
        position: "relative",
        borderRadius: 2,
        flex: 1,
        width: "100%",
        aspectRatio: "16/10",
        maxWidth: 400,
        minWidth: 250,
        transition: "transform 0.3s ease, box-shadow 0.3s ease",
        "&:hover": {
          transform: "scale(1.05)",
          cursor: "pointer",
        },
        "&:hover .thumbnail": { filter: "grayscale(0)" },
      }}
      onMouseEnter={() => setElevation(4)}
      onMouseLeave={() => setElevation(2)}
      onClick={openDetailedPage}
    >
      <Tooltip title={favouriteTooltip} placement="left">
        <Fab
          size="small"
          onClick={toggleFavourite}
          sx={{
            position: "absolute",
            bgcolor: "transparent",
            boxShadow: "none",
            top: 12,
            right: 12,
            color: favourite ? "primary.main" : "rgba(255, 255, 255, 0.5)",
            ":hover": { bgcolor: "primary.light", color: "white" },
          }}
        >
          <FavoriteRounded />
        </Fab>
      </Tooltip>
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
      />
      <Box
        sx={{
          position: "absolute",
          textAlign: "start",
          width: "100%",
          bottom: "0",
          bgcolor: "rgba(255, 255, 255, 0.4)",
          backdropFilter: "blur(10px)",
          borderBottomLeftRadius: "inherit",
          borderBottomRightRadius: "inherit",
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <Box
          textAlign="start"
          sx={{
            ml: 2,
            maxWidth: "50%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
          }}
        >
          <Tooltip title={name} arrow placement="top">
            <Typography variant="h6" noWrap>
              {name}
            </Typography>
          </Tooltip>

          <Tooltip title={category} arrow>
            <Typography variant="subtitle1" color="text.secondary" noWrap>
              {category}
            </Typography>
          </Tooltip>
        </Box>
        <Box
          textAlign="end"
          sx={{
            mr: 2,
            maxWidth: "50%",
            display: "flex",
            flexDirection: "column",
            justifyContent: "center",
          }}
        >
          <Typography variant="subtitle1">{rating + " / 5 ⭐"}</Typography>
          <Typography variant="caption" color="text.secondary">
            ({reviewCount + " reviews"})
          </Typography>
        </Box>
      </Box>
    </Paper>
  );
}
