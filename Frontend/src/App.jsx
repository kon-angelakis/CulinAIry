import { Route, Routes } from "react-router";
import "./App.css";
import PageLayout from "./config/PageLayout.jsx";
import ProtectedRoute from "./config/ProtectedRoute.jsx";
import HomePage from "./pages/HomePage.jsx";
import LoginPage from "./pages/LoginPage.jsx";
import PlaceDetailsPage from "./pages/PlaceDetailsPage.jsx";
import RegisterPage from "./pages/RegisterPage.jsx";
import SearchResultsPage from "./pages/SearchResultsPage.jsx";

export default function App() {
  return (
    <Routes>
      <Route path="/login" element={<LoginPage />} />
      <Route path="/register" element={<RegisterPage />} />
      <Route path="/*" element={<LoginPage />} />
      <Route element={<ProtectedRoute />}>
        <Route element={<PageLayout />}>
          {/* protected pages have an appbar/footer layout */}
          <Route path="/home" element={<HomePage />} />
          <Route path="/results" element={<SearchResultsPage />} />
          <Route path="/details" element={<PlaceDetailsPage />} />
        </Route>
      </Route>
    </Routes>
  );
}
