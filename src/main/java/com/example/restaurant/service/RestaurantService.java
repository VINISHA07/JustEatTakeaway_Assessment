package com.example.restaurant.service;

import com.example.restaurant.model.Restaurant;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Service responsible for fetching and filtering restaurant data from the Just Eat API.
 * <p>
 * Calls the Just Eat enriched restaurant endpoint, deserialises the response directly
 * into {@link Restaurant} objects via Jackson, and returns a List capped at
 * {@value MAX_RESULTS} results.
 */
@Service
public class RestaurantService {

    /**
     * Maximum number of restaurants to return in a single response.
     * Adjust this value to control the result set size.
     */
    private static final int MAX_RESULTS = 12;

    /** Base URL of the Just Eat enriched restaurant API. */
    private static final String JUST_EAT_API_BASE_URL = "https://uk.api.just-eat.io/discovery/uk/restaurants/enriched/bypostcode";

    private final RestTemplate restTemplate;
    private final ObjectMapper mapper = new ObjectMapper();

    /**
     * Constructs the service with a {@link RestTemplate} built from Spring Boot's
     * auto-configured {@link RestTemplateBuilder}.
     *
     * @param builder the Spring Boot RestTemplate builder
     */
    public RestaurantService(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    /**
     * Fetches restaurants for the given postcode from the Just Eat API and returns
     * the first {@value MAX_RESULTS} results.
     *
     * @param postcode the UK postcode to search (e.g. "EC4M7RF")
     * @return up to {@value MAX_RESULTS} restaurants
     * @throws Exception if the API call fails or the response cannot be parsed
     */
    public List<Restaurant> getRestaurantsByPostcode(String postcode) throws Exception {
        String url = JUST_EAT_API_BASE_URL + "/" + postcode;

        String body = restTemplate.getForObject(url, String.class);
        JsonNode restaurantsNode = mapper.readTree(body).get("restaurants");

        List<Restaurant> restaurants = restaurantsNode == null ? List.of() :
                mapper.convertValue(restaurantsNode, new TypeReference<List<Restaurant>>() {})
                      .stream()
                      .limit(MAX_RESULTS)
                      .collect(Collectors.toList());

        return restaurants;
    }
}
