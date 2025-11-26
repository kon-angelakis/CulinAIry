package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.Location;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PaginationRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SimilarPlacesRequest;

@Service
public class RecommendationsService {

        private final PlaceService placeService;
        private final UserService userService;
        private final UserInteractionService interactionService;

        public RecommendationsService(PlaceService placeService, UserService userService,
                        UserInteractionService interactionService) {
                this.placeService = placeService;
                this.userService = userService;
                this.interactionService = interactionService;
        }

        public ApiResponse<LinkedHashSet<PlaceDTO>> FindBestPlaces(Location request) {
                Set<Place> topPlaces = new HashSet<>(
                                placeService.FindTopPlaces(request.longitude(),
                                                request.latitude()));
                if (topPlaces.size() > 0)
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(true,
                                        String.format("Showing the %d top places", topPlaces.size()),
                                        topPlaces.stream()
                                                        .map(Place::ToPlaceDTO)
                                                        .collect(Collectors.toCollection(LinkedHashSet::new)));
                else
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(false,
                                        "Couldn't find the best places at this time", null);

        }

        public ApiResponse<LinkedHashSet<PlaceDTO>> FindTrendingPlaces(Location request) {
                // Get most clicked placeids
                List<String> mostClickedPlaces = interactionService.FindMostClickedPlaceIds();
                // Find their place data unordered but nearby
                List<Place> trendingPlaces = placeService.FindAllPlacesByIdNearby(mostClickedPlaces,
                                request.longitude(),
                                request.latitude());

                // Order and sort the top 10 ranking nearby places
                Map<String, Integer> clickOrder = new HashMap<>();
                for (int i = 0; i < mostClickedPlaces.size(); i++) {
                        clickOrder.put(mostClickedPlaces.get(i), i);
                }
                List<Place> orderedTrending = trendingPlaces.stream()
                                .sorted(Comparator
                                                .comparingInt((Place p) -> clickOrder.getOrDefault(p.getId(),
                                                                Integer.MAX_VALUE))
                                                .thenComparingDouble(Place::getDistance))
                                .limit(10)
                                .toList();

                if (orderedTrending.size() > 0)
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(true,
                                        String.format("Showing %d trending places in the past 24 hours",
                                                        orderedTrending.size()),
                                        orderedTrending.stream()
                                                        .map(Place::ToPlaceDTO)
                                                        .collect(Collectors.toCollection(LinkedHashSet::new)));
                else
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(false,
                                        "Couldn't find the trending places at this time", null);

        }

        public ApiResponse<LinkedHashSet<PlaceDTO>> FindSimilarPlaces(SimilarPlacesRequest request) {
                Set<Place> similarPlaces = new HashSet<>(
                                placeService.FindSimilarPlaces(request.location().longitude(),
                                                request.location().latitude(), request.primaryType(),
                                                request.secondaryTypes(), request.originalId()));
                if (similarPlaces.size() > 0)
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(true,
                                        String.format("Showing %d similar places", similarPlaces.size()),
                                        similarPlaces.stream()
                                                        .map(Place::ToPlaceDTO)
                                                        .collect(Collectors.toCollection(LinkedHashSet::new)));
                else
                        return new ApiResponse<LinkedHashSet<PlaceDTO>>(false,
                                        "Couldn't find the trending places at this time", null);

        }

        public ApiResponse<List<PlaceDTO>> FindCuratedPlaces(Location request) {

                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();
                User user = userService.GetUser(userDetails.getUsername());
                List<String> placesTypes = userService.FindCuratedPlacesById(userDetails).data();
                List<PlaceDTO> places = placeService.FindPlaceInclusive(
                                request.longitude(),
                                request.latitude(),
                                10000,
                                placesTypes, new PaginationRequest(0, 10), "relevance", -1)
                                .stream()
                                .map(Place::ToPlaceDTO)
                                .limit(10)
                                .collect(Collectors.toCollection(ArrayList::new));
                // Gather first some data about the user and then display recommendations
                int favSize = interactionService.FindAllByUserIdAndType(user.getId(), InteractionType.FAVOURITES)
                                .size();
                if (places.size() == 0 || favSize < 6) {
                        return new ApiResponse<List<PlaceDTO>>(false, "No recommendations... for now",
                                        null);
                }
                return new ApiResponse<List<PlaceDTO>>(true,
                                String.format("Here are %d curated places we think you’ll love", places.size()),
                                places);

        }

        // We make the assumption that the user has visited a place if he has left a
        // review
        public ApiResponse<List<PlaceDTO>> FindPastPlaces(Location request) {
                UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                                .getPrincipal();
                List<Review> userReviews = userService.FindAllReviews(userDetails).data();
                List<String> placeIds = userReviews.stream()
                                .map(Review::getPlaceId)
                                .collect(Collectors.toList());
                List<Place> pastPlacesRaw = userService
                                .FindPastPlaces(request.longitude(), request.latitude(), placeIds)
                                .data();

                if (pastPlacesRaw.size() == 0) {
                        return new ApiResponse<List<PlaceDTO>>(false, "No data found",
                                        null);
                }
                // Randomize and select 10
                // Collections.shuffle(pastPlacesRaw);
                List<PlaceDTO> pastPlaces = pastPlacesRaw.stream().map(Place::ToPlaceDTO).limit(10)
                                .collect(Collectors.toList());
                return new ApiResponse<List<PlaceDTO>>(true,
                                "Revisit these places",
                                pastPlaces);
        }
}
