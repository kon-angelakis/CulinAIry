import {
  Alert,
  Box,
  Button,
  Container,
  Link,
  Paper,
  Snackbar,
  TextField,
  Typography,
} from "@mui/material";
import { useState } from "react";
import apiAxios from "../config/apiAxiosConfig.js";

export default function RegisterPage() {
  const [alert, setAlert] = useState({
    open: false,
    severity: "",
    message: "",
  });

  const [formData, setFormData] = useState({
    firstName: "",
    lastName: "",
    email: "",
    username: "",
    password: "",
    cpassword: "",
  });

  const [errors, setErrors] = useState({});

  const validateEmail = (email) => {
    //valid email format
    const regex = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/;
    return regex.test(email);
  };

  const validateField = (name, value) => {
    let message = "";

    if (!value) message = "This field is required";
    else if (name === "email" && !validateEmail(value))
      message = "Enter a valid email address";
    else if (name === "username" && value.length > 18)
      message = "Max 18 characters allowed";
    else if (name === "cpassword" && value !== formData.password)
      message = "Passwords do not match";

    setErrors((prev) => ({ ...prev, [name]: message }));
  };

  const handleChange = (e) => {
    setFormData({
      ...formData,
      [e.target.name]: e.target.value,
    });
  };

  const handleBlur = (e) => {
    const { name, value } = e.target;
    validateField(name, value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();

    // Validate all fields before submitting
    Object.keys(formData).forEach((key) => validateField(key, formData[key]));

    const hasErrors = Object.values(errors).some((err) => err);
    if (hasErrors) return;

    apiAxios
      .post("/auth/register", formData)
      .then((response) => {
        setAlert({
          open: true,
          severity: response.data.success ? "success" : "error",
          message: response.data.message,
        });
      })
      .catch((error) => {
        throw error;
      });
  };

  return (
    <Box sx={{ minHeight: "100vh", display: "flex", flexDirection: "column" }}>
      <Snackbar
        sx={{ position: "fixed" }}
        open={alert.open}
        autoHideDuration={2000} // 2 secs
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
          sx={{
            p: { xs: 5, sm: 10 },
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            maxWidth: 500,
            width: "100%",
            borderRadius: 3,
          }}
        >
          <Typography variant="h5" sx={{ mb: 3, fontWeight: 600 }}>
            Sign Up to Culinairy
          </Typography>
          <form onSubmit={handleSubmit} method="POST" style={{ width: "100%" }}>
            <Box
              sx={{
                display: "flex",
                gap: 2,
                width: "100%",
                flexDirection: { xs: "column", sm: "row" },
              }}
            >
              <TextField
                fullWidth
                id="firstName"
                name="firstName"
                label="First Name"
                variant="standard"
                color="secondary"
                value={formData.firstName}
                onChange={handleChange}
              />
              <TextField
                fullWidth
                id="lastName"
                name="lastName"
                label="Last Name"
                variant="standard"
                color="secondary"
                value={formData.lastName}
                onChange={handleChange}
              />
            </Box>

            <TextField
              margin="normal"
              required
              fullWidth
              type="email"
              id="email"
              name="email"
              label="Email Address"
              variant="standard"
              color="secondary"
              value={formData.email}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!errors.email}
              helperText={errors.email}
            />
            <TextField
              margin="normal"
              required
              fullWidth
              id="username"
              name="username"
              label="Username"
              variant="standard"
              color="secondary"
              value={formData.username}
              onChange={handleChange}
              onBlur={handleBlur}
              error={!!errors.username}
              slotProps={{ htmlInput: { maxLength: 18 } }}
              helperText={errors.username || "Max 18 characters"}
            />

            <Box
              sx={{
                display: "flex",
                gap: 2,
                width: "100%",
                flexDirection: { xs: "column", sm: "row" },
                mt: 2,
              }}
            >
              <TextField
                fullWidth
                required
                name="password"
                label="Password"
                type="password"
                id="password"
                variant="standard"
                color="secondary"
                value={formData.password}
                onChange={handleChange}
                onBlur={handleBlur}
                error={!!errors.password}
                helperText={errors.password}
              />
              <TextField
                fullWidth
                required
                name="cpassword"
                label="Confirm Password"
                type="password"
                id="cpassword"
                variant="standard"
                color="secondary"
                value={formData.cpassword}
                onChange={handleChange}
                onBlur={handleBlur}
                error={!!errors.cpassword}
                helperText={errors.cpassword}
              />
            </Box>

            <Button
              type="submit"
              fullWidth
              variant="contained"
              sx={{ mt: 4, mb: 2, py: 1.2 }}
            >
              Sign Up
            </Button>
          </form>
          <Box
            sx={{
              display: "flex",
              justifyContent: "flex-start",
              width: "100%",
              p: 1,
            }}
          >
            <Link href="/login" underline="hover">
              Already have an account? Sign In
            </Link>
          </Box>
        </Paper>
      </Container>
    </Box>
  );
}
