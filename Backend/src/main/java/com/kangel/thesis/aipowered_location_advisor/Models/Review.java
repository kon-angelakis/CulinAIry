package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Document(collection = "Reviews")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Review {

    @Id
    private ObjectId id;
    private ObjectId authorId;
    private String placeId;
    private String placeName;
    private int rating;
    private String text;

    @CreatedDate
    private final LocalDateTime dateCreated = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Review))
            return false;
        Review review = (Review) o;
        return id != null && id.equals(review.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
