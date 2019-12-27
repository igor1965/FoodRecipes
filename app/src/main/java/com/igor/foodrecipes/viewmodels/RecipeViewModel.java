package com.igor.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.igor.foodrecipes.models.Recipe;
import com.igor.foodrecipes.repositories.RecipeRepository;
import com.igor.foodrecipes.requests.responses.RecipeResponse;

public class RecipeViewModel extends ViewModel {
    private RecipeRepository mRecipeRepository;
    private String mRecipeId;
    private Boolean mDidRetrieveRecipe;

    public RecipeViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mDidRetrieveRecipe = false;
    }
    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }
    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }
    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeRepository.isRecipeRequestTimedOut();
    }

    public String getRecipeId() {
        return mRecipeId;
    }
    public void setRetrievedRecipe(boolean retrivedRecipe){
        mDidRetrieveRecipe = retrivedRecipe;
    }
    public boolean didRetriveRecipe(){
        return mDidRetrieveRecipe;
    }
}
