import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import SearchRoundedIcon from "@mui/icons-material/SearchRounded";
import {
  Box,
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

export default function SearchBar() {
  const navigate = useNavigate();
  const [open, setOpen] = useState(false);
  const anchorRef = useRef(null);
  const [radius, setRadius] = useState(5000);

  const { precise, locationGranted, location, handlePreciseToggle } =
    usePreciseLocation();
  const [formData, setFormData] = useState({
    userInput: "",
    location: location,
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
      location: location,
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
            "&:hover": { cursor: "pointer" },
            color: "text.secondary",
          }}
          onClick={() => {
            setOpen((prev) => !prev);
          }}
        />

        <Popper
          open={open}
          anchorEl={anchorRef.current}
          placement="bottom"
          style={{ zIndex: 1300 }}
        >
          <Paper
            elevation={3}
            sx={{
              p: 2,
              mt: 1,
              minWidth: 250,
              bgcolor: "background.paper",
            }}
          >
            <Typography variant="subtitle1" gutterBottom>
              Advanced Search
            </Typography>
            <FormControlLabel
              control={
                <Switch
                  checked={precise && locationGranted}
                  onChange={handlePreciseToggle}
                />
              }
              label={"Precise location"}
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
          </Paper>
        </Popper>
      </form>
    </Box>
  );
}
