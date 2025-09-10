import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import MenuIcon from "@mui/icons-material/Menu";
import {
  AppBar,
  Avatar,
  Box,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Toolbar,
  Typography,
} from "@mui/material";
import { useState } from "react";

import { ReactComponent as LogoRibbon } from "../assets/logo_ribbon.svg";
import { useNavigate } from "react-router";

export default function CulinairyAppbar() {
  const navigate = useNavigate();

  const [drawerOpen, setDrawerOpen] = useState(false);

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
              onClick={() => setDrawerOpen(true)}
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
    </Box>
  );
}
