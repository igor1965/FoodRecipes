package com.igor.foodrecipes.requests;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.util.Log;

import com.igor.foodrecipes.AppExecutors;
import com.igor.foodrecipes.models.Recipe;
import com.igor.foodrecipes.requests.responses.RecipeSearchResponse;
import com.igor.foodrecipes.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

import static com.igor.foodrecipes.util.Constants.NETWORK_TIMEOUT;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;
    private MutableLiveData<List<Recipe>> mRecipes;
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;

    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public void searchRecipesApi(String query,int pageNumber){
        if (mRetrieveRecipesRunnable !=null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query,pageNumber);
        final Future handler = AppExecutors.get().networkIO().submit(mRetrieveRecipesRunnable);

        // Set a timeout for the data refresh
        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know it timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }
    private class RetrieveRecipesRunnable implements Runnable{

        private String query;
        private int pageNumber;
        private boolean cancelRequest;

        public RetrieveRecipesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {
            try {
                Response response = getRecipes(query,pageNumber).execute();
                if (cancelRequest){
                    return;
                }
                if(response.code() == 200){
                    List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                    if (pageNumber == 1){
                        mRecipes.postValue(list);
                    }
                    else{
                        List<Recipe>currentRecipes = mRecipes.getValue();
                        currentRecipes.addAll(list);
                        mRecipes.postValue(currentRecipes);
                    }
                }else{
                    String error  = response.errorBody().string();
                    Log.e(TAG, "run: " + error );
                    mRecipes.postValue(null);
                }
            } catch (IOException e) {
                e.printStackTrace();
                mRecipes.postValue(null);
            }


        }
        private Call<RecipeSearchResponse>getRecipes(String query,int pageNumber){
            return ServiceGenerator.getRecipeApi().searchRecipe(Constants.API_KEY,query,String.valueOf(pageNumber));
        }
        private void canselRequest(){
            Log.d(TAG, "canselRequest: canseling the search request");
            cancelRequest = true;
        }
    }


}
