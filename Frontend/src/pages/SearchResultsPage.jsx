import { Box, Grid, Typography } from "@mui/material";
import SkeletonPlaceCard from "../components/SkeletonPlaceCard";

export default function SearchResultsPage() {
  return (
    <Box>
      <Typography variant="h4" sx={{ mb: 4 }}>
        Found X results
      </Typography>

      <Grid container spacing={2} justifyContent="space-evenly">
        {Array.from({ length: 11 }).map((_, idx) => (
          <Grid
            item
            key={idx}
            sx={{
              flex: "1 1 275px", //container dynamic shrinking
              maxWidth: 400,
              width: { xs: "275px" }, //on mobiles start with the smallest possible size
            }}
          >
            <SkeletonPlaceCard />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
}
