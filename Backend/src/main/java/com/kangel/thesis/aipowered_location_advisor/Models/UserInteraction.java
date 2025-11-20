package com.kangel.thesis.aipowered_location_advisor.Models;

import java.time.LocalDateTime;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Document(collection = "Interactions")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInteraction {

    @Id
    private ObjectId id;
    private ObjectId userId;
    private String placeId;
    private InteractionType type; // type==CLICKED have a lifetime of 24hours then mongo deletes

    @CreatedDate
    private final LocalDateTime dateInteracted = LocalDateTime.now();

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof UserInteraction))
            return false;
        UserInteraction interaction = (UserInteraction) o;
        return id != null && id.equals(interaction.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
