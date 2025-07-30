package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Test;

public class Pokemon {
    @Test
    public void getPokemonsTest() {
        Response response = RestAssured.given().header("Accept", "application/json")
                .when().get("https://pokeapi.co/api/v2/pokemon")
                .then().statusCode(200).extract().response();
        PokemonPojo deserializedRsponse = response.as(PokemonPojo.class);

        System.out.println(deserializedRsponse.getCount());
        System.out.println(deserializedRsponse.getNext());
    }
}
