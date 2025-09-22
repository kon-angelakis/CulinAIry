import { Box } from "@mui/material";
import SearchResultsBox from "../components/SearchResultsBox";
import { useLocation } from "react-router";

// General router page for any result grid like favourites or a new search query
export default function ResultsPage() {
  const location = useLocation();
  const { searchEndpoint, axiosMethod, formData, text } = location.state || {};
  return (
    <Box>
      <SearchResultsBox
        endpoint={searchEndpoint}
        method={axiosMethod}
        formData={formData}
        text={text}
      />
    </Box>
  );
}
