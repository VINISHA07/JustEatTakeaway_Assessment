package com.example.restaurant.controller;

import com.example.restaurant.service.RestaurantService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantApiController {

    private final RestaurantService restaurantService;

    public RestaurantApiController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/bypostcode/{postcode}")
    public ResponseEntity<?> getRestaurantsByPostcode(@PathVariable String postcode) {
        try {
            return ResponseEntity.ok(restaurantService.getRestaurantsByPostcode(postcode));
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error fetching restaurants: " + e.getMessage());
        }
    }
}
