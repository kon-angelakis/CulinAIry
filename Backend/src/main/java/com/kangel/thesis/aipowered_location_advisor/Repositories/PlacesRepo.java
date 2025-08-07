package com.kangel.thesis.aipowered_location_advisor.Repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.kangel.thesis.aipowered_location_advisor.Models.Place;

@Repository
public interface PlacesRepo extends MongoRepository<Place, String> {

    @Query("""
    {
    'location': {
        $near: {
        $geometry: {
            type: 'Point',
            coordinates: [?0, ?1]
        },
        $maxDistance: ?2
        }
    },
    'type': { $in: ?3 }
    }
    """)
    List<Place> findNearbyPlacesWithAnyType(double lon, double lat, double maxDist, List<String> types);


}
