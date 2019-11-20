package com.igor.foodrecipes.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.igor.foodrecipes.models.Recipe;
import com.igor.foodrecipes.repositories.RecipeRepository;

import java.util.List;

public class RecipeListViewModel extends ViewModel {

   // private MutableLiveData<List<Recipe>> mRecipes = new MutableLiveData<>();
    private RecipeRepository mRecipeRepository;
    private boolean mIsViewingRecipes;

    public RecipeListViewModel() {

        mRecipeRepository = RecipeRepository.getInstance();
    }
    public LiveData<List<Recipe>> getRecipes() {
        mIsViewingRecipes = false;
        return mRecipeRepository.getRecipes();
    }
    public void searchRecipesApi(String query,int pageNumber) {
        mIsViewingRecipes = true;
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }
    public boolean isViewingRecipes() {
        return mIsViewingRecipes;
    }

    public void setIsViewingRecipes(boolean isViewingRecipes){
        mIsViewingRecipes = isViewingRecipes;
    }


}
