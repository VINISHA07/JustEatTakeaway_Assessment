package com.example.restaurant.model;

import lombok.Getter;

import java.util.List;

/**
 * Outbound response object returned to the frontend.
 * <p>
 * Wraps the filtered and limited list of {@link Restaurant} objects alongside
 * the postcode that was searched. Serialised to JSON by Jackson via Spring's
 * {@code @RestController}.
 */
@Getter
public class RestaurantResponse {

    /** The postcode used for the restaurant search. */
    private final String postcode;

    /** The list of restaurants returned, capped at the service-defined limit. */
    private final List<Restaurant> restaurants;

    /**
     * Constructs a response for the given postcode and list of restaurants.
     *
     * @param postcode    the postcode that was searched
     * @param restaurants the filtered, limited list of restaurants
     */
    public RestaurantResponse(String postcode, List<Restaurant> restaurants) {
        this.postcode = postcode;
        this.restaurants = restaurants;
    }
}
