package org.example;

import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import io.restassured.response.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import java.sql.SQLOutput;
import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.databind.type.LogicalType.Map;

public class Starwars {

    @Test
    public void validateCountNumber() {
        int totalCount = 0;
        int expectedCount = 0;
        String baseUrl = "https://swapi.dev/api/people";
        String nextPageUrl = baseUrl;

        while (nextPageUrl != null) {
            RestAssured.useRelaxedHTTPSValidation();
            Response response = RestAssured.get(nextPageUrl);
            JSONObject json = new JSONObject(response.asString());

            if (expectedCount == 0) {
                expectedCount = json.getInt("count");
            }

            JSONArray results = json.getJSONArray("results");
            totalCount += results.length();

            nextPageUrl = json.optString("next", null);

            if (nextPageUrl != null && nextPageUrl.isEmpty()) nextPageUrl = null;
        }

        System.out.println("Expected Count: " + expectedCount);
        System.out.println("Summed Count: " + totalCount);

        Assert.assertEquals(totalCount, expectedCount);

        if (expectedCount == totalCount) {
            System.out.println("Count validated successfully.");
        } else {
            System.out.println("Count mismatch!");
        }
    }

    @Test
    public void validateBlueEyedCount() {
        int blueEyedCharacters = 0;
        String nextPageUrl = "https://swapi.dev/api/people";

        while (nextPageUrl != null) {
            Response response = RestAssured.given().header("Accept", "application/json")
                    .relaxedHTTPSValidation()
                    .when().get(nextPageUrl)
                    .then().statusCode(200)
                    .extract().response();

            Map<String, Object> deserializedResponse = response.as(new TypeRef<Map<String, Object>>() {});

            nextPageUrl = (String) deserializedResponse.get("next");
            List<Map<String, Object>> results = (List<Map<String, Object>>) deserializedResponse.get("results");

            for (Map<String, Object> result : results) {
                if (result.get("eye_color").equals("blue")) {
                    blueEyedCharacters += 1;
                    System.out.println(result.entrySet());
                }
            }
        }
        System.out.println("Blue eyed character count: " + blueEyedCharacters);
    }

    @Test
    public void printCreatedTime() {
        String nextPageUrl = "https://swapi.dev/api/people";

        while (nextPageUrl != null) {
            Response response = RestAssured.given().header("Accept", "application/json")
                    .relaxedHTTPSValidation()
                    .when().get(nextPageUrl)
                    .then().statusCode(200)
                    .extract().response();

            Map<String, Object> deserializedResponse = response.as(new TypeRef<Map<String, Object>>() {
            });

            nextPageUrl = (String) deserializedResponse.get("next");
            List<Map<String, Object>> results = (List<Map<String, Object>>) deserializedResponse.get("results");

            for (Map<String, Object> result : results) {
                System.out.println(result.get("created"));
            }
        }
    }

    @Test
    public void printHeightAbove170() {

        String nextPageUrl = "https://swapi.dev/api/people";

        while (nextPageUrl != null) {

            RestAssured.useRelaxedHTTPSValidation();
            Response response = RestAssured.get(nextPageUrl);
            JSONObject json = new JSONObject(response.asString());
            JSONArray results = json.getJSONArray("results");
            for (int i =0; i < results.length(); i++) {
                JSONObject character = results.getJSONObject(i);
                String heightStr = character.getString("height");

                if (!heightStr.equalsIgnoreCase("unknown")) {
                    try {
                        int height = Integer.parseInt(heightStr);
                        if (height > 172) {
                            System.out.println(character.getString("name") + " - Height: " + height );
                        }
                    } catch (NumberFormatException e) {
                        
                    }
                }
            }

            nextPageUrl = json.optString("next", null);

        }


    }
}
