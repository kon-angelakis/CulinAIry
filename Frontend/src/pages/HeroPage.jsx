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
import { alpha } from "@mui/material/styles";
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

// Styled components (theme-aware)
const HeroContainer = styled(Box)(({ theme }) => {
  const isDark = theme.palette.mode === "dark";

  // softer gradients so white isn't aggressive
  const bgStart = theme.palette.background.default;
  const bgEnd = isDark
    ? alpha(theme.palette.background.paper, 0.92)
    : alpha(theme.palette.background.paper, 0.98);

  const overlay = isDark
    ? alpha(theme.palette.primary.main, 0.08)
    : alpha(theme.palette.secondary.main, 0.2);

  return {
    minHeight: "100vh",
    minWidth: "100%",
    background: `linear-gradient(135deg, ${bgStart} 0%, ${bgEnd} 100%)`,
    position: "relative",
    overflow: "hidden",
    // subtle overlay to add warmth / depth depending on mode
    "&::after": {
      content: '""',
      position: "absolute",
      inset: 0,
      background: `linear-gradient(180deg, transparent 0%, ${overlay} 100%)`,
      pointerEvents: "none",
    },
  };
});

const GradientText = styled(Typography)(({ theme }) => ({
  // avoid heavy glow by lowering alpha and removing hard white
  background: `linear-gradient(135deg, ${alpha(
    theme.palette.primary.main,
    0.98
  )}, ${alpha(theme.palette.primary.main, 0.75)})`,
  WebkitBackgroundClip: "text",
  WebkitTextFillColor: "transparent",
  backgroundClip: "text",
  textShadow: `0 0 10px ${alpha(theme.palette.primary.main, 0.12)}`,
}));

const AnimatedIcon = styled(Box)(({ theme }) => ({
  animation: `${bounce} 2s infinite`,
  color: theme.palette.primary.main,
}));

const FadeInBox = styled(Box)(() => ({
  animation: `${fadeInUp} 0.8s ease-out`,
}));

const GlowButton = styled(Button)(({ theme }) => ({
  background: `linear-gradient(135deg, ${theme.palette.primary.main}, ${alpha(
    theme.palette.primary.main,
    0.85
  )})`,
  color: theme.palette.primary.contrastText,
  "&:hover": {
    background: `linear-gradient(135deg, ${alpha(
      theme.palette.primary.dark
        ? theme.palette.primary.dark
        : theme.palette.primary.main,
      1
    )}, ${theme.palette.primary.main})`,
    boxShadow: `0 0 28px ${alpha(theme.palette.primary.main, 0.25)}`,
  },
}));

// floating decorative elements should be neutral gray-ish and visible in both modes
const FloatingElement = styled(Box)(({ theme }) => {
  const grayVisible =
    theme.palette.mode === "dark"
      ? theme.palette.grey?.[400] ?? alpha("#fff", 0.45)
      : theme.palette.grey?.[800] ?? alpha("#000", 0.45);

  return {
    position: "absolute",
    opacity: 0.08,
    animation: `${bounce} 2s infinite`,
    color: grayVisible,
    // ensure children icons inherit this color
    "& .MuiSvgIcon-root": {
      color: "inherit",
      opacity: 0.95,
    },
  };
});

const HeroPageMUI = () => {
  const navigate = useNavigate();

  const theme = useTheme();
  const [mounted, setMounted] = React.useState(false);

  React.useEffect(() => {
    setMounted(true);
  }, []);

  // derived helper colors for inline sx use (so orbs respect mode)
  const orbPrimary = alpha(
    theme.palette.primary.main,
    theme.palette.mode === "dark" ? 0.1 : 0.08
  );
  const orbSecondary = alpha(
    theme.palette.secondary.main,
    theme.palette.mode === "dark" ? 0.08 : 0.08
  );

  // small helper: image filter for SVGs so they read white on dark mode
  const svgFilter =
    theme.palette.mode === "dark" ? "brightness(0) invert(1)" : "none";

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
                {/* use Box component for img so we can use sx */}
                <Box
                  component="img"
                  src={logo}
                  alt="Culinairy Logo"
                  sx={{
                    height: 40,
                    width: 40,
                    filter: svgFilter,
                    transition: "filter .2s ease, opacity .15s ease",
                  }}
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
              <Box
                component="img"
                src={logoRibbon}
                alt="Culinairy"
                sx={{
                  height: 64,
                  opacity: 0.95,
                  transition: "opacity 0.3s ease, filter .15s ease",
                  filter: svgFilter,
                }}
                onMouseEnter={(e) => (e.currentTarget.style.opacity = "1")}
                onMouseLeave={(e) => (e.currentTarget.style.opacity = "0.95")}
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
                  // subtle paper tone that stays gentle in both modes
                  backgroundColor: (t) =>
                    t.palette.mode === "dark"
                      ? alpha(t.palette.background.paper, 0.03)
                      : alpha(t.palette.background.paper, 0.015),
                  transition: "all 0.25s ease",
                  cursor: "pointer",
                  "&:hover": { transform: "scale(1.05)" },
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
                  backgroundColor: (t) =>
                    t.palette.mode === "dark"
                      ? alpha(t.palette.background.paper, 0.03)
                      : alpha(t.palette.background.paper, 0.015),
                  transition: "all 0.25s ease",
                  cursor: "pointer",
                  "&:hover": { transform: "scale(1.05)" },
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
                  backgroundColor: (t) =>
                    t.palette.mode === "dark"
                      ? alpha(t.palette.background.paper, 0.03)
                      : alpha(t.palette.background.paper, 0.015),
                  transition: "all 0.25s ease",
                  cursor: "pointer",
                  "&:hover": { transform: "scale(1.05)" },
                }}
              >
                <Stack alignItems="center" spacing={1}>
                  <LocationOn sx={{ fontSize: 32, color: "warning.main" }} />
                  <Typography variant="caption" sx={{ fontWeight: 600 }}>
                    Nearby
                  </Typography>
                </Stack>
              </Paper>
            </Stack>
          </FadeInBox>
        </Box>
      </Container>

      {/* Background Decorative Elements (neutral grayish so visible in both modes) */}
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

      {/* Gradient Orbs (toned down) */}
      <Box
        sx={{
          position: "absolute",
          top: "25%",
          left: "25%",
          width: 384,
          height: 384,
          borderRadius: "50%",
          background: `linear-gradient(135deg, ${orbPrimary}, ${alpha(
            theme.palette.primary.main,
            0.04
          )})`,
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
          background: `linear-gradient(135deg, ${orbSecondary}, ${alpha(
            theme.palette.secondary.main,
            0.03
          )})`,
          filter: "blur(60px)",
          animation: `${bounce} 4s infinite ease-in-out 1s`,
        }}
      />
      <CulinairyFooter />
    </HeroContainer>
  );
};

export default HeroPageMUI;
