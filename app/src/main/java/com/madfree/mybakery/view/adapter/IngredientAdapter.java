package com.madfree.mybakery.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Ingredient;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.IngredientViewHolder> {

    public static final String LOG_TAG = IngredientAdapter.class.getSimpleName();

    private List<Ingredient> mIngredientList;

    public IngredientAdapter() {
        //Log.d(LOG_TAG, "Setting up IngredientAdapter");
    }

    @NonNull
    @Override
    public IngredientAdapter.IngredientViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                                 viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.ingredient_list_item, viewGroup, false);
        return new IngredientViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHolder ingredientViewHolder, int i) {
       ingredientViewHolder.quantityTextView.setText(mIngredientList.get(i).getQuantity().toString());
       ingredientViewHolder.measureTextView.setText(mIngredientList.get(i).getMeasure());
       ingredientViewHolder.ingredientNameTextView.setText(mIngredientList.get(i).getIngredient());
    }

    @Override
    public int getItemCount() {
        if (mIngredientList == null) {
            //Log.d(LOG_TAG, "Ingredient list is empty");
            return 0;
        }
        //Log.d(LOG_TAG, "Ingredient adapter received a list of : " + mIngredientList.size());
        return mIngredientList.size();
    }

    public List<Ingredient> getmIngredientList() {
        return mIngredientList;
    }

    public void setmIngredientList(List<Ingredient> mIngredientList) {
        this.mIngredientList = mIngredientList;
        notifyDataSetChanged();
    }

    class IngredientViewHolder extends RecyclerView.ViewHolder {
        final TextView quantityTextView;
        final TextView measureTextView;
        final TextView ingredientNameTextView;

        IngredientViewHolder(@NonNull View itemView) {
            super(itemView);
            quantityTextView = itemView.findViewById(R.id.ingredient_quantity_txt);
            measureTextView = itemView.findViewById(R.id.ingredient_measure_txt);
            ingredientNameTextView = itemView.findViewById(R.id.ingredient_name_txt);
        }
    }
}
