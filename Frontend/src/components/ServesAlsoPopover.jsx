import {
  Popover,
  IconButton,
  List,
  ListItem,
  ListItemText,
  Box,
  Typography,
} from "@mui/material";
import InfoRoundedIcon from "@mui/icons-material/InfoRounded";

import { useState } from "react";

export default function ServesAlsoPopover({ secondaryTypes }) {
  const [anchorEl, setAnchorEl] = useState(null);

  const handleClick = (event) => setAnchorEl(event.currentTarget);
  const handleClose = () => setAnchorEl(null);

  const open = Boolean(anchorEl);
  const id = open ? "secondary-types-popover" : undefined;

  return (
    <>
      <IconButton
        aria-describedby={id}
        size="small"
        onClick={handleClick}
        sx={{ ml: 1, color: "text.secondary" }}
      >
        <InfoRoundedIcon fontSize="small" />
      </IconButton>
      <Popover
        id={id}
        open={open}
        anchorEl={anchorEl}
        onClose={handleClose}
        anchorOrigin={{ vertical: "bottom", horizontal: "left" }}
      >
        <Box sx={{ p: 2, maxWidth: 220 }}>
          <Typography variant="subtitle2" gutterBottom>
            Serves Also
          </Typography>
          <List dense>
            {secondaryTypes.map((type, idx) => (
              <ListItem key={idx} sx={{ p: 0 }}>
                <ListItemText primary={type} />
              </ListItem>
            ))}
          </List>
        </Box>
      </Popover>
    </>
  );
}
