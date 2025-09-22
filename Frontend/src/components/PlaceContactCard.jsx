import { Paper, Box, Link, Typography, Divider, Skeleton } from "@mui/material";
import ServesAlsoPopover from "../components/ServesAlsoPopover";
import ImportContactsRoundedIcon from "@mui/icons-material/ImportContactsRounded";
import CategoryRoundedIcon from "@mui/icons-material/CategoryRounded";
import FmdGoodRoundedIcon from "@mui/icons-material/FmdGoodRounded";
import LanguageRoundedIcon from "@mui/icons-material/LanguageRounded";
import LocalPhoneRoundedIcon from "@mui/icons-material/LocalPhoneRounded";

export default function PlaceContactCard({
  type,
  secondaryTypes,
  website,
  phone,
  address,
  directionsUri,
  isLoading = false,
}) {
  function getBaseUrl(url) {
    try {
      const parsed = new URL(url);
      return `${parsed.protocol}//${parsed.hostname}`;
    } catch {
      return url;
    }
  }

  return (
    <Paper sx={{ p: 2 }}>
      <Box
        sx={{
          display: "flex",
          alignItems: "center",
          justifyContent: "center",
        }}
      >
        <ImportContactsRoundedIcon sx={{ mr: 1 }} color="primary" />
        <Typography variant="h6">Contact Info</Typography>
      </Box>
      <Divider sx={{ my: 1 }} />

      <Box
        sx={{
          p: 1,
          display: "flex",
          flexDirection: "column",
          justifyContent: "space-between",
          alignItems: "start",
        }}
        gap={4}
      >
        {/* Category */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <CategoryRoundedIcon sx={{ mr: 1 }} color="primary" />
          {isLoading ? (
            <Skeleton width={100} />
          ) : (
            <Typography variant="body1" textAlign={"start"}>
              {type}
            </Typography>
          )}
          {!isLoading && secondaryTypes?.length > 0 && (
            <ServesAlsoPopover secondaryTypes={secondaryTypes} />
          )}
        </Box>

        {/* Website */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <LanguageRoundedIcon sx={{ mr: 1 }} color="primary" />
          {isLoading ? (
            <Skeleton width={120} />
          ) : website ? (
            <Link
              href={website}
              target="_blank"
              rel="noopener"
              underline="hover"
              color="secondary"
              textAlign={"start"}
            >
              {getBaseUrl(website)}
            </Link>
          ) : (
            "-"
          )}
        </Box>

        {/* Phone */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <LocalPhoneRoundedIcon sx={{ mr: 1 }} color="primary" />
          {isLoading ? (
            <Skeleton width={90} />
          ) : phone ? (
            <Link
              href={`tel:${phone}`}
              underline="hover"
              color="secondary"
              textAlign={"start"}
            >
              {phone}
            </Link>
          ) : (
            "-"
          )}
        </Box>

        {/* Address */}
        <Box sx={{ display: "flex", alignItems: "center" }}>
          <FmdGoodRoundedIcon sx={{ mr: 1 }} color="primary" />
          {isLoading ? (
            <Skeleton width={180} />
          ) : address ? (
            <Link
              href={directionsUri}
              target="_blank"
              rel="noopener"
              underline="hover"
              color="secondary"
              textAlign={"start"}
            >
              <Typography variant="body1">{address}</Typography>
            </Link>
          ) : (
            "-"
          )}
        </Box>
      </Box>
    </Paper>
  );
}
