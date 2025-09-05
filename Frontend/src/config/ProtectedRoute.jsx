import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router";
import authAxios from "./authAxiosConfig";

const ProtectedRoute = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);

  useEffect(() => {
    authAxios
      .get("/auth/authenticated")
      .then((response) => {
        console.log("User authenticated:", response.data);
        setIsAuthenticated(true);
      })
      .catch((error) => {
        console.error("Not authenticated:", error);
        setIsAuthenticated(false);
      });
  }, []);

  //filter out unauthenticated requests
  if (isAuthenticated === null) {
    return null;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  //Render auth content
  return <Outlet />;
};

export default ProtectedRoute;
