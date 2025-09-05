import GoogleIcon from "@mui/icons-material/Google";

import { Button } from "@mui/material";

export default function GoogleButton() {
  return (
    <Button
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
