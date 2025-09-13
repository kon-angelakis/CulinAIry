package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.List;
import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Config.Security.Auth.UserPrincipal;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.PlaceListType;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Repositories.UserRepo;

@Service
public class UserService implements UserDetailsService {

    public static final int MAX_HISTORY = 16;

    private final UserRepo userRepo;
    private final PlaceService placeService;

    public UserService(UserRepo userRepo, PlaceService placeService) {
        this.userRepo = userRepo;
        this.placeService = placeService;
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

}
