package com.example.android.letsbake.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.letsbake.R;
import com.example.android.letsbake.models.Step;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Zsolt on 09.04.2018.
 */

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Step> stepsList;
    private final OnStepClickListener listener;

    public interface OnStepClickListener {
        void onItemClick(Step step);
    }

    /**
     * Create a new {@link StepsAdapter} object.
     *
     * @param stepsList is the list of {@link Step}s to be displayed.
     * @param listener  is an object of OnStepClickListener.
     */
    public StepsAdapter(ArrayList<Step> stepsList, OnStepClickListener listener) {
        this.stepsList = stepsList;
        this.listener = listener;
    }

    @Override
    public StepsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new StepsAdapter.ViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.item_recipe_step, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull StepsAdapter.ViewHolder holder, int position) {

        final Step step = stepsList.get(position);

        // Get the step ID and set it on the corresponding View
        int stepId = step.getStepId();
        holder.stepId.setText(context.getString(R.string.step_format, String.valueOf(stepId)));

        // Get the step description and set it on the corresponding View
        String stepShortDescription = step.getStepShortDescription();
        holder.stepShortDescription.setText(stepShortDescription);

        // Set the OnClickListener on a single recipe
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(step);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (stepsList == null) {
            return 0;
        } else {
            return stepsList.size();
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.tv_step_id)
        TextView stepId;
        @BindView(R.id.tv_step_short_description)
        TextView stepShortDescription;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    // Helper method to clear or update step list
    public void setStepsList(ArrayList<Step> stepsList) {
        this.stepsList = stepsList;
    }
}
