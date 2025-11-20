package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.WeightedRating;

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
    private boolean isDetailed;
    private boolean isGoogleReviewed;
    private String thumbnail;

    private String name, primaryType, primaryTypeRaw, phone, address, website, directionsUri;
    private Double rating, inappRating;
    private Integer totalRatings, inappTotalRatings;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;
    @Field("distance")
    private Double distance; // Mongo returned distance variable

    private List<String> secondaryTypes;
    private List<String> schedule;
    private List<String> photos;
    private Set<Review> reviews = new HashSet<>();

    private LocalDateTime dateUpdated;

    public PlaceDTO ToPlaceDTO() {
        return new PlaceDTO(id, thumbnail, name, primaryType, location,
                new WeightedRating(
                        (rating * totalRatings + inappRating * inappTotalRatings)
                                / (totalRatings + inappTotalRatings + 0.00001),
                        totalRatings + inappTotalRatings),
                distance);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Place place = (Place) o;
        return id != null && id.equals(place.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
