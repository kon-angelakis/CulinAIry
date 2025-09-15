package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;
import java.util.Set;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;
import com.kangel.thesis.aipowered_location_advisor.Models.Records.PlaceDTO;

@Repository
public interface PlaceRepo extends MongoRepository<Place, String> {

        @Aggregation(pipeline = {
                        "{ $geoNear: { " +
                                        "near: { type: 'Point', coordinates: [?0, ?1] }, " +
                                        "distanceField: 'dist', " +
                                        "maxDistance: ?2, " +
                                        "spherical: true " +
                                        "} }",
                        "{ $match: { $expr: { $setIsSubset: [?3, '$secondaryTypes'] } } }",
                        "{ $sort: { dist: 1 } }",
                        "{ $limit: 50 }"
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
                        "{ $limit: 50 }"
        })
        public List<Place> findNearbyPlacesInclusive(double lon, double lat, int maxDist, List<String> types);

        @Query(value = "{ '_id': { $in: ?0 } }", fields = "{ 'id': 1, 'thumbnail': 1, 'name': 1, 'primaryType': 1}")
        public List<PlaceDTO> findAllPlaceDTOSById(@Param("ids") Iterable<String> ids);
}
