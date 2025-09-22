import Footer from "rc-footer";
import { Typography } from "@mui/material";
import "rc-footer/assets/index.css"; // import 'rc-footer/asssets/index.less';
import { ThemeContext } from "../App.jsx";
import { useContext } from "react";

export default function CulinairyFooter() {
  const { mode, setMode, deviceTheme } = useContext(ThemeContext);
  return (
    <Footer
      bottom={
        <Typography variant="subtitle1">
          Konstantinos Angelakis &copy; 2024-{new Date().getFullYear()}{" "}
          <b>Culinairy</b>
        </Typography>
      }
      backgroundColor="transparent"
      theme={mode == "light" ? "light" : "dark"}
    />
  );
}
