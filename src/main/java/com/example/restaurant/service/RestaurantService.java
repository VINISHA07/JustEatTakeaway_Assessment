package com.example.restaurant.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class RestaurantService {

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    private static final String JUST_EAT_API_BASE_URL = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode";

    public RestaurantService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    public ObjectNode getRestaurantsByPostcode(String postcode) throws Exception {
        String url = JUST_EAT_API_BASE_URL + "/" + postcode;

        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        JsonNode rootNode = mapper.readTree(response.getBody());
        JsonNode restaurantsNode = rootNode.get("restaurants");

        if (restaurantsNode == null || !restaurantsNode.isArray()) {
            return mapper.createObjectNode();
        }

        ArrayNode filteredRestaurants = mapper.createArrayNode();

        for (JsonNode restaurant : restaurantsNode) {
            ObjectNode filtered = mapper.createObjectNode();

            filtered.put("id", restaurant.get("id").asText());
            filtered.put("name", restaurant.get("name").asText());

            JsonNode address = restaurant.get("address");
            if (address != null) {
                ObjectNode addressObj = mapper.createObjectNode();
                addressObj.put("firstLine", address.get("firstLine") != null ? address.get("firstLine").asText() : "");
                addressObj.put("city", address.get("city") != null ? address.get("city").asText() : "");
                addressObj.put("postalCode", address.get("postalCode") != null ? address.get("postalCode").asText() : "");
                filtered.set("address", addressObj);
            }

            JsonNode rating = restaurant.get("rating");
            if (rating != null) {
                ObjectNode ratingObj = mapper.createObjectNode();
                ratingObj.put("starRating", rating.get("starRating") != null ? rating.get("starRating").asDouble() : 0);
                ratingObj.put("count", rating.get("count") != null ? rating.get("count").asInt() : 0);
                filtered.set("rating", ratingObj);
            }

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

        ObjectNode result = mapper.createObjectNode();
        result.put("postcode", postcode);
        result.put("count", filteredRestaurants.size());
        result.set("restaurants", filteredRestaurants);

        return result;
    }
}

