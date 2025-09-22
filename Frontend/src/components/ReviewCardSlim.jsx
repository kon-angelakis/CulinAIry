import { useState } from "react";
import { Box, Divider, Paper, Typography, Link } from "@mui/material";

export default function ReviewCardSlim({ placeId, place, rating, text }) {
  const [expanded, setExpanded] = useState(false);
  const maxLength = 150; //character limit

  const isLong = text.length > maxLength;
  const displayText =
    !expanded && isLong ? text.slice(0, maxLength) + "..." : text;

  return (
    <Paper elevation={8}>
      <Box sx={{ p: 2 }}>
        <Box
          sx={{
            display: "flex",
            flexDirection: "row",
            justifyContent: "space-between",
            alignItems: "center",
          }}
        >
          <Typography
            sx={{
              maxWidth: "80%",
              overflowX: "hidden",
              textOverflow: "ellipsis",
            }}
            variant="h6"
            color="primary"
            textAlign={"start"}
          >
            <Link
              href={`/place/${placeId}`}
              underline="none"
              sx={{ "&:hover": { cursor: "pointer" } }}
            >
              {place}
            </Link>
          </Typography>
          <Typography color="secondary">{rating} / 5</Typography>
        </Box>
        <Divider sx={{ my: 2 }} />
        <Box
          sx={{
            display: "flex",
            flexDirection: "column",
          }}
        >
          <Typography textAlign={"justify"}>
            {displayText}
            {isLong && (
              <Link
                underline="none"
                color="secondary.light"
                component="button"
                onClick={() => setExpanded(!expanded)}
                sx={{ ml: 1 }}
              >
                {expanded ? "Read less" : "Read more"}
              </Link>
            )}
          </Typography>
        </Box>
      </Box>
    </Paper>
  );
}
