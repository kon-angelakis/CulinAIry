package com.kangel.thesis.aipowered_location_advisor.Services;

import org.springframework.stereotype.Service;

import io.github.cdimascio.dotenv.Dotenv;
import io.imagekit.sdk.ImageKit;
import io.imagekit.sdk.config.Configuration;
import io.imagekit.sdk.exceptions.BadRequestException;
import io.imagekit.sdk.exceptions.ForbiddenException;
import io.imagekit.sdk.exceptions.InternalServerException;
import io.imagekit.sdk.exceptions.TooManyRequestsException;
import io.imagekit.sdk.exceptions.UnauthorizedException;
import io.imagekit.sdk.exceptions.UnknownException;
import io.imagekit.sdk.models.FileCreateRequest;
import io.imagekit.sdk.models.results.Result;

@Service
public class ImagekitService {

    private final String publicKey, privateKey, urlString;

    public ImagekitService(Dotenv env) {
        this.publicKey = env.get("IMAGEKIT_PUBLIC");
        this.privateKey = env.get("IMAGEKIT_PRIVATE");
        this.urlString = env.get("IMAGEKIT_URL");

        ImageKit imageKit = ImageKit.getInstance();
        Configuration config = new Configuration(publicKey, privateKey, urlString);
        imageKit.setConfig(config);
    }

    // The path is gonna be like /places/placeID/0-2 (Reason to not use photoID
    // instead is google changes up the names each request so we end up with
    // multiple duplicate images, this way we only keep 3 at all times and overwrite
    // them if needed)
    public Result UploadImage(String googlePhotoUri, String placeId, int photoNum) throws InternalServerException,
            BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        FileCreateRequest fileCreateRequest = new FileCreateRequest(googlePhotoUri, String.format("%d.jpg", photoNum));
        fileCreateRequest.setFolder(String.format("/places/%s/", placeId));
        fileCreateRequest.useUniqueFileName = false;
        Result result = ImageKit.getInstance().upload(fileCreateRequest);

        return result;
    }

    public String RequestImage(String placeId, int photoNum) {
        return String.format("%s/places/%s/%d.jpg", urlString, placeId, photoNum);
    }

}
