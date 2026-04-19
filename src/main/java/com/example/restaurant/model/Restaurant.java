package com.example.restaurant.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Represents a restaurant as returned by the Just Eat API.
 * <p>
 * Fields are populated by Jackson during deserialisation and may appear unused by the IDE �
 * do NOT remove them. {@code @JsonIgnoreProperties(ignoreUnknown = true)} ensures only the
 * fields declared here are mapped; all other API fields are discarded.
 */
@Getter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Restaurant {

    /** Unique identifier for the restaurant. */
    private String id;

    /** Display name of the restaurant. */
    private String name;

    /** Physical address of the restaurant. */
    private Address address;

    /** Aggregate rating information. */
    private Rating rating;

    /** List of cuisines offered by the restaurant. */
    private List<Cuisine> cuisines;

    /**
     * Physical address of a restaurant.
     * Fields are Jackson-mapped - do NOT remove even if flagged as unused by the IDE.
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Address {
        /** First line of the street address. */
        private String firstLine;
        /** City name. */
        private String city;
        /** Postal code. */
        private String postalCode;
    }

    /**
     * Rating information for a restaurant.
     * Fields are Jackson-mapped - do NOT remove even if flagged as unused by the IDE.
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Rating {
        /** Star rating out of 5. */
        private double starRating;
    }

    /**
     * Represents a single cuisine type associated with a restaurant.
     * Fields are Jackson-mapped - do NOT remove even if flagged as unused by the IDE.
     */
    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Cuisine {
        /** Human-readable cuisine name (e.g. "Italian"). */
        private String name;
        /** URL-friendly cuisine identifier (e.g. "italian"). */
        private String uniqueName;
    }
}
