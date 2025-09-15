package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.Date;

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

    public Result UploadImageString(String imageUri, String path, String subfolderId, String savedName)
            throws InternalServerException,
            BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        FileCreateRequest fileCreateRequest = new FileCreateRequest(imageUri, String.format("%s.jpg", savedName));
        fileCreateRequest.setFolder(String.format("/%s/%s/", path, subfolderId));
        fileCreateRequest.useUniqueFileName = false;
        Result result = ImageKit.getInstance().upload(fileCreateRequest);

        return result;
    }

    public Result UploadImageBytes(byte[] imageUri, String path, String subfolderId, String savedName)
            throws InternalServerException,
            BadRequestException, UnknownException, ForbiddenException, TooManyRequestsException, UnauthorizedException {
        FileCreateRequest fileCreateRequest = new FileCreateRequest(imageUri, String.format("%s.jpg", savedName));
        fileCreateRequest.setFolder(String.format("/%s/%s/", path, subfolderId));
        fileCreateRequest.useUniqueFileName = false;
        Result result = ImageKit.getInstance().upload(fileCreateRequest);

        return result;
    }

    public String RequestImage(String path, String subfolderId, String savedName) {
        return String.format("%s/%s/%s/%s.jpg", urlString, path, subfolderId, savedName);
    }

    // For cache overriding
    public String RequestImage(String path, String subfolderId, String savedName, Result result) {
        Date updatedAt = result.getUpdatedAt();
        long timestamp = updatedAt != null ? updatedAt.getTime() : System.currentTimeMillis(); // result might not have
                                                                                               // populated yet
        return String.format("%s/%s/%s/%s.jpg?updatedAt=%d", urlString, path, subfolderId, savedName, timestamp);
    }

}
