import { Box, Paper, Tooltip, Typography } from "@mui/material";
import { useContext, useState } from "react";
import { useNavigate } from "react-router";
import { ThemeContext } from "../App.jsx";
import authAxios from "../config/authAxiosConfig";

export default function PlaceCard({
  id,
  thumbnail,
  name,
  category,
  rating,
  reviewCount,
}) {
  const { mode } = useContext(ThemeContext);

  const navigate = useNavigate();

  const [elevation, setElevation] = useState(2);

  //Add place to history
  const handleClick = async () => {
    const response = await authAxios.post(`/user/history/${id}`).then(() => {
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
      onClick={handleClick}
    >
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
          bgcolor:
            mode == "light"
              ? "rgba(211, 211, 211, 0.5)"
              : " rgba(39, 39, 39, 0.5)",
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
            maxWidth: "90%",
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
      </Box>
    </Paper>
  );
}
