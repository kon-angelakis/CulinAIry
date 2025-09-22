import ExitToAppRoundedIcon from "@mui/icons-material/ExitToAppRounded";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import FavoriteRoundedIcon from "@mui/icons-material/FavoriteRounded";
import MenuIcon from "@mui/icons-material/Menu";
import PersonRoundedIcon from "@mui/icons-material/PersonRounded";
import ScheduleRoundedIcon from "@mui/icons-material/ScheduleRounded";
import SettingsRoundedIcon from "@mui/icons-material/SettingsRounded";
import StarRoundedIcon from "@mui/icons-material/StarRounded";
import {
  AppBar,
  Avatar,
  Box,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Toolbar,
  Typography,
} from "@mui/material";
import { useContext, useState } from "react";

import { useNavigate } from "react-router";
import { UserContext } from "../App.jsx"; // or wherever you put the context
import { ReactComponent as LogoRibbon } from "../assets/logo_ribbon.svg";

export default function CulinairyAppbar() {
  const { user, setUser } = useContext(UserContext);

  const navigate = useNavigate();

  const [drawerOpen, setDrawerOpen] = useState(false);
  const [drawerPosition, setDrawerPosition] = useState("right");

  return (
    <Box>
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
              onClick={() => {
                setDrawerOpen(true);
                setDrawerPosition("left");
              }}
            >
              <MenuIcon sx={{ color: "#fff" }} />
            </IconButton>

            <Box
              sx={{
                display: "flex",
                alignItems: "center",
                height: { xs: 40, sm: 50, md: 60 },
                mx: { xs: "auto", sm: 0 },
                "&:hover": {
                  cursor: "pointer",
                  color: "primary.dark",
                },
              }}
              onClick={() => {
                navigate("/home");
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
            <Avatar
              sx={{ bgcolor: "secondary.main", width: 50, height: 50, m: 1 }}
              src={user.pfp}
            />
            <Box
              sx={{ display: "flex", alignItems: "center" }}
              onClick={() => {
                setDrawerOpen(true);
                setDrawerPosition("right");
              }}
            >
              <Typography
                variant="subtitle1"
                sx={{ fontWeight: 500, cursor: "pointer" }}
              >
                {user.firstName}
              </Typography>
              <IconButton size="small">
                <ExpandMoreIcon />
              </IconButton>
            </Box>
          </Box>
        </Toolbar>
      </AppBar>
      {/* SETTINGS DRAWER */}
      <Drawer
        anchor={drawerPosition}
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        variant="temporary"
      >
        <Box
          sx={{
            width: { xs: 200, sm: 300 },
            display: "flex",
            flexDirection: "column",
            height: "100%",
          }}
          role="presentation"
          onClick={() => setDrawerOpen(false)}
          onKeyDown={() => setDrawerOpen(false)} // allows ESC/tab navigation to also close
        >
          <List sx={{ flexGrow: 1 }}>
            <ListItem
              sx={{
                display: "flex",
                flexDirection: "column",
                justifyContent: "center",
                alignItems: "center",
              }}
            >
              <Avatar
                src={user.pfp}
                sx={{ width: 80, height: 80, textAlign: "center", mb: 2 }}
              />
              <Typography variant="h6">{user.username}</Typography>
            </ListItem>
            <ListItem divider sx={{ my: 1 }}></ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "primary.light" },
              }}
              onClick={() => {
                navigate("/profile");
              }}
            >
              <ListItemIcon>
                <PersonRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="Profile" />
            </ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "primary.light" },
              }}
              onClick={() => {
                navigate(`${user.username}/favourites`, {
                  state: {
                    searchEndpoint: "/user/places",
                    axiosMethod: "GET",
                    formData: { params: { type: "FAVOURITES" } },
                    text: "favourite place(s)",
                  },
                });
              }}
            >
              <ListItemIcon>
                <FavoriteRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="Favourites" />
            </ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "primary.light" },
              }}
              onClick={() => {
                navigate(`${user.username}/history`, {
                  state: {
                    searchEndpoint: "/user/places",
                    axiosMethod: "GET",
                    formData: { params: { type: "RECENTLY_VIEWED" } },
                    text: "/ 16 most recents",
                  },
                });
              }}
            >
              <ListItemIcon>
                <ScheduleRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="History" />
            </ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "primary.light" },
              }}
              onClick={() => {
                navigate(`${user.username}/reviews`);
              }}
            >
              <ListItemIcon>
                <StarRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="Reviews" />
            </ListItem>
          </List>
          <List>
            <ListItem divider sx={{ my: 1 }}></ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "primary.light" },
              }}
              onClick={() => {
                navigate("/settings");
              }}
            >
              <ListItemIcon>
                <SettingsRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="Settings" />
            </ListItem>
            <ListItem
              button
              sx={{
                cursor: "pointer",
                "&:hover": { bgcolor: "secondary.main" },
              }}
              onClick={() => {
                localStorage.clear();
                window.location.reload();
              }}
            >
              <ListItemIcon>
                <ExitToAppRoundedIcon />
              </ListItemIcon>
              <ListItemText primary="Logout" />
            </ListItem>
          </List>
        </Box>
      </Drawer>
    </Box>
  );
}
