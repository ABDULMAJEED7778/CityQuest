package com.example.cityquest.adapter;



import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cityquest.R;
import com.example.cityquest.model.City;

import java.util.List;

public class PopularCityAdapter extends RecyclerView.Adapter<PopularCityAdapter.CityViewHolder>{
    private List<City> cityList;

    public PopularCityAdapter(List<City> cityList) {
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.popular_city_item, parent, false);
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);
        holder.textViewCityName.setText(city.getCityName());
        holder.textViewCountryName.setText(city.getCountryName());
        holder.textViewCityRating.setText(city.getCityRating());
        // Assume you have a method to load images
        //Glide.with(holder.imageViewCity.getContext()).load(city.getImageUrl()).into(holder.imageViewCity);
        holder.relativeLayout.setBackgroundResource(city.getBackgroundDrawable());

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public static class CityViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout relativeLayout;
        TextView textViewCityName, textViewCountryName,textViewCityRating;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);
            relativeLayout = itemView.findViewById(R.id.city_item_popular_rl);
            textViewCityName = itemView.findViewById(R.id.CityName_popular_cities);
            textViewCountryName = itemView.findViewById(R.id.CountryName_popular_cities);
            textViewCityRating = itemView.findViewById(R.id.rating_popular_cities);
        }
    }
}


