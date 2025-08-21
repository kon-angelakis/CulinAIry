package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;

@Repository
public interface PlacesRepo extends MongoRepository<Place, String> {

    @Aggregation(pipeline = {
            "{ $geoNear: { " +
                    "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                    "distanceField: 'dist', " +
                    "maxDistance: ?2, " +
                    "spherical: true " +
                    "} }",
            "{ $match: { $expr: { $setIsSubset: [?3, '$secondaryTypes'] } } }",
            "{ $sort: { dist: 1 } }",
            "{ $limit: 20 }"
    })
    public List<Place> findNearbyPlacesDemanding(double lon, double lat, int maxDist, List<String> types);

    @Aggregation(pipeline = {
            "{ $geoNear: { " +
                    "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                    "distanceField: 'dist', " +
                    "maxDistance: ?2, " +
                    "spherical: true " +
                    "} }",
            "{ $addFields: { " +
                    "matchCount: { $size: { $setIntersection: ['$secondaryTypes', ?3] } } " +
                    "} }",
            "{ $match: { matchCount: { $gt: 0 } } }",
            "{ $sort: { matchCount: -1 } }",
            "{ $limit: 20 }"
    })
    public List<Place> findNearbyPlacesInclusive(double lon, double lat, int maxDist, List<String> types);

}
