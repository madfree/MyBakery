package com.madfree.mybakery.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.madfree.mybakery.R;
import com.madfree.mybakery.data.Recipe;

import java.util.List;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder> {

    private List<Recipe> recipeList;
    private Context context;

    public RecipeAdapter(List<Recipe> recipeList, Context context) {
        this.recipeList = recipeList;
        this.context = context;
    }

    class RecipeViewHolder extends RecyclerView.ViewHolder {

        final TextView recipeName;

        RecipeViewHolder(View view) {
            super(view);
            recipeName = view.findViewById(R.id.recipeNameTv);
        }
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.recipe_list_item, viewGroup, false);
        return new RecipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeViewHolder recipeViewHolder, final int position) {
        recipeViewHolder.recipeName.setText(recipeList.get(position).getRecipeName());

        recipeViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(view.getContext(), IngredientActivity.class);
                //intent.putExtra("recipeId", recipeList.get(position).getRecipeId());
                //view.getContext().startActivity(intent);
                Toast.makeText(context, "You click on receipe: " + position, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return recipeList.size();
    }

}
