import { Navigate, Route, Routes } from "react-router";
import "./App.css";
import PageLayout from "./config/PageLayout.jsx";
import ProtectedRoute from "./config/ProtectedRoute.jsx";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import PlaceDetailsPage from "./pages/PlaceDetailsPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import HeroPage from "./pages/HeroPage.jsx";
import { GoogleOAuthProvider } from "@react-oauth/google";
import UserProfilePage from "./pages/UserProfilePage.jsx";
import { createContext, useState, useEffect } from "react";
import UserSettingsPage from "./pages/UserSettingsPage.jsx";
import { CssBaseline, ThemeProvider } from "@mui/material";
import { lightTheme, darkTheme } from "./Theme.js";
import UserReviewsPage from "./pages/UserReviewsPage.jsx";
import { APIProvider } from "@vis.gl/react-google-maps";
import SearchResultsPage from "./pages/SearchResultsPage.jsx";
import UserFavouritesPage from "./pages/UserFavouritesPage.jsx";
import UserHistoryPage from "./pages/UserHistoryPage.jsx";

const queryClient = new QueryClient();
export const UserContext = createContext();
export const ThemeContext = createContext();

export default function App() {
  const [user, setUser] = useState(
    JSON.parse(localStorage.getItem("UserDetails"))
  );

  const [mode, setMode] = useState(localStorage.getItem("Theme") || "system");
  const [deviceTheme, setDeviceTheme] = useState(null);

  // detect system preference
  useEffect(() => {
    const mediaQuery = window.matchMedia("(prefers-color-scheme: dark)");
    setDeviceTheme(mediaQuery.matches ? "dark" : "light");

    const handler = (e) => setDeviceTheme(e.matches ? "dark" : "light");
    mediaQuery.addEventListener("change", handler);
    return () => mediaQuery.removeEventListener("change", handler);
  }, []);

  const resolvedMode = mode === "system" ? deviceTheme : mode;
  const appliedTheme = resolvedMode === "dark" ? darkTheme : lightTheme;
  localStorage.setItem("Theme", resolvedMode);

  return (
    <ThemeContext.Provider value={{ mode, setMode, deviceTheme }}>
      <ThemeProvider theme={appliedTheme}>
        <CssBaseline />
        <APIProvider
          apiKey={"AIzaSyBqIg8Y57Qs4nh9cTdRlY5VEDjgOqLOHhc"}
          onLoad={() => console.log("Maps API has loaded.")}
        >
          <GoogleOAuthProvider clientId="862654135638-ihdkg67htd7spqhla2qf3bk40ee0cdrk.apps.googleusercontent.com">
            <UserContext.Provider value={{ user, setUser }}>
              <QueryClientProvider client={queryClient}>
                <Routes>
                  <Route path="/login" element={<LoginPage />} />
                  <Route path="/register" element={<RegisterPage />} />
                  <Route path="/" element={<HeroPage />} />
                  <Route path="/*" element={<Navigate to="/" replace />} />
                  <Route element={<ProtectedRoute />}>
                    <Route element={<PageLayout />}>
                      {/* protected pages have an appbar/footer layout */}
                      <Route path="/home" element={<HomePage />} />
                      <Route path="/results" element={<SearchResultsPage />} />
                      <Route
                        path="/:username/favourites"
                        element={<UserFavouritesPage />}
                      />
                      <Route
                        path="/:username/history"
                        element={<UserHistoryPage />}
                      />
                      <Route
                        path="/:username/reviews"
                        element={<UserReviewsPage />}
                      />
                      <Route path="/place/:id" element={<PlaceDetailsPage />} />
                      <Route path="/profile" element={<UserProfilePage />} />
                      <Route path="/settings" element={<UserSettingsPage />} />
                    </Route>
                  </Route>
                </Routes>
              </QueryClientProvider>
            </UserContext.Provider>
          </GoogleOAuthProvider>
        </APIProvider>
      </ThemeProvider>
    </ThemeContext.Provider>
  );
}
