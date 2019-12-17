package com.igor.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.igor.foodrecipes.models.Recipe;
import com.igor.foodrecipes.repositories.RecipeRepository;
import com.igor.foodrecipes.requests.responses.RecipeResponse;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }
    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }
    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }
}
