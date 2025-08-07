package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Document(collection = "Places")
@Data
@AllArgsConstructor
@Getter
public class Place {

    @Id
    private String id;

    private String name, primaryType, phone, address, website, directionsUri;
    private double rating;
    private int totalRatings;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private List<String> secondaryTypes = List.of();
    private List<String> schedule = List.of();
    private List<String> photos = List.of();
    private List<Review> reviews = List.of();

    @CreatedDate
    private final LocalDateTime dateUpdated = LocalDateTime.now();
}
