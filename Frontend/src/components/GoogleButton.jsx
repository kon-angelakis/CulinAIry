import GoogleIcon from "@mui/icons-material/Google";
import { useGoogleLogin } from "@react-oauth/google";
import { Button } from "@mui/material";
import { useNavigate } from "react-router";
import apiAxios from "../config/apiAxiosConfig";

export default function GoogleButton({ showAlert }) {
  const navigate = useNavigate();

  const login = useGoogleLogin({
    onSuccess: async (codeResponse) => {
      console.log("Authorization Code:", codeResponse.code);

      try {
        const response = await apiAxios.post("/oauth2/google", {
          code: codeResponse.code,
        });
        showAlert({
          severity: response.data.success ? "success" : "error",
          message: response.data.message,
        });
        localStorage.setItem("AuthToken", response.data.data.jwtToken);
        localStorage.setItem(
          "UserDetails",
          JSON.stringify(response.data.data.userDetails)
        );
        navigate("/home");
      } catch (error) {
        console.error("Google login failed:", error);
      }
    },
    flow: "auth-code",
  });

  return (
    <Button
      onClick={login}
      fullWidth
      variant="outlined"
      color="secondary"
      startIcon={<GoogleIcon />}
      sx={{
        mb: 2,
        py: 1.2,
        textTransform: "none",
      }}
    >
      Sign in with Google
    </Button>
  );
}
