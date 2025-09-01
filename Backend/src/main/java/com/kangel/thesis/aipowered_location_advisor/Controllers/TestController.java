package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.LinkedHashSet;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.ApiResponse;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.AiManager;
import com.kangel.thesis.aipowered_location_advisor.Services.ImagekitService;
import com.kangel.thesis.aipowered_location_advisor.Services.PlaceService;
import com.kangel.thesis.aipowered_location_advisor.Services.SearchService;
import com.kangel.thesis.aipowered_location_advisor.Services.UserService;
import com.kangel.thesis.aipowered_location_advisor.Services.Authentication.Auth.AuthService;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.EmailFactory;
import com.kangel.thesis.aipowered_location_advisor.Services.Messaging.Email.SpringEmailService;

import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserService userService;
    private final SearchService searchService;
    private final ImagekitService imagekitService;
    private final AuthService authService;
    private final EmailFactory mEmailFactory;
    private final SpringEmailService mEmailService;
    private final PlaceService placeService;
    private final AiManager openAiService;

    public TestController(SearchService searchService, ImagekitService imagekitService, AuthService authService,
            SpringEmailService mEmailService, EmailFactory mEmailFactory, PlaceService placeService,
            AiManager openAiService, UserService userService) {
        this.userService = userService;
        this.searchService = searchService;
        this.imagekitService = imagekitService;
        this.authService = authService;
        this.mEmailFactory = mEmailFactory;
        this.mEmailService = mEmailService;
        this.placeService = placeService;
        this.openAiService = openAiService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> PlaceSearch(@Valid @RequestBody SearchRequest request)
            throws JsonProcessingException, InterruptedException {
        ApiResponse<LinkedHashSet<PlaceDTO>> response = searchService.SearchPlaces(request);
        return (new ResponseEntity<>(response, HttpStatus.OK));
    }

    @PostMapping("/imagekit")
    public String postMethodName() throws InternalServerException, BadRequestException,
            UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        imagekitService.UploadImage(
                "https://lh3.googleusercontent.com/place-photos/AJnk2czROuQWAJtcc-KYJiS_Emjpy37O2lvSn0M9L78VjkRZnj5x8IcpL8PlxwYgAykQVWnFs92scIfTp4rHECTQCcxQ3E5q7IWWCELoSaBULThacdBEbMbyOlIOcjb1O0--oAJtrx88xwyq2K8AqBg=s4800-w1920-h1080",
                "ChIJaSIytstyZIgR9KXlpsw1rMw",
                1);

        return "entity";
    }

    @GetMapping("/place/{placeId}")
    public ResponseEntity<?> FindPlace(@PathVariable String placeId) {
        ApiResponse<Place> response = placeService.FindPlace(placeId);
        return ResponseEntity.ok(response);

    }

}
