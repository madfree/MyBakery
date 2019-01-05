package com.madfree.mybakery.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.madfree.mybakery.R;
import com.madfree.mybakery.service.model.Step;

import java.util.List;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepViewHolder> {

    public static final String LOG_TAG = StepAdapter.class.getSimpleName();

    private List<Step> mStepList;
    private Context context;

    public StepAdapter() {
        Log.d(LOG_TAG, "Setting up StepAdapter");
    }

    @NonNull
    @Override
    public StepAdapter.StepViewHolder onCreateViewHolder(@NonNull ViewGroup
                                                                             viewGroup, int i) {
        Context context = viewGroup.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.steps_list_item, viewGroup, false);
        return new StepViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepViewHolder stepViewHolder, int i) {
        stepViewHolder.stepName.setText(mStepList.get(i).getShortDescription());
    }

    @Override
    public int getItemCount() {
        if (mStepList == null) {
            Log.d(LOG_TAG, "Step list is empty");
            return 0;
        }
        Log.d(LOG_TAG, "Step adapter received a list of : " + mStepList.size());
        return mStepList.size();
    }

    public List<Step> getmStepList() {
        return mStepList;
    }

    public void setmStepList(List<Step> mStepList) {
        this.mStepList = mStepList;
        notifyDataSetChanged();
    }

    class StepViewHolder extends RecyclerView.ViewHolder {
        final TextView stepName;

        StepViewHolder(@NonNull View itemView) {
            super(itemView);
            stepName = itemView.findViewById(R.id.stepNameTv);
        }
    }
}
