package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.City;

import java.util.List;

public class CityPagerAdapter extends RecyclerView.Adapter<CityPagerAdapter.CityViewHolder> {
    private final Context context;
    private final List<City> cityList; // Assuming City is a model class holding city details

    public CityPagerAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull

    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_item_view_pager, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.textViewExplore.setText("Explore " + city.getCityName());
        holder.cityDescription.setText(city.getCityDescription());
        holder.exploreCityTV.setText("Explore " + city.getCityName());

        // Load the city photo using Glide
        Glide.with(context)
                .load(city.getCityImageUrl()) // Assuming getPhotoUri() returns the URI for the city's photo
                .into(holder.imageViewCity);

        holder.exploreCityTV.setOnClickListener(v -> {
            // Handle button click to explore the city
        });
    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    static class CityViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewCity;
        TextView textViewExplore;
        TextView cityDescription;
        TextView exploreCityTV;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewCity = itemView.findViewById(R.id.imageViewCity);
            textViewExplore = itemView.findViewById(R.id.textViewExplore);
            exploreCityTV = itemView.findViewById(R.id.exploreCityTextView);
            cityDescription = itemView.findViewById(R.id.city_desc);
        }
    }
}
