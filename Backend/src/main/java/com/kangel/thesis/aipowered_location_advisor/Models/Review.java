package com.kangel.thesis.aipowered_location_advisor.Models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Data
@AllArgsConstructor
@Getter
public class Review {

    private int rating;
    private String text;
}
