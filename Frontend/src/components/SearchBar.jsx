import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import {
  Box,
  Button,
  Divider,
  FormControlLabel,
  InputAdornment,
  Paper,
  Popper,
  Slider,
  Switch,
  TextField,
  Typography,
} from "@mui/material";
import { useRef, useState } from "react";
import { useNavigate } from "react-router";
import usePreciseLocation from "../hooks/usePreciseLocation";
import Grow from "@mui/material/Grow";
import { RestaurantRounded } from "@mui/icons-material";

export default function SearchBar() {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const anchorRef = useRef(null);
  const [radius, setRadius] = useState(5000);

  const { precise, locationGranted, userLocation, handlePreciseToggle } =
    usePreciseLocation();
  const [formData, setFormData] = useState({
    userInput: "",
    location: userLocation,
    radius: 5000,
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleSubmit = (e) => {
    const submittedForm = {
      ...formData,
      location: userLocation,
    };
    setFormData(submittedForm);
    e.preventDefault();
    //Simulate a loading might implement this into an actual loading based on results gotten
    setTimeout(() => {
      navigate("/results", {
        state: {
          searchEndpoint: "/search/places",
          axiosMethod: "POST",
          formData: submittedForm,
          title: "Search results",
        },
      });
    }, 1000);
    console.log(submittedForm);
  };

  return (
    <Box
      sx={{
        position: "relative",
        display: "flex",
        alignItems: "center",
        mx: "auto",
        width: { xs: "70%", sm: "50%", md: "40%" },
      }}
      ref={anchorRef}
    >
      <form
        method="POST"
        onSubmit={handleSubmit}
        style={{ display: "contents" }}
      >
        <TextField
          name="userInput"
          required
          placeholder="Search for places"
          variant="outlined"
          size="small"
          fullWidth
          onChange={handleChange}
          slotProps={{
            input: {
              startAdornment: (
                <InputAdornment position="start">
                  <SearchRoundedIcon />
                </InputAdornment>
              ),
            },
          }}
        />
        <ExpandMoreIcon
          sx={{
            position: "relative",
            ml: 1,
            transition: "transform 0.3s ease",
            transform: open ? "rotate(180deg)" : "rotate(0deg)",
            color: "text.secondary",
            p: 0.5,
            borderRadius: "50%",
            "&:hover": {
              cursor: "pointer",
              bgcolor: "action.hover",
            },
          }}
          onClick={() => setOpen((prev) => !prev)}
        />

        <Popper
          open={open}
          anchorEl={anchorRef.current}
          placement="bottom"
          style={{ zIndex: 1300 }}
          transition
        >
          {({ TransitionProps }) => (
            <Grow {...TransitionProps} timeout={250}>
              <Paper
                elevation={6}
                sx={{
                  p: 4,
                  mt: 1,
                  minWidth: 280,
                  borderRadius: 3,
                  bgcolor: "background.paper",
                }}
              >
                <Typography variant="h6" gutterBottom textAlign={"center"}>
                  Advanced Search
                </Typography>

                <FormControlLabel
                  control={
                    <Switch
                      checked={precise && locationGranted}
                      onChange={handlePreciseToggle}
                    />
                  }
                  label="Precise location"
                />

                <Typography variant="body2" gutterBottom>
                  Filter by distance:
                </Typography>

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
                <Divider sx={{ my: 2 }} />
                <Button
                  type="submit"
                  variant="contained"
                  size={"large"}
                  color="primary"
                  sx={{ height: 48, width: "100%" }}
                  endIcon={<RestaurantRounded />}
                  onClick={handleSubmit}
                >
                  Search
                </Button>
              </Paper>
            </Grow>
          )}
        </Popper>
      </form>
    </Box>
  );
}
