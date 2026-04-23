package com.example.restaurant.controller;

import com.example.restaurant.model.Restaurant;
import com.example.restaurant.service.RestaurantService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * REST controller exposing the restaurant search API.
 * <p>
 * Acts as a thin HTTP layer - delegates all business logic to {@link RestaurantService}
 * and returns the result directly to the caller.
 */
@RestController
@RequestMapping("/api/restaurants")
public class RestaurantApiController {

    private final RestaurantService restaurantService;

    /**
     * Constructs the controller with the required {@link RestaurantService}.
     *
     * @param restaurantService the service handling restaurant lookups
     */
    public RestaurantApiController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    /**
     * Returns a list of restaurants for the given UK postcode.
     * <p>
     * Delegates to {@link RestaurantService#getRestaurantsByPostcode(String)} and
     * returns the first 12 results from the Just Eat API.
     *
     * @param postcode the UK postcode to search (e.g. "EC4M7RF")
     * @return {@code 200 OK} with a list of restaurants, or {@code 500} on error
     */
    @GetMapping("/bypostcode/{postcode}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByPostcode(@PathVariable String postcode) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantsByPostcode(postcode));
        } catch (Exception e) {
            return ResponseEntity.status(500).build();
        }
    }
}
