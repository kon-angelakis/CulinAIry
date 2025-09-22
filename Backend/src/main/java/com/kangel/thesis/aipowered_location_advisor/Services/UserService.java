package com.kangel.thesis.aipowered_location_advisor.Services;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth.UserPrincipal;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Review;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.PlaceListType;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ChangeDataRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.CuratedSearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ReviewAdditionRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Repositories.UserRepo;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.JwtService;

import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import io.imagekit.sdk.models.results.Result;

@Service
public class UserService implements UserDetailsService {

    public static final int MAX_HISTORY = 16;

    private final UserRepo userRepo;
    private final PlaceService placeService;
    private final ImagekitService imagekitService;
    private final JwtService jwtService;
    private final ReviewService reviewService;

    public UserService(UserRepo userRepo, PlaceService placeService, ImagekitService imagekitService,
            JwtService jwtService, ReviewService reviewService) {
        this.userRepo = userRepo;
        this.placeService = placeService;
        this.imagekitService = imagekitService;
        this.jwtService = jwtService;
        this.reviewService = reviewService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User loginedUser = username.contains("@") ? userRepo.findByEmail(username).orElse(null)
                : userRepo.findByUsername(username).orElse(null);
        if (loginedUser == null)
            throw new UsernameNotFoundException("User not found");
        return new UserPrincipal(loginedUser);
    }

    public boolean UserExists(String user) { // If the user exists in general
        return user.contains("@") ? userRepo.findByEmail(user).isPresent()
                : userRepo.findByUsername(user).isPresent();
    }

    public User GetUser(String user) { // If the user exists do get the user
        return user.contains("@") ? userRepo.findByEmail(user).orElse(null)
                : userRepo.findByUsername(user).orElse(null);
    }

    public void DeleteUser(User user) {
        userRepo.delete(user);
    }

    public ApiResponse<User> GetUserDetails() { // Get user but in ApiResponse format
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = GetUser(username);

        return new ApiResponse<User>(true, "User retrieved", user);
    }

    public ApiResponse<Boolean> IsFavourite(String placeId, String username) {
        return new ApiResponse<Boolean>(userRepo.existsByUsernameAndFavouritesContains(username, placeId), "favourite?",
                userRepo.existsByUsernameAndFavouritesContains(username, placeId));
    }

    public ApiResponse<Void> ToggleFavourite(String placeId, boolean toggle) { // toggle decides to
                                                                               // append or remove from favs
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = GetUser(username);

        if (toggle) {
            user.getFavourites().add(placeId);
            SaveUser(user);
            return new ApiResponse<Void>(true, "Place added to favourites", null);
        } else {
            user.getFavourites().remove(placeId);
            SaveUser(user);
            return new ApiResponse<Void>(true, "Place removed from favourites", null);
        }

    }

    public ApiResponse<Void> AddRecentlyViewed(String placeId) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = GetUser(username);
        Set<String> recentlyViewed = user.getHistory();
        // If already in set remove and add at the top
        recentlyViewed.remove(placeId);
        recentlyViewed.add(placeId);
        // Remove the oldest element if size exceeds maximum allowed
        if (recentlyViewed.size() > MAX_HISTORY)
            recentlyViewed.remove(recentlyViewed.iterator().next());
        SaveUser(user);
        return new ApiResponse<Void>(true, "Updated recently viewed", null);
    }

    public ApiResponse<List<PlaceDTO>> GetUserPlaces(PlaceListType type) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = userDetails.getUsername();
        User user = GetUser(username);
        List<PlaceDTO> data;

        switch (type) {
            case FAVOURITES -> data = placeService.FindAllPlaceDTOSById(user.getFavourites());
            case RECENTLY_VIEWED -> data = placeService.FindAllPlaceDTOSById(user.getHistory());
            default -> throw new IllegalArgumentException("Invalid place list type");
        }
        return new ApiResponse<List<PlaceDTO>>(true, "Retrieved list of places", data);
    }

    public User SaveUser(User user) {
        if (user == null)
            return null;
        return userRepo.save(user);
    }

    public ApiResponse<LoginResponse> ChangePfp(byte[] imageFile) throws InternalServerException, BadRequestException,
            UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        try {

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            User user = GetUser(userDetails.getUsername());

            Result result = imagekitService.UploadImageBytes(imageFile,
                    String.format("users/%s", user.getId().toString()),
                    "avatar");
            user.setPfp(
                    imagekitService.RequestImage(String.format("users/%s", user.getId().toString()), "avatar", result));
            // Change profile picture in the db and return a new jwt
            user = SaveUser(user);
            String newJwt = jwtService.GenerateToken(user);
            return new ApiResponse<LoginResponse>(true, "Avatar changed",
                    new LoginResponse(user.ToUserDTO(), newJwt));
        } catch (Exception e) {
            return new ApiResponse<LoginResponse>(false, "Avatar change error", null);
        }
    }

    public ApiResponse<LoginResponse> ChangeData(ChangeDataRequest request) {
        try {

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            User user = GetUser(userDetails.getUsername());
            user.setUsername(request.username());
            user.setFirstName(request.firstName());
            user.setLastName(request.lastName());
            user = SaveUser(user);
            String newJwt = jwtService.GenerateToken(user);
            return new ApiResponse<LoginResponse>(true, "Data changed",
                    new LoginResponse(user.ToUserDTO(), newJwt));
        } catch (Exception e) {
            return new ApiResponse<LoginResponse>(false, "Data change error", null);
        }
    }

    public ApiResponse<Void> DeleteUser() {
        try {

            UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                    .getPrincipal();
            User user = GetUser(userDetails.getUsername());
            imagekitService.DeleteImage(String.format("/users/%s", user.getId().toString()));
            DeleteUser(user);
            return new ApiResponse<Void>(true, "User deleted",
                    null);
        } catch (Exception e) {
            return new ApiResponse<Void>(false, "User deletion error", null);
        }
    }

    public ApiResponse<List<PlaceDTO>> FindCuratedPlaces(CuratedSearchRequest request) {

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        List<String> placesTypes = userRepo.findCuratedPlacesByUsername(userDetails.getUsername());
        List<PlaceDTO> places = placeService.FindPlaceInclusive(
                request.location().longitude(),
                request.location().latitude(),
                request.radius(),
                placesTypes)
                .stream()
                .map(Place::ToPlaceDTO)
                .limit(5)
                .collect(Collectors.toCollection(ArrayList::new));
        if (places.size() == 0) {
            return new ApiResponse<List<PlaceDTO>>(false, "No recommendations... for now",
                    null);
        }
        return new ApiResponse<List<PlaceDTO>>(true, "Curated places retrieved",
                places);

    }

    public ApiResponse<Void> LeaveReview(ReviewAdditionRequest request) {
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal();
        User user = GetUser(userDetails.getUsername());
        if (reviewService.ExistsByAuthorIdAndPlaceId(user.getId(), request.placeId()))
            return new ApiResponse<Void>(false, "User already left review", null);
        Review review = new Review();
        review.setAuthorId(user.getId());
        review.setPlaceId(request.placeId());
        review.setPlaceName(request.placeName());
        review.setText(request.text());
        review.setRating(request.rating());
        review = reviewService.SaveReview(review);
        // Update place inapp rating and count
        Place place = placeService.FindPlace(request.placeId()).data();
        Double totalScore = place.getInappRating() * place.getInappTotalRatings() + request.rating();
        Integer totalRatings = place.getInappTotalRatings() + 1;
        BigDecimal newRating = BigDecimal.valueOf(totalScore)
                .divide(BigDecimal.valueOf(totalRatings), 1, RoundingMode.HALF_UP);
        place.setInappTotalRatings(totalRatings);
        place.setInappRating(newRating.doubleValue());
        placeService.SavePlace(place);

        return new ApiResponse<Void>(true, "Review submitted", null);

    }

    public ApiResponse<Review> FindMyReview(UserDetails userDetails, String placeId) {
        ObjectId userId = GetUser(userDetails.getUsername()).getId();
        Review review = reviewService.FindMyReview(userId, placeId);
        if (review == null)
            return new ApiResponse<Review>(false, "No Review Found", null);
        return new ApiResponse<Review>(true, "Review Found", reviewService.FindMyReview(userId, placeId));
    }

    public ApiResponse<Page<Review>> FindAllReviews(UserDetails userDetails, int page, int size) {
        ObjectId userId = GetUser(userDetails.getUsername()).getId();
        return new ApiResponse<Page<Review>>(true, "Page retrieved", reviewService.FindByAuthorId(userId, page, size));
    }

}
