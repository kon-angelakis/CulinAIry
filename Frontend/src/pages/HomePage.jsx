import RoomRoundedIcon from "@mui/icons-material/RoomRounded";
import RestaurantRoundedIcon from "@mui/icons-material/RestaurantRounded";

import {
  Box,
  Button,
  Grid,
  Slider,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";

export default function HomePage() {
  const [formData, setFormData] = useState({
    userInput: "",
    location: null,
    radius: 5000, //matching starting slider radius
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

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  return (
    <Box>
      <Typography
        variant={"h2"}
        sx={{
          textAlign: "start",
          mb: 12,
        }}
      >
        Hungry? Start typing below to get started
      </Typography>
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
            min={100}
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
          {/* Show the grant location permission button first and iff the user accepts then proceed with showing him the search button */}
          <Typography variant="caption" color="text.secondary">
            Distance {radius}m
          </Typography>
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
              variant="contained"
              size={"large"}
              color="primary"
              sx={{ height: 56, width: "80%" }}
              onClick={() => {
                setLoadingNextPage(true);
                //Simulate a loading might implement this into an actual loading based on results gotten
                setTimeout(() => {
                  navigate("/results");
                }, 1000);
                console.log(formData);
              }}
              endIcon={<RestaurantRoundedIcon />}
              loadingPosition="start"
              loading={loadingNextPage}
            >
              Search
            </Button>
          )}
        </Grid>
      </Grid>
    </Box>
  );
}
