package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.Base64;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.PlaceListType;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ChangeDataRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ChangePfpRequest;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.LoginResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;

import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<?> UserDetails() {
        ApiResponse<User> response = userService.GetUserDetails();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/places") // Returns the fav/recently viewed place dtos to construct cards in the
                           // frontend, being less expensive
                           // than the full place object
    public ResponseEntity<?> Places(@RequestParam PlaceListType type) {
        ApiResponse<List<PlaceDTO>> response = userService.GetUserPlaces(type);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/favourites/{placeId}")
    public ResponseEntity<?> IsFavourite(@PathVariable String placeId, @RequestParam String username) {
        ApiResponse<Boolean> response = userService.IsFavourite(placeId, username);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/favourites/{placeId}")
    public ResponseEntity<?> AddFavourite(@PathVariable String placeId) {
        ApiResponse<Void> response = userService.ToggleFavourite(placeId, true);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/favourites/{placeId}")
    public ResponseEntity<?> RemoveFavourite(@PathVariable String placeId) {
        ApiResponse<Void> response = userService.ToggleFavourite(placeId, false);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/history/{placeId}")
    public ResponseEntity<?> AddRecentlyViewed(@PathVariable String placeId) {
        ApiResponse<Void> response = userService.AddRecentlyViewed(placeId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/pfp")
    public ResponseEntity<?> ChangeProfilePicture(@RequestBody ChangePfpRequest request) throws InternalServerException,
            BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        // base64 -> bytes
        byte[] bytes = Base64.getDecoder().decode(request.imageFileB64());

        ApiResponse<LoginResponse> response = userService.ChangePfp(bytes);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/credentials")
    public ResponseEntity<?> ChangeUserData(@Valid @RequestBody ChangeDataRequest request) {
        ApiResponse<LoginResponse> response = userService.ChangeData(request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<?> DeleteUser() {
        ApiResponse<Void> response = userService.DeleteUser();

        return ResponseEntity.ok(response);
    }

}
