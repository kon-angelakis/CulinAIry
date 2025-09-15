package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;

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
    private String thumbnail;

    private String name, primaryType, phone, address, website, directionsUri;
    private double rating;
    private int totalRatings;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private GeoJsonPoint location;

    private List<String> secondaryTypes;
    private List<String> schedule;
    private List<String> photos;
    private List<Review> reviews;

    private LocalDateTime dateUpdated;

    public PlaceDTO ToPlaceDTO() {
        return new PlaceDTO(id, thumbnail, name, primaryType);
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
