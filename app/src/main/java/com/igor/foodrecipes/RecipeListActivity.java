package com.igor.foodrecipes;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.igor.foodrecipes.adapters.OnRecipeListener;
import com.igor.foodrecipes.adapters.RecipeRecyclerAdapter;
import com.igor.foodrecipes.models.Recipe;


import com.igor.foodrecipes.util.Testing;
import com.igor.foodrecipes.util.VerticalSpacingItemDecorator;
import com.igor.foodrecipes.viewmodels.RecipeListViewModel;



import java.util.List;




public class RecipeListActivity extends BaseActivity implements OnRecipeListener {

    private static final String TAG = "RecipeListActivity";
    private RecipeListViewModel mRecipeListViewModel;
    private RecyclerView mRecyclerView;
    private RecipeRecyclerAdapter mAdapter;
    private SearchView mSearchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_list);
        mRecyclerView = findViewById(R.id.recipe_list);
        mRecipeListViewModel = ViewModelProviders.of(this).get(RecipeListViewModel.class);
        mSearchView = findViewById(R.id.search_view);

        initRecyclerView();
        subscribeObservers();
        //testRetrofitRequest();
        initSearchView();
        if (!mRecipeListViewModel.isViewingRecipes()){
            //display search categories
            displaySearchCategories();
        }
        setSupportActionBar((Toolbar)findViewById(R.id.toolbar));

    }
    private void subscribeObservers(){

        mRecipeListViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> recipes) {
                if(recipes!= null){
                    if (mRecipeListViewModel.isViewingRecipes()) {
                        Testing.printRecipes(recipes, "recipes test");
                        mRecipeListViewModel.setIsPerfomingQuery(false);
                        mAdapter.setRecipes(recipes);
                    }
                }


            }
        });
    }
    /*private void searchRecipesApi(String query,int pageNumber) {

        mRecipeListViewModel.searchRecipesApi(query, pageNumber);
    }*/
    private void initRecyclerView(){
        mAdapter = new RecipeRecyclerAdapter(this);
        VerticalSpacingItemDecorator itemDecorator = new VerticalSpacingItemDecorator(30);
        mRecyclerView.addItemDecoration(itemDecorator);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

    }
    private  void initSearchView(){

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                mAdapter.displayLoading();
                mRecipeListViewModel.searchRecipesApi(s, 1);
                mSearchView.clearFocus();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

    }


    private void testRetrofitRequest(){
        //searchRecipesApi("chicken breast",1);
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

    @Override
    public void onRecipeClick(int position) {

    }

    @Override
    public void onCategoryClick(String category) {
        mAdapter.displayLoading();
        mRecipeListViewModel.searchRecipesApi(category, 1);
        mSearchView.clearFocus();

    }
    private void displaySearchCategories(){
        Log.d(TAG, "displaySearchCategories: called.");
        mRecipeListViewModel.setIsViewingRecipes(false);
        mAdapter.displaySearchCategories();
    }

    @Override
    public void onBackPressed() {
        if(mRecipeListViewModel.onBackPressed()) {
            super.onBackPressed();
        }
        else{
            displaySearchCategories();
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.action_categories){
            displaySearchCategories();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.recipe_search_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

}
