package com.igor.foodrecipes.requests;

import com.igor.foodrecipes.requests.responses.RecipeResponse;
import com.igor.foodrecipes.requests.responses.RecipeSearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET SPECIFIC RECIPE
    @GET("api/get")
    Call<RecipeResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

}
