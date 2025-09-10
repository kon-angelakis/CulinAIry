import { Navigate, Route, Routes } from "react-router";
import "./App.css";
import PageLayout from "./config/PageLayout.jsx";
import ProtectedRoute from "./config/ProtectedRoute.jsx";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import PlaceDetailsPage from "./pages/PlaceDetailsPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import SearchResultsPage from "./pages/SearchResultsPage.jsx";
import { QueryClient, QueryClientProvider } from "@tanstack/react-query";
import HeroPage from "./pages/HeroPage.jsx";

const queryClient = new QueryClient();

export default function App() {
  return (
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
            <Route path="/place/:id" element={<PlaceDetailsPage />} />
          </Route>
        </Route>
      </Routes>
    </QueryClientProvider>
  );
}
