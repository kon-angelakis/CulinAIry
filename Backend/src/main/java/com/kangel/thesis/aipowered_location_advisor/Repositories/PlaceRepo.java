package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;

@Repository
public interface PlaceRepo extends MongoRepository<Place, String> {

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?1, ?2] }, " +
                                        "distanceField: 'distance', " +
                                        "spherical: true, " +
                                        "query: { _id: { $in: ?0 } } } }",
        })
        public List<Place> findByIdIn(List<String> ids, double lon, double lat);

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?1, ?2] }, " +
                                        "distanceField: 'distance', " +
                                        "maxDistance: 5000, " +
                                        "spherical: true, " +
                                        "query: { _id: { $in: ?0 } } } }",
        })
        public List<Place> findByIdInNearby(List<String> ids, double lon, double lat);

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                                        "distanceField: 'distance', " +
                                        "maxDistance: ?2, " +
                                        "spherical: true " +
                                        "} }",
                        "{ $match: { $expr: { $setIsSubset: [?3, '$secondaryTypes'] } } }",
                        "{ $sort: { dist: 1 } }",
                        "{ $limit: 50 }"
        })
        public List<Place> findPlacesDemandingNearby(double lon, double lat, int maxDist, List<String> types);

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                                        "distanceField: 'distance', " +
                                        "maxDistance: ?2, " +
                                        "spherical: true " +
                                        "} }",
                        "{ $addFields: { " +
                                        "matchCount: { $size: { $setIntersection: ['$secondaryTypes', ?3] } } " +
                                        "} }",
                        "{ $match: { matchCount: { $gt: 0 } } }",
                        "{ $sort: { matchCount: -1 } }",
                        "{ $limit: 50 }"
        })
        public List<Place> findPlacesInclusiveNearby(double lon, double lat, int maxDist, List<String> types);

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                                        "distanceField: 'distance', " +
                                        "maxDistance: 5000, " +
                                        "spherical: true " +
                                        "} }",
                        "{ $match: { inappRating: { $gt: 0 } } }",
                        "{ $sort: { inappRating: -1 } }",
                        "{ $limit: 9 }"
        })
        public List<Place> findTopPlacesNearby(double lon, double lat);

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "   near: { type: 'Point', coordinates: [?0, ?1] }, " +
                                        "   distanceField: 'distance', " +
                                        "   maxDistance: 5000, " +
                                        "   spherical: true " +
                                        "} }",
                        "{ $match: { " +
                                        "   _id: { $ne: ?4 }, " + // Exclude original
                                        "   primaryType: ?2, " +
                                        "   secondaryTypes: { $in: ?3 } " +
                                        "} }",
                        "{ $sample: { size: 10 } }"
        })
        public List<Place> findSimilarPlacesNearby(double lon, double lat, String primaryType,
                        List<String> secondaryTypes,
                        String placeId);

}
