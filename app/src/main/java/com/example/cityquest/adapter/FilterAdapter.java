package com.example.cityquest.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.R;
import com.example.cityquest.model.Filter;

import java.util.List;

public class FilterAdapter extends RecyclerView.Adapter<FilterAdapter.FilterViewHolder> {
    private List<Filter> filters;
    private Context context;

    public FilterAdapter(List<Filter> filters, Context context) {
        this.filters = filters;
        this.context = context;
    }

    @NonNull
    @Override
    public FilterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_filter_destination, parent, false);
        return new FilterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FilterViewHolder holder, int position) {
        Filter filter = filters.get(position);
        holder.textViewFilterName.setText(filter.getName());
        holder.imageViewFilterIcon.setImageResource(filter.getIconResId());

        // Set click listener for the RelativeLayout
        holder.relativeLayoutFilter.setOnClickListener(new View.OnClickListener() {
            boolean isSelected = true; // Initial background color state

            @Override
            public void onClick(View v) {

                if (isSelected) {
                    holder.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.background_color));
                    holder.relativeLayoutFilter.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_color));
                } else {
                    holder.textViewFilterName.setTextColor(ContextCompat.getColor(context, R.color.primary_color));
                    holder.relativeLayoutFilter.setBackgroundColor(ContextCompat.getColor(context, R.color.primary_color_light));
                }

                isSelected = !isSelected;
            }
        });
    }

    @Override
    public int getItemCount() {
        return filters.size();
    }

    static class FilterViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewFilterIcon;
        TextView textViewFilterName;
        RelativeLayout relativeLayoutFilter;

        public FilterViewHolder(View itemView) {
            super(itemView);
            imageViewFilterIcon = itemView.findViewById(R.id.filter_dest_filterIcon);
            textViewFilterName = itemView.findViewById(R.id.filter_dest_filterName);
            relativeLayoutFilter = itemView.findViewById(R.id.filter_dest_rl);

        }
    }
}

