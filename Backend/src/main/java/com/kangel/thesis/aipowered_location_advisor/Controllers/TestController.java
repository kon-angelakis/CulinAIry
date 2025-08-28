package com.kangel.thesis.aipowered_location_advisor.Controllers;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kangel.thesis.aipowered_location_advisor.Models.EmailMessage;
import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.User;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.EmailTemplate;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.SearchRequest;
import com.kangel.thesis.aipowered_location_advisor.Services.ImagekitService;
import com.kangel.thesis.aipowered_location_advisor.Services.SearchService;
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
@RequestMapping("/v1/api/test")
public class TestController {

    private final SearchService searchService;
    private final ImagekitService imagekitService;
    private final AuthService authService;
    private final EmailFactory mEmailFactory;
    private final SpringEmailService mEmailService;

    public TestController(SearchService searchService, ImagekitService imagekitService, AuthService authService,
            SpringEmailService mEmailService, EmailFactory mEmailFactory) {
        this.searchService = searchService;
        this.imagekitService = imagekitService;
        this.authService = authService;
        this.mEmailFactory = mEmailFactory;
        this.mEmailService = mEmailService;
    }

    @PostMapping("/search")
    public ResponseEntity<?> PlaceSearch(@Valid @RequestBody SearchRequest request)
            throws JsonProcessingException, InterruptedException {
        List<Place> results = searchService.SearchPlaces(request);
        return (new ResponseEntity<>(results, HttpStatus.OK));
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
}
