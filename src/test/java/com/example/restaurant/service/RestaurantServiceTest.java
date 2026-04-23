package com.example.restaurant.service;

import com.example.restaurant.model.Restaurant;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RestaurantServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private RestTemplateBuilder restTemplateBuilder;

    private RestaurantService service;

    @BeforeEach
    void setUp() {
        when(restTemplateBuilder.build()).thenReturn(restTemplate);
        service = new RestaurantService(restTemplateBuilder);
    }

    @Test
    void returnsRestaurantsForValidPostcode() throws Exception {
        String json = """
            {
              "restaurants": [
                {
                  "name": "Pizza Place",
                  "address": { "firstLine": "1 High St", "city": "London", "postalCode": "EC1A1BB" },
                  "rating": { "starRating": 4.5 },
                  "cuisines": [{ "name": "Italian" }]
                },
                {
                  "name": "Burger Joint",
                  "address": { "firstLine": "2 Low St", "city": "London", "postalCode": "EC1A1BB" },
                  "rating": { "starRating": 3.8 },
                  "cuisines": [{ "name": "American" }]
                }
              ]
            }
            """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        List<Restaurant> result = service.getRestaurantsByPostcode("EC1A1BB");

        assertThat(result).hasSize(2);
        assertThat(result.get(0).getName()).isEqualTo("Pizza Place");
        assertThat(result.get(1).getName()).isEqualTo("Burger Joint");
    }

    @Test
    void returnsEmptyListWhenNoRestaurantsInResponse() throws Exception {
        String json = """
            { "restaurants": [] }
            """;

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        List<Restaurant> result = service.getRestaurantsByPostcode("EC1A1BB");

        assertThat(result).isEmpty();
    }

    @Test
    void returnsEmptyListWhenRestaurantsNodeIsMissing() throws Exception {
        String json = "{}";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        List<Restaurant> result = service.getRestaurantsByPostcode("EC1A1BB");

        assertThat(result).isEmpty();
    }

    @Test
    void capsResultsAtTenRestaurants() throws Exception {
        String singleRestaurant = """
                {
                  "name": "Place %d",
                  "address": { "firstLine": "1 St", "city": "London", "postalCode": "EC1A1BB" },
                  "rating": { "starRating": 4.0 },
                  "cuisines": [{ "name": "Pizza" }]
                }
                """;

        StringBuilder restaurants = new StringBuilder("[");
        for (int i = 1; i <= 15; i++) {
            if (i > 1) restaurants.append(",");
            restaurants.append(singleRestaurant.formatted(i));
        }
        restaurants.append("]");

        String json = "{ \"restaurants\": " + restaurants + " }";

        when(restTemplate.getForObject(anyString(), eq(String.class))).thenReturn(json);

        List<Restaurant> result = service.getRestaurantsByPostcode("EC1A1BB");

        assertThat(result).hasSize(10);
    }

    @Test
    void throwsExceptionWhenApiFails() {
        when(restTemplate.getForObject(anyString(), eq(String.class)))
                .thenThrow(new RuntimeException("Connection refused"));

        assertThatThrownBy(() -> service.getRestaurantsByPostcode("EC1A1BB"))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Connection refused");
    }
}

