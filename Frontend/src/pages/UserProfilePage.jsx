import {
  Avatar,
  Box,
  Button,
  Container,
  IconButton,
  Stack,
  TextField,
  Typography,
  Paper,
  Grid,
  Divider,
  ButtonBase,
} from "@mui/material";
import { useState, useEffect, useContext } from "react";
import { Edit } from "@mui/icons-material";
import authAxios from "../config/authAxiosConfig";
import { UserContext } from "../App.jsx"; // or wherever you put the context

export default function UserProfilePage() {
  const { user, setUser } = useContext(UserContext);
  const [dataChanged, setDataChanged] = useState(false);

  const [formData, setFormData] = useState({
    username: user.username,
    firstName: user.firstName,
    lastName: user.lastName,
  });

  const resetFields = () => {
    setDataChanged(false);
    setFormData({
      username: user.username,
      firstName: user.firstName,
      lastName: user.lastName,
    });
  };

  const handleChange = (e) => {
    setDataChanged(true);
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleAvatarChange = (event) => {
    const file = event.target.files?.[0];
    if (!file) return;
    if (file.size > 25 * 1024 * 1024) {
      alert("File too large");
      return;
    }

    const reader = new FileReader();
    reader.onload = () => {
      const base64 = reader.result.split(",")[1];

      const payload = { imageFileB64: base64 };

      authAxios
        .post("/user/pfp", payload)
        .then((response) => {
          localStorage.setItem("AuthToken", response.data.data.jwtToken);
          localStorage.setItem(
            "UserDetails",
            JSON.stringify(response.data.data.userDetails)
          );
          setUser(response.data.data.userDetails);
        })
        .catch((error) => {
          console.log(error);
        });
    };
    reader.readAsDataURL(file); //to trigger avatar change more than 1 times
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    const response = await authAxios.post("/user/credentials", formData);
    localStorage.setItem("AuthToken", response.data.data.jwtToken);
    localStorage.setItem(
      "UserDetails",
      JSON.stringify(response.data.data.userDetails)
    );
    setUser(response.data.data.userDetails);
  };

  const handleDeletion = async () => {
    const confirmed = window.confirm(
      "Are you sure you want to delete your account?"
    );
    if (confirmed) {
      await authAxios.delete("/user");
      localStorage.clear();
      window.location.reload();
    }
  };

  return (
    <Box>
      <Typography variant="h2" sx={{ mb: 8 }}>
        My Profile
      </Typography>
      <form method="POST" onSubmit={handleSubmit} style={{ width: "100%" }}>
        <Grid
          container
          spacing={4}
          sx={{ display: "flex", flexDirection: { xs: "column", md: "row" } }}
        >
          <Grid
            size={{ xs: 12, md: 8 }}
            sx={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "start",
              alignItems: "center",
            }}
            gap={2}
          >
            <ButtonBase
              component="label"
              role={undefined}
              tabIndex={-1} // prevent label from tab focus
              aria-label="Avatar image"
              sx={{
                borderRadius: "40px",
                "&:has(:focus-visible)": {
                  outline: "2px solid",
                  outlineOffset: "2px",
                },
              }}
            >
              <Avatar
                src={user.pfp}
                sx={{
                  width: { xs: 128, md: 256 },
                  height: { xs: 128, md: 256 },
                  mb: 4,
                  transition: "filter 0.3s ease",
                  "&:hover": { filter: "brightness(0.5)" },
                }}
              />
              <input
                type="file"
                accept="image/*"
                style={{
                  border: 0,
                  clip: "rect(0 0 0 0)",
                  height: "1px",
                  margin: "-1px",
                  overflow: "hidden",
                  padding: 0,
                  position: "absolute",
                  whiteSpace: "nowrap",
                  width: "1px",
                }}
                onChange={handleAvatarChange}
              />
            </ButtonBase>

            <Button variant="contained" onClick={handleDeletion}>
              Delete Account
            </Button>
          </Grid>
          <Grid
            size="grow"
            sx={{
              display: "flex",
              flexDirection: "column",
              justifyContent: "space-evenly",
              alignItems: "center",
            }}
            gap={3}
          >
            <TextField
              variant="standard"
              value={user.email}
              label="Email Address"
              fullWidth
              slotProps={{
                input: {
                  readOnly: true,
                },
              }}
            />
            <TextField
              value={formData.username}
              onChange={handleChange}
              margin="normal"
              fullWidth
              id="username"
              label="Username"
              name="username"
              autoFocus
              variant="standard"
              color="secondary"
              slotProps={{ htmlInput: { maxLength: 18 } }}
            />
            <TextField
              value={formData.firstName}
              onChange={handleChange}
              margin="normal"
              fullWidth
              id="firstName"
              label="First Name"
              name="firstName"
              autoFocus
              variant="standard"
              color={user.regMethod == "LEGACY" ? "secondary" : "primary"}
              slotProps={{
                input: {
                  readOnly: user.regMethod == "LEGACY" ? false : true,
                },
              }}
            />
            <TextField
              value={formData.lastName}
              onChange={handleChange}
              margin="normal"
              fullWidth
              id="lastName"
              label="Last Name"
              name="lastName"
              autoFocus
              variant="standard"
              color={user.regMethod == "LEGACY" ? "secondary" : "primary"}
              slotProps={{
                input: {
                  readOnly: user.regMethod == "LEGACY" ? false : true,
                },
              }}
            />
            {dataChanged && (
              <Box sx={{ mt: 10 }}>
                <Button
                  variant="contained"
                  color="secondary"
                  onClick={resetFields}
                >
                  Reset Changes
                </Button>
                <Button type="submit" sx={{ ml: 4 }}>
                  Save Changes
                </Button>
              </Box>
            )}
          </Grid>
        </Grid>
      </form>
    </Box>
  );
}
