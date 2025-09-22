import RoomRoundedIcon from "@mui/icons-material/RoomRounded";
import RestaurantRoundedIcon from "@mui/icons-material/RestaurantRounded";
import DirectionsWalkRoundedIcon from "@mui/icons-material/DirectionsWalkRounded";
import DirectionsRunRoundedIcon from "@mui/icons-material/DirectionsRunRounded";
import DirectionsBikeRoundedIcon from "@mui/icons-material/DirectionsBikeRounded";
import DirectionsCarRoundedIcon from "@mui/icons-material/DirectionsCarRounded";

import {
  Box,
  Button,
  Divider,
  Grid,
  Paper,
  Slider,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import SearchResultsBox from "../components/SearchResultsBox";

export default function HomePage() {
  const [formData, setFormData] = useState({
    userInput: "",
    location: null,
    radius: 5000, //matching starting slider radius
  });

  const [curatedFormData, setCuratedFormData] = useState({
    location: null,
    radius: 5000,
  });

  const navigate = useNavigate();

  const [radius, setRadius] = useState(5000);

  const [locationGranted, setLocationGranted] = useState(false);
  const [requestingLocation, setRequestingLocation] = useState(false);

  const [loadingNextPage, setLoadingNextPage] = useState(false);

  const requestLocation = () => {
    setRequestingLocation(true);
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setRequestingLocation(false);
        setLocationGranted(true);
        // Update form location data
        setFormData({
          ...formData,
          location: {
            longitude: position.coords.longitude,
            latitude: position.coords.latitude,
          },
        });
        setCuratedFormData({
          ...curatedFormData,
          location: {
            longitude: position.coords.longitude,
            latitude: position.coords.latitude,
          },
        });
      },
      (error) => {
        setLocationGranted(false);
        setRequestingLocation(false);
        alert("Please enable location to continue");
      },
      {
        enableHighAccuracy: true,
      }
    );
  };

  useEffect(() => {
    requestLocation();
  }, []);

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    setLoadingNextPage(true);

    e.preventDefault();
    //Simulate a loading might implement this into an actual loading based on results gotten
    setTimeout(() => {
      navigate("/results", {
        state: {
          searchEndpoint: "/search",
          axiosMethod: "POST",
          formData: formData,
          text: "entries",
        },
      });
    }, 1000);
    console.log(formData);
  };

  function getDirectionsIcon(radius) {
    if (radius < 2000) {
      return <DirectionsWalkRoundedIcon fontSize="medium" />;
    } else if (radius < 5000) {
      return <DirectionsRunRoundedIcon fontSize="medium" />;
    } else if (radius < 7000) {
      return <DirectionsBikeRoundedIcon fontSize="medium" />;
    } else {
      return <DirectionsCarRoundedIcon fontSize="medium" />;
    }
  }

  return (
    <Box>
      <Typography
        variant={"h2"}
        sx={{
          textAlign: "start",
          mb: 12,
        }}
      >
        HUNGRY? START TYPING BELOW TO GET STARTED
      </Typography>
      <form method="POST" onSubmit={handleSubmit}>
        <Grid
          container
          rowSpacing={4}
          sx={{ mb: 6, flexDirection: { xs: "column", md: "row" } }}
        >
          <TextField
            name="userInput"
            onChange={handleChange}
            label="Search for restaurants, cafes..."
            variant="outlined"
            multiline
            fullWidth
            required
          />

          <Grid
            size={{ xs: "grow", md: 8 }}
            textAlign={"start"}
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
            }}
          >
            <Slider
              name="radius"
              value={radius}
              onChange={(e, val) => {
                setRadius(val);
                handleChange(e);
              }}
              min={500}
              max={10000}
              step={100}
              shiftStep={500}
              valueLabelDisplay="auto"
            />
          </Grid>

          <Grid
            size="grow"
            sx={{
              display: "flex",
              justifyContent: "center",
              alignItems: "center",
              flexDirection: "column",
            }}
          >
            {/* Display a distance indication mark */}
            <Typography variant="caption" color="text.secondary">
              {getDirectionsIcon(radius)}
            </Typography>
            <Typography variant="caption" color="text.secondary">
              Distance <strong>{radius / 1000}</strong>km
            </Typography>
            {/* Show the grant location permission button first and iff the user accepts then proceed with showing him the search button */}
            {!locationGranted ? (
              <Button
                variant="outlined"
                size={"small"}
                color="secondary"
                sx={{ height: 56, width: "80%" }}
                onClick={requestLocation}
                endIcon={<RoomRoundedIcon color="primary" />}
                loadingPosition="start"
                loading={requestingLocation}
              >
                {requestingLocation ? "Detecting.." : "Enable Location"}
              </Button>
            ) : (
              <Button
                type="submit"
                variant="contained"
                size={"large"}
                color="primary"
                sx={{ height: 56, width: "80%" }}
                endIcon={<RestaurantRoundedIcon />}
                loadingPosition="start"
                loading={loadingNextPage}
              >
                Search
              </Button>
            )}
          </Grid>
        </Grid>
      </form>
      <Divider sx={{ my: 16 }} />
      {locationGranted && (
        <Box sx={{ mt: "20vh" }}>
          <SearchResultsBox
            endpoint={"/user/curated"}
            method={"POST"}
            formData={curatedFormData}
            text={"nearby place(s) you might be interested in"}
          />
        </Box>
      )}
    </Box>
  );
}
