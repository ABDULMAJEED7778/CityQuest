package com.example.cityquest.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cityquest.ExploreCityActivity;
import com.example.cityquest.R;
import com.example.cityquest.SignInActivity;
import com.example.cityquest.SignUpActivity;
import com.example.cityquest.model.City;

import java.util.List;

public class TrendingCityAdapter extends RecyclerView.Adapter<TrendingCityAdapter.CityViewHolder> {

    private Context context;
    private List<City> cityList;

    // Constructor
    public TrendingCityAdapter(Context context, List<City> cityList) {
        this.context = context;
        this.cityList = cityList;
    }

    @NonNull
    @Override
    public CityViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.explore_scroll_one, parent, false); // Inflate your layout file (item_city_card.xml)
        return new CityViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CityViewHolder holder, int position) {
        City city = cityList.get(position);

        holder.cityNameTextView.setText(city.getCityName());
        holder.countryNameTextView.setText(city.getCountryName());
        holder.ratingTextView.setText(city.getCityRating());

        // Load image using Glide
        Glide.with(context)
                .load(city.getCityImageUrl())  // Load the city image from the URL
                .placeholder(R.drawable.bangalore)  // Placeholder image while loading
                .into(holder.cityImageView);

        holder.itemView.setOnClickListener(v -> {
            // Create an Intent to navigate to the CityDetailsActivity
            Intent intent = new Intent(context, ExploreCityActivity.class);

//
//            // Pass data (e.g., city name, country name, or city object) to the next activity
//            intent.putExtra("CITY_NAME", city.getCityName());
//            intent.putExtra("COUNTRY_NAME", city.getCountryName());
//            intent.putExtra("CITY_RATING", city.getCityRating());
//            intent.putExtra("CITY_IMAGE_URL", city.getCityImageUrl());

            // Start the new activity
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return cityList.size();
    }

    public class CityViewHolder extends RecyclerView.ViewHolder {

        ImageView cityImageView, starIconImageView, locationIconImageView;
        TextView cityNameTextView, countryNameTextView, ratingTextView;

        public CityViewHolder(@NonNull View itemView) {
            super(itemView);

            cityImageView = itemView.findViewById(R.id.city_image_popularCity);
            cityNameTextView = itemView.findViewById(R.id.CityName_popular_cities);
            countryNameTextView = itemView.findViewById(R.id.CountryName_popular_cities);
            ratingTextView = itemView.findViewById(R.id.rating_popular_cities);
            starIconImageView = itemView.findViewById(R.id.star_icon_city_category);
            locationIconImageView = itemView.findViewById(R.id.location_icon_popular_cities);
        }
    }
}

