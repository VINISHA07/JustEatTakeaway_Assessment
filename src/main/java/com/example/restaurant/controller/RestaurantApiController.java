package com.example.restaurant.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/restaurants")
public class RestaurantApiController {

    @Autowired
    private RestTemplate restTemplate;

    private static final String JUST_EAT_API_BASE_URL = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode";

    /**
     * Fetch restaurants by postcode from Just Eat API
     * Filters response to include only: name, address, rating, cuisines
     * @param postcode The postcode to search for restaurants
     * @return Filtered JSON response with restaurant name, address, rating, and cuisines
     */
    @GetMapping("/bypostcode/{postcode}")
    public ResponseEntity<?> getRestaurantsByPostcode(@PathVariable String postcode) {
        try {
            String url = JUST_EAT_API_BASE_URL + "/" + postcode;
            
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            
            // Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            JsonNode rootNode = mapper.readTree(response.getBody());
            
            // Extract restaurants array
            JsonNode restaurantsNode = rootNode.get("restaurants");
            
            if (restaurantsNode == null || !restaurantsNode.isArray()) {
                return ResponseEntity.ok(mapper.createObjectNode());
            }
            
            // Filter restaurants to include only required fields
            ArrayNode filteredRestaurants = mapper.createArrayNode();
            
            for (JsonNode restaurant : restaurantsNode) {
                ObjectNode filtered = mapper.createObjectNode();
                
                // Extract required fields
                filtered.put("id", restaurant.get("id").asText());
                filtered.put("name", restaurant.get("name").asText());
                
                // Extract address
                JsonNode address = restaurant.get("address");
                if (address != null) {
                    ObjectNode addressObj = mapper.createObjectNode();
                    addressObj.put("firstLine", address.get("firstLine") != null ? address.get("firstLine").asText() : "");
                    addressObj.put("city", address.get("city") != null ? address.get("city").asText() : "");
                    addressObj.put("postalCode", address.get("postalCode") != null ? address.get("postalCode").asText() : "");
                    filtered.set("address", addressObj);
                }
                
                // Extract rating
                JsonNode rating = restaurant.get("rating");
                if (rating != null) {
                    ObjectNode ratingObj = mapper.createObjectNode();
                    ratingObj.put("starRating", rating.get("starRating") != null ? rating.get("starRating").asDouble() : 0);
                    ratingObj.put("count", rating.get("count") != null ? rating.get("count").asInt() : 0);
                    filtered.set("rating", ratingObj);
                }
                
                // Extract cuisines
                JsonNode cuisines = restaurant.get("cuisines");
                if (cuisines != null && cuisines.isArray()) {
                    ArrayNode cuisineArray = mapper.createArrayNode();
                    for (JsonNode cuisine : cuisines) {
                        ObjectNode cuisineObj = mapper.createObjectNode();
                        cuisineObj.put("name", cuisine.get("name") != null ? cuisine.get("name").asText() : "");
                        cuisineObj.put("uniqueName", cuisine.get("uniqueName") != null ? cuisine.get("uniqueName").asText() : "");
                        cuisineArray.add(cuisineObj);
                    }
                    filtered.set("cuisines", cuisineArray);
                }
                
                filteredRestaurants.add(filtered);
            }
            
            // Build response object
            ObjectNode result = mapper.createObjectNode();
            result.put("postcode", postcode);
            result.put("count", filteredRestaurants.size());
            result.set("restaurants", filteredRestaurants);
            
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(500).body(
                "Error fetching restaurants: " + e.getMessage()
            );
        }
    }
}
