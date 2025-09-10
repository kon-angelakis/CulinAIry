import React from "react";
import {
  Box,
  Button,
  Container,
  Typography,
  AppBar,
  Toolbar,
  Stack,
  Paper,
  Fade,
  Grow,
  useTheme,
  styled,
  keyframes,
} from "@mui/material";
import { LocationOn, Star, Restaurant, Person } from "@mui/icons-material";
import logo from "../assets/logo.svg";
import logoRibbon from "../assets/logo_ribbon.svg";
import { useNavigate } from "react-router";
import CulinairyFooter from "../components/CulinairyFooter";

// Custom animations
const bounce = keyframes`
  0%, 100% {
    transform: translateY(0);
    animation-timing-function: cubic-bezier(0.8,0,1,1);
  }
  50% {
    transform: translateY(-10px);
    animation-timing-function: cubic-bezier(0,0,0.2,1);
  }
`;

const fadeInUp = keyframes`
  0% {
    opacity: 0;
    transform: translateY(20px);
  }
  100% {
    opacity: 1;
    transform: translateY(0);
  }
`;

// Styled components
const HeroContainer = styled(Box)(({ theme }) => ({
  minHeight: "100vh",
  minWidth: "100%",
  background: `linear-gradient(135deg, ${theme.palette.background.default} 0%, #FFF8F0 100%)`,
  position: "relative",
  overflow: "hidden",
}));

const GradientText = styled(Typography)(({ theme }) => ({
  background: `linear-gradient(135deg, ${theme.palette.primary.main}, ${theme.palette.primary.light})`,
  WebkitBackgroundClip: "text",
  WebkitTextFillColor: "transparent",
  backgroundClip: "text",
  textShadow: `0 0 20px ${theme.palette.primary.main}30`,
}));

const AnimatedIcon = styled(Box)({
  animation: `${bounce} 2s infinite`,
});

const FadeInBox = styled(Box)({
  animation: `${fadeInUp} 0.8s ease-out`,
});

const GlowButton = styled(Button)(({ theme }) => ({
  background: `linear-gradient(135deg, ${theme.palette.primary.main}, ${theme.palette.primary.light})`,
  "&:hover": {
    background: `linear-gradient(135deg, ${theme.palette.primary.dark}, ${theme.palette.primary.main})`,
    boxShadow: `0 0 40px ${theme.palette.primary.main}60`,
  },
}));

const FloatingElement = styled(Box)(({ theme }) => ({
  position: "absolute",
  opacity: 0.05,
  animation: `${bounce} 2s infinite`,
  color: theme.palette.primary.main,
}));

const HeroPageMUI = () => {
  const navigate = useNavigate();

  const theme = useTheme();
  const [mounted, setMounted] = React.useState(false);

  React.useEffect(() => {
    setMounted(true);
  }, []);

  return (
    <HeroContainer>
      {/* Header */}
      <AppBar
        position="sticky"
        elevation={0}
        sx={{
          backgroundColor: "transparent",
          backdropFilter: "blur(10px)",
        }}
      >
        <Toolbar>
          <Box sx={{ display: "flex", alignItems: "center", flexGrow: 1 }}>
            <Fade in={mounted} timeout={600}>
              <Box sx={{ display: "flex", alignItems: "center", gap: 1.5 }}>
                <img
                  src={logo}
                  alt="Culinairy Logo"
                  style={{ height: 40, width: 40 }}
                />
                <Typography
                  variant="h5"
                  component="div"
                  sx={{ fontWeight: 700, color: "text.primary" }}
                >
                  Culinairy
                </Typography>
              </Box>
            </Fade>
          </Box>

          <Fade in={mounted} timeout={800}>
            <Stack direction="row" spacing={2}>
              <Button
                color="inherit"
                size="medium"
                sx={{ color: "text.primary" }}
                onClick={() => {
                  setTimeout(() => {
                    navigate("/login");
                  }, 1000);
                }}
              >
                Log In
              </Button>
              <GlowButton
                variant="contained"
                size="medium"
                onClick={() => {
                  setTimeout(() => {
                    navigate("/register");
                  }, 1000);
                }}
              >
                Sign Up
              </GlowButton>
            </Stack>
          </Fade>
        </Toolbar>
      </AppBar>

      {/* Main Content */}
      <Container maxWidth="lg" sx={{ position: "relative", zIndex: 10 }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
            alignItems: "center",
            justifyContent: "center",
            minHeight: "100vh",
            textAlign: "center",
            py: 8,
          }}
        >
          {/* Logo Ribbon */}
          <Grow in={mounted} timeout={1000}>
            <Box sx={{ mb: 4 }}>
              <img
                src={logoRibbon}
                alt="Culinairy"
                style={{
                  height: 64,
                  opacity: 0.9,
                  transition: "opacity 0.3s ease",
                }}
                onMouseEnter={(e) => (e.currentTarget.style.opacity = "1")}
                onMouseLeave={(e) => (e.currentTarget.style.opacity = "0.9")}
              />
            </Box>
          </Grow>

          {/* Main Headline */}
          <FadeInBox sx={{ mb: 6, animationDelay: "0.2s" }}>
            <Typography
              variant="h1"
              component="h1"
              sx={{ mb: 3, color: "text.primary" }}
            >
              Discover Amazing
              <br />
              <GradientText variant="h1">Food Places</GradientText>
            </Typography>

            <Typography
              variant="h6"
              sx={{
                maxWidth: 600,
                mx: "auto",
                color: "text.secondary",
                lineHeight: 1.6,
                mb: 6,
              }}
            >
              Find the perfect restaurant, café, or food spot near you. Explore
              places, read reviews, and never miss out on great food again.
            </Typography>
          </FadeInBox>

          {/* CTA Buttons */}
          <FadeInBox sx={{ mb: 8, animationDelay: "0.4s" }}>
            <Stack
              direction={{ xs: "column", sm: "row" }}
              spacing={3}
              sx={{ alignItems: "center", justifyContent: "center" }}
            >
              <GlowButton
                variant="contained"
                size="large"
                startIcon={<LocationOn />}
                sx={{ minWidth: 200 }}
                onClick={() => {
                  setTimeout(() => {
                    navigate("/register");
                  }, 1000);
                }}
              >
                Explore Near Me
              </GlowButton>
            </Stack>
          </FadeInBox>

          {/* Feature Icons */}
          <FadeInBox sx={{ animationDelay: "0.6s" }}>
            <Stack
              direction="row"
              spacing={6}
              sx={{ justifyContent: "center", alignItems: "center" }}
            >
              <Paper
                elevation={0}
                sx={{
                  p: 3,
                  borderRadius: 3,
                  backgroundColor: "rgba(255, 107, 0, 0.1)",
                  transition: "all 0.3s ease",
                  cursor: "pointer",
                  "&:hover": {
                    backgroundColor: "rgba(255, 107, 0, 0.2)",
                    transform: "scale(1.05)",
                  },
                }}
              >
                <Stack alignItems="center" spacing={1}>
                  <Person sx={{ fontSize: 32, color: "primary.main" }} />
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    Ai Assisted
                  </Typography>
                </Stack>
              </Paper>

              <Paper
                elevation={0}
                sx={{
                  p: 3,
                  borderRadius: 3,
                  backgroundColor: "rgba(46, 125, 50, 0.1)",
                  transition: "all 0.3s ease",
                  cursor: "pointer",
                  "&:hover": {
                    backgroundColor: "rgba(46, 125, 50, 0.2)",
                    transform: "scale(1.05)",
                  },
                }}
              >
                <Stack alignItems="center" spacing={1}>
                  <AnimatedIcon>
                    <Restaurant
                      sx={{ fontSize: 32, color: "secondary.main" }}
                    />
                  </AnimatedIcon>
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    Great Food
                  </Typography>
                </Stack>
              </Paper>

              <Paper
                elevation={0}
                sx={{
                  p: 3,
                  borderRadius: 3,
                  backgroundColor: "rgba(255, 193, 7, 0.1)",
                  transition: "all 0.3s ease",
                  cursor: "pointer",
                  "&:hover": {
                    backgroundColor: "rgba(255, 193, 7, 0.2)",
                    transform: "scale(1.05)",
                  },
                }}
              >
                <Stack alignItems="center" spacing={1}>
                  <LocationOn sx={{ fontSize: 32, color: "#FF8F00" }} />
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    Nearby
                  </Typography>
                </Stack>
              </Paper>
            </Stack>
          </FadeInBox>
        </Box>
      </Container>

      {/* Background Decorative Elements */}
      <FloatingElement sx={{ top: "10%", left: "5%", animationDelay: "1s" }}>
        <Person sx={{ fontSize: 96 }} />
      </FloatingElement>
      <FloatingElement
        sx={{ top: "20%", right: "10%", animationDelay: "1.5s" }}
      >
        <Restaurant sx={{ fontSize: 80 }} />
      </FloatingElement>
      <FloatingElement sx={{ bottom: "30%", left: "8%", animationDelay: "2s" }}>
        <Star sx={{ fontSize: 64 }} />
      </FloatingElement>
      <FloatingElement
        sx={{ bottom: "10%", right: "5%", animationDelay: "2.5s" }}
      >
        <LocationOn sx={{ fontSize: 72 }} />
      </FloatingElement>

      {/* Gradient Orbs */}
      <Box
        sx={{
          position: "absolute",
          top: "25%",
          left: "25%",
          width: 384,
          height: 384,
          borderRadius: "50%",
          background: `linear-gradient(135deg, ${theme.palette.primary.main}20, ${theme.palette.primary.light}20)`,
          filter: "blur(60px)",
          animation: `${bounce} 4s infinite ease-in-out`,
        }}
      />
      <Box
        sx={{
          position: "absolute",
          bottom: "25%",
          right: "25%",
          width: 320,
          height: 320,
          borderRadius: "50%",
          background: `linear-gradient(135deg, ${theme.palette.secondary.main}20, ${theme.palette.secondary.light}20)`,
          filter: "blur(60px)",
          animation: `${bounce} 4s infinite ease-in-out 1s`,
        }}
      />
      <CulinairyFooter />
    </HeroContainer>
  );
};

export default HeroPageMUI;
