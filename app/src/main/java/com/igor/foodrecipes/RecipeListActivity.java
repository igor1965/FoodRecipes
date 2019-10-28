package com.igor.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.igor.foodrecipes.models.Recipe;
import com.igor.foodrecipes.requests.RecipeApi;
import com.igor.foodrecipes.requests.ServiceGenerator;
import com.igor.foodrecipes.requests.responses.RecipeResponse;
import com.igor.foodrecipes.requests.responses.RecipeSearchResponse;
import com.igor.foodrecipes.util.Constants;
import com.igor.foodrecipes.util.Testing;
import com.igor.foodrecipes.viewmodels.RecipeListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeListActivity extends BaseActivity {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);

        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);

        subscribeObservers();

        findViewById(R.id.test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                testRetrofitRequest();
            }
        });



    }
    private void subscribeObservers(){

        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes!= null){
                    Testing.printRecipes(recipes,"recipes test");
                }


            }
        });
    }
    private void searchRecipesApi(String query,int pageNumber) {

        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }


    private void testRetrofitRequest(){
        searchRecipesApi("chicken breast",1);
       /* RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
        *//*Call<RecipeSearchResponse> responseCall = recipeApi.searchRecipe(Constants.API_KEY,
                "chicken breast","1");
        responseCall.enqueue(new Callback<RecipeSearchResponse>() {
            @Override
            public void onResponse(Call<RecipeSearchResponse> call, Response<RecipeSearchResponse> response) {
                Log.d(TAG, "onResponse: server response: " + response.toString());
                if(response.code()== 200){
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    List<Recipe> recipes = new ArrayList<>(response.body().getRecipes());
                    for (Recipe recipe:recipes){
                        Log.d(TAG, "onResponse: " + recipe.getTitle());
                    }
                }
                else{
                    try{
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
                
            }

            @Override
            public void onFailure(Call<RecipeSearchResponse> call, Throwable t) {

            }
        });*//*
        Call<RecipeResponse> responseCall = recipeApi.getRecipe(Constants.API_KEY,"8c0314");
        responseCall.enqueue(new Callback<RecipeResponse>() {
            @Override
            public void onResponse(Call<RecipeResponse> call, Response<RecipeResponse> response) {

                Log.d(TAG, "onResponse: server response: " + response.toString());
                if(response.code()== 200){
                    Log.d(TAG, "onResponse: " + response.body().toString());
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: Retrieved recipe: " + recipe.toString() );
                }
                else{
                    try{
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }

            }



            @Override
            public void onFailure(Call<RecipeResponse> call, Throwable t) {

            }
        });*/
    }
}
