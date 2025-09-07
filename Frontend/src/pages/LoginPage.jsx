import {
  Alert,
  Box,
  Button,
  Container,
  Divider,
  Link,
  Paper,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import GoogleButton from "../components/GoogleButton.jsx";

import { useEffect, useState } from "react";
import { useNavigate } from "react-router";
import apiAxios from "../config/apiAxiosConfig.js";
import authAxios from "../config/authAxiosConfig.js";

export default function LoginPage() {
  const navigate = useNavigate(); //Skip login if already logined
  useEffect(() => {
    authAxios
      .get("/auth/authenticated")
      .then(() => navigate("/home"))
      .catch(() => navigate("/login"));
  }, [navigate]);

  const [formData, setFormData] = useState({
    user: "",
    pass: "",
  });

  const [alert, setAlert] = useState({
    open: false,
    severity: "",
    message: "",
  });

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const saveAuthenticationDetails = (response) => {
    localStorage.setItem("AuthToken", response.data.data.jwtToken);
    localStorage.setItem(
      "UserDetails",
      JSON.stringify(response.data.data.userDetails)
    );
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    apiAxios
      .post("/auth/login", formData)
      .then((response) => {
        console.log(response);
        setAlert({
          open: true,
          severity: response.data.success ? "success" : "error",
          message: response.data.message,
        });
        if (response.data.success) {
          saveAuthenticationDetails(response);
          window.location.reload(); //Refresh to redirect to homepage via useEffect
        }
      })
      .catch((error) => {
        throw error;
      });
  };

  return (
    <Box
      sx={{
        minHeight: "100vh",
        display: "flex",
        flexDirection: "column",
      }}
    >
      <Snackbar //could make this a custom component
        sx={{ position: "fixed" }}
        open={alert.open}
        autoHideDuration={2000} // disappears after 2 seconds
        onClose={() => setAlert({ ...alert, open: false })}
        anchorOrigin={{ vertical: "top", horizontal: "center" }}
      >
        <Alert
          onClose={() => setAlert({ ...alert, open: false })}
          severity={alert.severity}
          sx={{ width: "100%" }}
        >
          {alert.message}
        </Alert>
      </Snackbar>
      <Container
        sx={{
          scale: { xs: "0.9", sm: "1" },
          flex: 1,
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <Paper
          elevation={6}
          borderRadius={15}
          sx={{
            p: { xs: 5, sm: 10 },
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            maxWidth: 400,
            width: "100%",
          }}
        >
          <Typography variant="h5" sx={{ mb: 2, fontWeight: 600 }}>
            Sign In to Culinairy
          </Typography>
          <form onSubmit={handleSubmit} method="POST" style={{ width: "100%" }}>
            <TextField
              value={formData.user}
              onChange={handleChange}
              margin="normal"
              required
              fullWidth
              id="user"
              label="Username or Email"
              name="user"
              autoComplete="user"
              autoFocus
              variant="standard"
              color="secondary"
            />
            <TextField
              value={formData.pass}
              onChange={handleChange}
              margin="normal"
              required
              fullWidth
              name="pass"
              label="Password"
              type="password"
              id="pass"
              autoComplete="current-password"
              variant="standard"
              color="secondary"
            />

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 3, mb: 2, py: 1.2 }}
            >
              Sign In
            </Button>
          </form>
          <Divider t sx={{ width: "100%", mb: 2 }}>
            OR
          </Divider>
          <GoogleButton />
          <Box
            sx={{
              display: "flex",
              justifyContent: "space-between",
              width: "100%",
              flexDirection: "column",
              gap: 2,
              p: 2,
            }}
          >
            <Link href="#" underline="hover">
              Forgot password?
            </Link>
            <Link href="/register" underline="hover">
              Don't have an account? Sign Up
            </Link>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}
