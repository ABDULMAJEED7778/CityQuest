package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.R;
import com.google.android.libraries.places.api.model.AutocompletePrediction;

import java.util.List;

public class PlaceAutocompleteAdapter extends RecyclerView.Adapter<PlaceAutocompleteAdapter.ViewHolder> {

    private final Context context;
    private final List<AutocompletePrediction> predictions;
    private OnItemClickListener onItemClickListener;

    public PlaceAutocompleteAdapter(Context context, List<AutocompletePrediction> predictions) {
        this.context = context;
        this.predictions = predictions;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.place_autocomplete_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AutocompletePrediction prediction = predictions.get(position);
        holder.textView.setText(prediction.getFullText(null).toString());

        holder.itemView.setOnClickListener(v -> {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return predictions.size();
    }

    public void updateData(List<AutocompletePrediction> newPredictions) {
        predictions.clear();
        if (newPredictions != null) {
            predictions.addAll(newPredictions);
        }
        notifyDataSetChanged();
    }

    public AutocompletePrediction getItemAt(int position) {
        return predictions.get(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.place_name);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }
}
