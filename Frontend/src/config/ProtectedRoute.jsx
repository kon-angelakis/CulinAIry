import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import authAxios from "./axiosConfig";

const ProtectedRoute = ({ children }) => {
  const [isAuthenticated, setIsAuthenticated] = useState(null);
  const navigate = useNavigate();

  useEffect(() => {
    authAxios
      .get("/api/auth/authenticated")
      .then((response) => {
        console.log("User authenticated:", response.data);
        setIsAuthenticated(true);
      })
      .catch((error) => {
        console.error("Not authenticated:", error);
        setIsAuthenticated(false);
      });
  }, []);

  if (isAuthenticated === null) {
    return <p>Loading...</p>; //Replace with a navigation page in the future
  }

  if (isAuthenticated === false) {
    navigate("/login");
    return null;
  }

  return children; //Show the protected page if auth is granted
};

export default ProtectedRoute;
