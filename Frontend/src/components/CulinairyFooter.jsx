import Footer from "rc-footer";
import { Typography } from "@mui/material";
import "rc-footer/assets/index.css"; // import 'rc-footer/asssets/index.less';

export default function CulinairyFooter() {
  return (
    <Footer
      bottom={
        <Typography variant="subtitle1">
          Konstantinos Angelakis &copy; 2024-{new Date().getFullYear()}{" "}
          <b>Culinairy</b>
        </Typography>
      }
      backgroundColor="transparent"
      theme="light"
    />
  );
}
