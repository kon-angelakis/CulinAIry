import { Box, Fab, Paper, Tooltip, Typography } from "@mui/material";
import { FavoriteRounded } from "@mui/icons-material";
import { useState } from "react";

export default function PlaceCard({
  id,
  thumbnail,
  name,
  category,
  rating,
  reviewCount,
}) {
  const [elevation, setElevation] = useState(2);

  const [favourite, setFavourite] = useState(false);
  const [favouriteTooltip, setFavouriteTooltip] = useState("Add to favourites");

  const toggleFavourite = () => {
    setFavourite((prev) => !prev);
    setFavouriteTooltip(
      favourite ? "Add to favourites" : "Remove from favourites"
    );
  };

  return (
    <Paper
      elevation={elevation}
      data-id={id}
      sx={{
        position: "relative",
        borderRadius: 2,
        flex: 1,
        width: "100%",
        aspectRatio: "16/10",
        maxWidth: 400,
        minWidth: 275,

        transition: "transform 0.3s ease, box-shadow 0.3s ease",
        "&:hover": {
          transform: "scale(1.05)",
          boxShadow: "0 10px 20px rgba(0,0,0,0.2)",
          cursor: "pointer",
        },
        "&:hover .thumbnail": { filter: "grayscale(0)" },
      }}
      onMouseEnter={() => setElevation(8)}
      onMouseLeave={() => setElevation(4)}
    >
      <Tooltip title={favouriteTooltip} placement="left">
        <Fab
          size="small"
          onClick={toggleFavourite}
          sx={{
            position: "absolute",
            bgcolor: "transparent",
            top: 12,
            right: 12,
            color: favourite ? "primary.main" : "primary.dark",
            ":hover": { bgcolor: "primary.light" },
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
          bgcolor: "rgba(255, 255, 255, 0.05)",
          backdropFilter: "blur(10px)",
          borderBottomLeftRadius: "inherit",
          borderBottomRightRadius: "inherit",
          display: "flex",
          flexDirection: "row",
          justifyContent: "space-between",
        }}
      >
        <Box textAlign="start" sx={{ ml: 2, maxWidth: "70%" }}>
          <Tooltip title={name} arrow placement="top">
            <Typography variant="h5" noWrap>
              {name}
            </Typography>
          </Tooltip>

          <Tooltip title={category} arrow>
            <Typography variant="subtitle1" color="text.secondary" noWrap>
              {category}
            </Typography>
          </Tooltip>
        </Box>
        <Box textAlign="end" sx={{ mr: 2 }}>
          <Typography variant="h6">{rating + " / 5 ⭐"}</Typography>
          <Typography variant="caption" color="text.secondary">
            ({reviewCount + " reviews"})
          </Typography>
        </Box>
      </Box>
    </Paper>
  );
}
