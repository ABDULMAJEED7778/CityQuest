package com.example.cityquest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.R;
import com.example.cityquest.model.City;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.CityViewHolder> {
    private List<City> cityList;
    private Context context;


    public CityAdapter(List<City> cityList, Context context) {
        this.cityList = cityList;
        this.context = context;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_city, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.textViewCityName.setText(city.getCityName());
        holder.textViewCountryName.setText(city.getCountryName());
        holder.textViewCityRating.setText(city.getCityRating());
        // Assume you have a method to load images
        Glide.with(context)
                .load(city.getCityImageUrl())
                .placeholder(R.drawable.image_placeholder) // Placeholder image
                .error(R.drawable.image_placeholder)
                .into(holder.cityImage);


    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        ImageView cityImage;
        TextView textViewCityName, textViewCountryName,textViewCityRating;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            cityImage = itemView.findViewById(R.id.city_image_city_category);
            textViewCityName = itemView.findViewById(R.id.CityName_city_category);
            textViewCountryName = itemView.findViewById(R.id.CountryName_city_category);
            textViewCityRating = itemView.findViewById(R.id.rating_city_category);
        }
    }
}

