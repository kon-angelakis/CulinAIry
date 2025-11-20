package com.kangel.thesis.aipowered_location_advisor.Services;

import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.kangel.thesis.aipowered_location_advisor.Models.UserInteraction;
import com.kangel.thesis.aipowered_location_advisor.Models.Enums.InteractionType;
import com.kangel.thesis.aipowered_location_advisor.Repositories.UserInteractionRepo;

@Service
public class UserInteractionService {

    private final UserInteractionRepo interactionRepo;

    public UserInteractionService(UserInteractionRepo interactionRepo) {
        this.interactionRepo = interactionRepo;
    }

    public UserInteraction FindByUserIdAndPlaceIdAndType(ObjectId userId, String placeId, InteractionType type) {
        Optional<UserInteraction> retrievedPlaceId = interactionRepo.findByUserIdAndPlaceIdAndType(userId, placeId,
                type);
        return retrievedPlaceId.isPresent() ? retrievedPlaceId.get() : null;
    }

    public Page<UserInteraction> FindAllByUserIdAndType(ObjectId userId, InteractionType type,
            int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "dateInteracted"));
        Page<UserInteraction> retrievedInteractions = interactionRepo.findAllByUserIdAndType(userId,
                type, pageable);
        return retrievedInteractions;
    }

    public List<UserInteraction> FindAllByUserIdAndType(ObjectId userId, InteractionType type) {
        List<UserInteraction> retrievedInteractions = interactionRepo.findAllByUserIdAndType(userId,
                type);
        return retrievedInteractions;
    }

    public List<UserInteraction> FindAllByPlaceIdAndType(String placeId, InteractionType type) {
        List<UserInteraction> retrievedInteractions = interactionRepo.findAllByPlaceIdAndType(placeId,
                type);
        return retrievedInteractions;
    }

    public List<String> FindMostClickedPlaceIds() {
        return interactionRepo.findMostClickedPlaceIds();
    }

    public UserInteraction SaveInteraction(UserInteraction interaction) {
        return interactionRepo.save(interaction);
    }

    public Boolean DeleteInteraction(UserInteraction interaction) {
        try {
            interactionRepo.delete(interaction);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
