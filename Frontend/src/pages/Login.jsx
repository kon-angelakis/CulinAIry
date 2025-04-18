import { useEffect } from "react";
import LoginForm from "../components/Forms/LoginForm";
import Footer from "../components/PageInfo/Footer";
import authAxios from "../config/axiosConfig";
import { useNavigate } from "react-router-dom";

function Login() {
  //If the user is logined skip the login screen
  const navigate = useNavigate();
  useEffect(() => {
    authAxios
      .get("/api/auth/authenticated")
      .then(() => navigate("/home"))
      .catch(() => navigate("/login"));
  }, [navigate]);

  return (
    <>
      <LoginForm />
      <Footer />
    </>
  );
}

export default Login;
