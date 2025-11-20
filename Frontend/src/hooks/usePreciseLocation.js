// Hook that manages and switches between precise and approximate geolocation
import { useState, useEffect } from "react";

export default function usePreciseLocation() {
  const [precise, setPrecise] = useState(false);
  const [locationGranted, setLocationGranted] = useState(false);
  const [location, setLocation] = useState(
    JSON.parse(localStorage.getItem("UserGeolocationApproximateLocation"))
  );

  const requestPreciseLocation = () => {
    navigator.geolocation.getCurrentPosition(
      (position) => {
        setLocationGranted(true);
        const coords = {
          longitude: position.coords.longitude,
          latitude: position.coords.latitude,
        };
        setLocation({
          longitude: coords.longitude,
          latitude: coords.latitude,
        });
        localStorage.setItem(
          "UserGeolocationApproximateLocation",
          JSON.stringify(coords)
        );
      },
      () => {
        setLocationGranted(false);
      },
      {
        enableHighAccuracy: true,
      }
    );
  };

  const handlePreciseToggle = (event) => {
    const val = event.target.checked;
    setPrecise(val);
    if (val) {
      requestPreciseLocation();
    } else {
      setLocation(
        JSON.parse(localStorage.getItem("UserGeolocationApproximateLocation"))
      );
    }
  };

  return {
    precise,
    locationGranted,
    location,
    handlePreciseToggle,
  };
}
