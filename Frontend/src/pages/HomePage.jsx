import DirectionsBikeRoundedIcon from "@mui/icons-material/DirectionsBikeRounded";
import DirectionsCarRoundedIcon from "@mui/icons-material/DirectionsCarRounded";
import DirectionsRunRoundedIcon from "@mui/icons-material/DirectionsRunRounded";
import DirectionsWalkRoundedIcon from "@mui/icons-material/DirectionsWalkRounded";
import RestaurantRoundedIcon from "@mui/icons-material/RestaurantRounded";

import {
  Box,
  Button,
  FormControlLabel,
  Grid,
  Slider,
  Switch,
  TextField,
  Typography,
} from "@mui/material";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import RecommendationBox from "../components/RecommendationBox";
import authAxios from "../config/authAxiosConfig";
import usePreciseLocation from "../hooks/usePreciseLocation";

export default function HomePage() {
  const [geoData, setGeoData] = useState(null);

  const [formData, setFormData] = useState({
    userInput: "",
    location: null,
    radius: 5000, //matching starting slider radius
  });

  const navigate = useNavigate();

  const [radius, setRadius] = useState(5000);

  const [loadingNextPage, setLoadingNextPage] = useState(false);

  //Gets the user's approximate location on initial page load and store it in localStorage
  useEffect(() => {
    async function getGeolocation() {
      const result = await authAxios.get("/search/geolocation");
      const city = result.data.data.city;
      const coords = {
        longitude: result.data.data.lon,
        latitude: result.data.data.lat,
      };
      localStorage.setItem("UserGeolocationCity", city);
      localStorage.setItem(
        "UserGeolocationApproximateLocation",
        JSON.stringify(coords)
      );
      setGeoData({ city: city, location: coords });
    }
    getGeolocation();
  }, []);

  const { precise, locationGranted, location, handlePreciseToggle } =
    usePreciseLocation();

  const handleFormChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    const submittedForm = {
      ...formData,
      location: location || geoData.location,
    };
    setFormData(submittedForm);
    setLoadingNextPage(true);
    e.preventDefault();
    //Simulate a loading might implement this into an actual loading based on results gotten
    setTimeout(() => {
      navigate("/results", {
        state: {
          formData: submittedForm,
        },
      });
    }, 1000);
    console.log(submittedForm);
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
      {geoData && (
        <>
          <Typography
            variant={"h2"}
            sx={{
              textAlign: "start",
              mb: 12,
            }}
          >
            Looking for fun places in <u>{geoData.city}</u>? Start typing below
            to begin your <u>Culinairy</u> journey!
          </Typography>
          <form method="POST" onSubmit={handleSubmit}>
            <Grid
              container
              rowSpacing={4}
              sx={{
                mb: 6,
                flexDirection: { xs: "column", md: "row" },
                width: "80%",
                mx: "auto",
              }}
            >
              <TextField
                name="userInput"
                onChange={handleFormChange}
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
                    handleFormChange(e);
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
                <FormControlLabel
                  control={
                    <Switch
                      checked={precise && locationGranted}
                      onChange={handlePreciseToggle}
                    />
                  }
                  label={"Precise location"}
                />
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
              </Grid>
            </Grid>
          </form>
          <RecommendationBox
            title={"Hot places around you"}
            endpoint={"/recommendations/trending"}
            method={"POST"}
            formData={location || geoData.location}
            height={450}
          />

          <RecommendationBox
            title={"Top gainers amongst others in your area"}
            endpoint={"/recommendations/best"}
            method={"POST"}
            formData={location || geoData.location}
            height={450}
          />
          <RecommendationBox
            title={"Here's some places we think you will love"}
            endpoint={"/recommendations/curated"}
            method={"POST"}
            formData={location || geoData.location}
            height={450}
          />
          <RecommendationBox
            title={"You liked these places so why not visit again"}
            endpoint={"/recommendations/revisit"}
            method={"POST"}
            formData={location || geoData.location}
            height={450}
          />
        </>
      )}
    </Box>
  );
}
