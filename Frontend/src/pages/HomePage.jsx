import {
  Paper,
  AppBar,
  Toolbar,
  Box,
  Avatar,
  Typography,
  IconButton,
  Button,
  TextField,
  Slider,
  Grid,
  Drawer,
  List,
  ListItem,
  ListItemText,
  alpha,
} from "@mui/material";
import MenuIcon from "@mui/icons-material/Menu";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import { ReactComponent as LogoRibbon } from "../assets/logo_ribbon.svg";
import { useState } from "react";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";

export default function HomePage() {
  const [drawerOpen, setDrawerOpen] = useState(false);
  const [searching, setSearching] = useState(false);
  const [radius, setRadius] = useState(5000);

  return (
    <Box>
      {/* APPBAR */}
      <AppBar sx={{ px: 2 }}>
        <Toolbar sx={{ display: "flex", justifyContent: "space-between" }}>
          {/* LEFT SIDE */}
          <Box
            sx={{
              display: "flex",
              alignItems: "center",
              justifyContent: "center",
            }}
          >
            <IconButton
              sx={{ display: { xs: "flex", sm: "none" } }}
              onClick={() => setDrawerOpen(true)}
            >
              <MenuIcon sx={{ color: "#fff" }} />
            </IconButton>

            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                height: { xs: 35, sm: 50, md: 60 },
                mx: { xs: "auto", sm: 0 },
                "&:hover": {
                  cursor: "pointer",
                  color: "primary.dark",
                },
              }}
              onClick={() => {
                window.location.href = "/home";
              }}
            >
              <LogoRibbon
                style={{
                  width: "auto",
                  height: "110%",
                  display: "block",
                }}
              />
            </Box>
          </Box>

          {/* RIGHT SIDE */}
          <Box
            sx={{
              display: { xs: "none", sm: "flex" },
              alignItems: "center",
              gap: 1,
            }}
          >
            <Avatar sx={{ bgcolor: "secondary.main" }}>{"U"}</Avatar>
            <Box sx={{ display: "flex", alignItems: "center" }}>
              <Typography
                variant="subtitle1"
                sx={{ fontWeight: 500, cursor: "pointer" }}
              >
                {"Firstname"}
              </Typography>
              <IconButton size="small">
                <ExpandMoreIcon />
              </IconButton>
            </Box>
          </Box>
        </Toolbar>
      </AppBar>
      {/* HAMBURGER DRAWER */}
      <Drawer
        anchor="left"
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
      >
        <Box sx={{ width: 250 }} role="presentation">
          <List>
            <ListItem button>
              <ListItemText primary="Profile" />
            </ListItem>
            <ListItem button>
              <ListItemText primary="Settings" />
            </ListItem>
            <ListItem button>
              <ListItemText primary="Logout" />
            </ListItem>
          </List>
        </Box>
      </Drawer>
      {/* MAIN PAGE */}
      <Paper elevation={2} sx={{ py: 10, px: 2 }}>
        {!searching && (
          <Box>
            <Typography
              variant="h3"
              sx={{
                textAlign: "start",
                fontWeight: 700,
                mb: 4,
              }}
            >
              Hungry? Start typing below to get started
            </Typography>

            <Box
              sx={{
                display: "flex",
                flexDirection: { xs: "column", md: "row" },
                alignItems: "center",
                gap: 2,
                mb: 4,
              }}
            >
              <TextField
                label="Search for restaurants, cafes..."
                variant="outlined"
                fullWidth
              />

              <Box sx={{ width: { xs: "100%", sm: 200 } }}>
                <Typography gutterBottom>Radius (meters)</Typography>
                <Slider
                  value={radius}
                  onChange={(e, val) => setRadius(val)}
                  min={0}
                  max={20000}
                  valueLabelDisplay="auto"
                />
              </Box>

              <Button variant="contained" color="primary" sx={{ height: 56 }}>
                Use My Location
              </Button>

              <Button
                variant="contained"
                color="secondary"
                sx={{ height: 56 }}
                onClick={() => setSearching(true)}
              >
                Search
              </Button>
            </Box>
          </Box>
        )}
        {searching && (
          <Box>
            <Typography variant="h4" sx={{ mb: 4 }}>
              Found X results
            </Typography>

            <Grid container spacing={2} justifyContent="space-evenly">
              {Array.from({ length: 11 }).map((_, idx) => (
                <Grid
                  item
                  key={idx}
                  sx={{
                    flex: "1 1 275px", //container dynamic shrinking
                    maxWidth: 400,
                    width: { xs: "275px" }, //on mobiles start with the smallest possible size
                  }}
                >
                  <SkeletonPlaceCard />
                </Grid>
              ))}
            </Grid>
          </Box>
        )}
      </Paper>
    </Box>
  );
}
